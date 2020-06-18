package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Frogchar
 * @version 1.0
 * @date 2020/6/18 11:12
 * @Email songchao_ss@163.com
 */
@Service
public class RoleService extends BaseService {
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RoleDao roleDao;

    @Resource
    PermissionDao permissionDao;

    /**
     * 添加角色
     */
    public void save(Role role) {
        //填充其他参数
        role.setId(idWorker.nextId() + "");
        roleDao.save(role);
    }

    /**
     * 更新角色
     */
    public void update(Role role) {
        Role targer = roleDao.getOne(role.getId());
        targer.setDescription(role.getDescription());
        targer.setName(role.getName());
        roleDao.save(targer);
    }

    /**
     * 根据ID查询角色
     */
    public Role findById(String id) {
        return roleDao.findById(id).get();
    }

    public List<Role> findAll(String companyId) {
        return roleDao.findAll(getSpec(companyId));
    }

    /**
     * 删除角色
     */
    public void delete(String id) {
        roleDao.deleteById(id);
    }

    public Page<Role> findByPage(String companyId, int page, int size) {
        return roleDao.findAll(getSpec(companyId), PageRequest.of(page-1, size));
    }

    /**
     * 分配权限
     * @param roleId
     * @param permIds
     */
    public void assignPerms(String roleId, List<String> permIds) {
        Role role = roleDao.findById(roleId).get();
        //2.构造角色的权限集合
        Set<Permission> perms = new HashSet<>();
        for (String permId : permIds) {
            Permission permission = permissionDao.findById(permId).get();
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PY_API, permission.getId());
            perms.addAll(apiList);
            perms.add(permission);//当前菜单或按钮的权限
        }
        role.setPermissions(perms);
        //4.更新角色
        roleDao.save(role);
    }
}
