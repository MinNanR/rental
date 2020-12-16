package site.minnan.rental.infrastructure.enumerate;

/**
 * 用户类型枚举
 * created by Minnan on 2020/12/17
 */
public enum Role {

    ADMIN("管理员", 1),
    LANDLORD("房东", 2),
    TENANT("房客 ", 3);

    private final String roleName;

    private final Integer roleId;

    Role(String roleName, Integer roleId){
        this.roleName = roleName;
        this.roleId = roleId;
    }

    public String roleName(){
        return roleName;
    }

    public Integer roleId() {
        return roleId;
    }
}
