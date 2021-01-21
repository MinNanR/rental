package site.minnan.rental.application.provider;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.domain.mapper.BillMapper;
import site.minnan.rental.infrastructure.enumerate.BillStatus;
import site.minnan.rental.userinterface.dto.CreateBillDTO;

import java.sql.Timestamp;

@Service(timeout = 5000, interfaceClass = BillProviderService.class)
@Slf4j
public class BillProviderServiceImpl implements BillProviderService {

    @Autowired
    private BillMapper billMapper;

    @Reference(check = false)
    private RoomProviderService roomProviderService;

    /**
     * 房间由空闲转入在租状态时创建账单
     *
     * @param dto
     */
    @Override
    public void createBill(CreateBillDTO dto) {
        DateTime now = DateTime.now();
        JSONObject roomInfo = roomProviderService.getRoomInfo(dto.getRoomId());
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
                .status(BillStatus.INIT)
                .build();
        bill.setCreateUser(dto.getUserId(), dto.getUserName(), new Timestamp(now.getTime()));
        billMapper.insert(bill);
    }

    @Override
    public void completeBillWithSurrender(Integer roomId) {
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId)
                .eq("status", BillStatus.INIT);
        Bill bill = billMapper.selectOne(queryWrapper);
        if (bill != null) {
            UpdateWrapper<Bill> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("status", BillStatus.UNRECORDED)
                    .eq("id", bill.getId());
            billMapper.update(null, updateWrapper);
        }
    }
}
