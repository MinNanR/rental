package site.minnan.rental.application.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.minnan.rental.application.provider.UserProviderService;
import site.minnan.rental.application.service.TenantService;
import site.minnan.rental.domain.aggregate.Tenant;
import site.minnan.rental.domain.entity.JwtUser;
import site.minnan.rental.domain.mapper.TenantMapper;
import site.minnan.rental.domain.vo.ListQueryVO;
import site.minnan.rental.domain.vo.TenantInfoVO;
import site.minnan.rental.domain.vo.TenantVO;
import site.minnan.rental.infrastructure.enumerate.Gender;
import site.minnan.rental.infrastructure.exception.EntityAlreadyExistException;
import site.minnan.rental.infrastructure.exception.EntityNotExistException;
import site.minnan.rental.userinterface.dto.AddTenantDTO;
import site.minnan.rental.userinterface.dto.AddTenantUserDTO;
import site.minnan.rental.userinterface.dto.DetailsQueryDTO;
import site.minnan.rental.userinterface.dto.GetTenantListDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantMapper tenantMapper;

    @Reference
    private UserProviderService userProviderService;

    /**
     * 添加房客
     *
     * @param dto
     */
    @Override
    @Transactional
    public void addTenant(AddTenantDTO dto) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer check = tenantMapper.checkTenantExistByIdentificationNumber(dto.getIdentificationNumber());
        if (check != null) {
            throw new EntityAlreadyExistException("房客已存在");
        }
        Tenant tenant = Tenant.builder()
                .name(dto.getName())
                .gender(Gender.valueOf(dto.getGender()))
                .birthday(dto.getBirthday())
                .phone(dto.getPhone())
                .identificationNumber(dto.getIdentificationNumber())
                .hometownProvince(dto.getHometownProvince())
                .hometownCity(dto.getHometownCity())
                .build();
        tenant.setCreateUser(jwtUser);
        tenantMapper.insert(tenant);
        AddTenantUserDTO tenantUserDTO = AddTenantUserDTO.builder()
                .phone(dto.getPhone())
                .realName(dto.getName())
                .userId(jwtUser.getId())
                .userName(jwtUser.getRealName())
                .build();
        userProviderService.createTenantUser(tenantUserDTO);
    }

    /**
     * 列表查询房客
     *
     * @param dto
     * @return
     */
    @Override
    public ListQueryVO<TenantVO> getTenantList(GetTenantListDTO dto) {
        QueryWrapper<Tenant> wrapper = new QueryWrapper<>();
        Optional.ofNullable(dto.getName()).ifPresent(s -> wrapper.like("name", s));
        Optional.ofNullable(dto.getPhone()).ifPresent(s -> wrapper.like("phone", s));
        Optional.ofNullable(dto.getHometownProvince()).ifPresent(s -> wrapper.eq("hometown_province", s));
        Optional.ofNullable(dto.getHometownCity()).ifPresent(s -> wrapper.eq("hometown_city", s));
        Page<Tenant> queryPage = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<Tenant> page = tenantMapper.selectPage(queryPage, wrapper);
        List<TenantVO> voList = page.getRecords().stream().map(TenantVO::assemble).collect(Collectors.toList());
        return new ListQueryVO<>(voList, page.getTotal());
    }

    /**
     * 查询房客详情
     *
     * @param dto
     * @return
     */
    @Override
    public TenantInfoVO getTenantInfo(DetailsQueryDTO dto) {
        Tenant tenant = tenantMapper.selectById(dto.getId());
        if(tenant == null){
            throw new EntityNotExistException("房客不存在");
        }
        return TenantInfoVO.assemble(tenant);
    }

}
