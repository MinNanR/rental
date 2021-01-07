package site.minnan.rental.application.provider;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.domain.aggregate.Utility;
import site.minnan.rental.domain.mapper.BillMapper;
import site.minnan.rental.domain.mapper.UtilityMapper;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.infrastructure.enumerate.UtilityStatus;
import site.minnan.rental.userinterface.dto.CreateBillDTO;
import site.minnan.rental.userinterface.response.ResponseEntity;

import java.sql.Timestamp;

@Service(timeout = 5000, interfaceClass = BillProviderService.class)
@Slf4j
public class BillProviderServiceImpl implements BillProviderService {

    @Autowired
    private UtilityMapper utilityMapper;

    @Autowired
    private BillMapper billMapper;

    /**
     * 房间由空闲转入在租状态时创建账单
     *
     * @param dto
     */
    @Override
    public void createBill(CreateBillDTO dto) {
        DateTime now = DateTime.now();
        JSONObject roomInfo = dto.getRoomInfo();
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
                .status(BillStatus.UNCOMPLETED)
                .build();
        bill.setCreateUser(dto.getUserId(), dto.getUserName(), new Timestamp(now.getTime()));
        billMapper.insert(bill);
        Utility utility = Utility.builder()
                .year(now.year())
                .month(now.month())
                .houseId(roomInfo.getInt("houseId"))
                .houseName(roomInfo.getStr("houseName"))
                .roomId(dto.getRoomId())
                .roomNumber(roomInfo.getStr("roomNumber"))
                .floor(roomInfo.getInt("floor"))
                .billId(bill.getId())
                .status(UtilityStatus.USING)
                .build();
        utility.setCreateUser(dto.getUserId(), dto.getUserName(), new Timestamp(now.getTime()));
        utilityMapper.insert(utility);
    }
}
