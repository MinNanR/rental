package site.minnan.rental.application.provider;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.domain.entity.BillTenantRelevance;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.BillMapper;
import site.minnan.rental.domain.mapper.BillTenantRelevanceMapper;
import site.minnan.rental.domain.vo.SettleQueryVO;
import site.minnan.rental.domain.vo.UtilityPrice;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.userinterface.dto.CreateBillDTO;
import site.minnan.rental.userinterface.dto.SettleQueryDTO;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service(timeout = 30000, interfaceClass = BillProviderService.class)
@Slf4j
public class BillProviderServiceImpl implements BillProviderService {

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillTenantRelevanceMapper billTenantRelevanceMapper;

    @Autowired
    private BillService billService;

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
        Bill bill = Bill.builder()
                .year(now.year())
                .month(now.month() + 1)
                .houseId(roomInfo.getInt("houseId"))
                .houseName(roomInfo.getStr("houseName"))
                .roomId(dto.getRoomId())
                .roomNumber(roomInfo.getStr("roomNumber"))
                .floor(roomInfo.getInt("floor"))
                .rent(roomInfo.getInt("price"))
                .completedDate(now.offsetNew(DateField.MONTH, 1))
                .utilityStartId(currentUtilityId)
                .status(BillStatus.INIT)
                .build();
        bill.setCreateUser(dto.getUserId(), dto.getUserName(), new Timestamp(now.getTime()));
        billMapper.insert(bill);
        List<BillTenantRelevance> relevanceList =
                dto.getTenantIdList().stream().map(e -> BillTenantRelevance.of(bill.getId(), e)).collect(Collectors.toList());
        billTenantRelevanceMapper.insertBatch(relevanceList);
    }

    @Override
    public void completeBillWithSurrender(Integer roomId) {
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId)
                .eq("status", BillStatus.INIT);
        Bill bill = billMapper.selectOne(queryWrapper);
        if (bill != null) {
            SettleQueryDTO dto = new SettleQueryDTO(bill.getRoomId(), bill.getUtilityStartId());
            SettleQueryVO settle = utilityProviderService.getUtility(dto);
            JSONObject start = settle.getUtilityStart();
            JSONObject end = settle.getUtilityEnd();
            UtilityPrice price = billService.getUtilityPrice();
            bill.settleWater(start.getBigDecimal("water"), end.getBigDecimal("water"), price.getWaterPrice());
            bill.settleElectricity(start.getBigDecimal("electricity"), end.getBigDecimal("electricity"), price.getElectricityPrice());
            bill.setUtilityEndId(end.getInt("id"));
            bill.setUpdateUser(JwtUser.builder().id(0).realName("系统").build());
            bill.unsettled();
            billMapper.settleBatch(Collections.singletonList(bill));
        }

    }
}
