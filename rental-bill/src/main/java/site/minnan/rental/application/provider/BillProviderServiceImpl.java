package site.minnan.rental.application.provider;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.domain.entity.BillDetails;
import site.minnan.rental.domain.entity.BillTenantRelevance;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.BillMapper;
import site.minnan.rental.domain.mapper.BillTenantRelevanceMapper;
import site.minnan.rental.domain.vo.SettleQueryVO;
import site.minnan.rental.domain.vo.UtilityPrice;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.infrastructure.enumerate.BillType;
import site.minnan.rental.infrastructure.utils.ReceiptUtils;
import site.minnan.rental.userinterface.dto.CreateBillDTO;
import site.minnan.rental.userinterface.dto.SettleQueryDTO;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service(timeout = 30000, interfaceClass = BillProviderService.class)
@Slf4j
public class BillProviderServiceImpl implements BillProviderService {

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillTenantRelevanceMapper billTenantRelevanceMapper;

    @Autowired
    private BillService billService;

    @Autowired
    private ReceiptUtils receiptUtils;

    @Reference(check = false)
    private RoomProviderService roomProviderService;

    @Reference(check = false)
    private UtilityProviderService utilityProviderService;

    /**
     * 房间由空闲转入在租状态时创建账单
     *
     * @param dto
     */
    @Override
    public void createBill(CreateBillDTO dto) {
        DateTime now = DateTime.now();
        JSONObject roomInfo = roomProviderService.getRoomInfo(dto.getRoomId());
        Integer currentUtilityId = utilityProviderService.getCurrentUtility(dto.getRoomId());
        UtilityPrice price = billService.getUtilityPrice();
        //入住账单
        Bill checkInBill = Bill.builder()
                .year(now.year())
                .month(now.month() + 1)
                .houseId(roomInfo.getInt("houseId"))
                .houseName(roomInfo.getStr("houseName"))
                .roomId(dto.getRoomId())
                .roomNumber(roomInfo.getStr("roomNumber"))
                .accessCardQuantity(dto.getCardQuantity())
                .accessCardCharge(dto.getCardQuantity() * price.getAccessCardPrice())
                .deposit(dto.getDeposit())
                .rent(roomInfo.getInt("price"))
                .remark(dto.getRemark())
                .completedDate(now)
                .utilityStartId(currentUtilityId)
                .status(BillStatus.PAID)//TODO 确认是否当面收款
                .type(BillType.CHECK_IN)
                .build();
        checkInBill.setCreateUser(dto.getUserId(), dto.getUserName(), new Timestamp(now.getTime()));
        DateTime nextMonth = now.offsetNew(DateField.MONTH, 1);
        Bill monthlyBill = Bill.builder()
                .year(now.year())
                .month(nextMonth.month() + 1)
                .houseId(roomInfo.getInt("houseId"))
                .houseName(roomInfo.getStr("houseName"))
                .roomId(dto.getRoomId())
                .roomNumber(roomInfo.getStr("roomNumber"))
                .rent(0)
                .completedDate(nextMonth)
                .utilityStartId(currentUtilityId)
                .status(BillStatus.INIT)
                .type(BillType.MONTHLY)
                .build();
        monthlyBill.setCreateUser(dto.getUserId(), dto.getUserName(), new Timestamp(now.getTime()));
        billMapper.insertBatch(CollectionUtil.newArrayList(checkInBill, monthlyBill));
        List<BillTenantRelevance> relevanceList = dto.getTenantIdList().stream()
                .flatMap(e -> Stream.of(BillTenantRelevance.of(checkInBill.getId(), e),
                        BillTenantRelevance.of(monthlyBill.getId(), e)))
                .collect(Collectors.toList());
        billTenantRelevanceMapper.insertBatch(relevanceList);
        //TODO 生成入住收据
    }

    @Override
    public void completeBillWithSurrender(Integer roomId) {
        BillDetails bill = billMapper.getBillDetailsByRoomId(roomId);
        if (bill != null) {
            SettleQueryDTO dto = new SettleQueryDTO(bill.getRoomId(), bill.getUtilityStartId());
            SettleQueryVO settle = utilityProviderService.getUtility(dto);
            JSONObject start = settle.getUtilityStart();
            JSONObject end = settle.getUtilityEnd();
            UtilityPrice price = billService.getUtilityPrice();
            bill.settleWater(start.getBigDecimal("water"), end.getBigDecimal("water"), price.getWaterPrice());
            bill.settleElectricity(start.getBigDecimal("electricity"), end.getBigDecimal("electricity"),
                    price.getElectricityPrice());
            bill.setUtilityEndId(end.getInt("id"));
            bill.setUpdateUser(JwtUser.builder().id(0).realName("系统").build());
            bill.unsettled();
            try {
                receiptUtils.generateReceipt(bill);
            } catch (IOException e) {
                log.error("生成收据失败");
            }
            Date date = new Date();
            bill.surrenderCompleted(date);
            billMapper.settleBatch(Collections.singletonList(bill));
        }

    }
}
