package com.ihrm.system.dao;

import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Frogchar
 * @version 1.0
 * @date 2020/6/18 11:54
 * @Email songchao_ss@163.com
 */
public interface PermissionDao extends JpaRepository<Permission,String>, JpaSpecificationExecutor<Permission> {
    List<Permission> findByTypeAndPid(Integer type,String pid);
}
