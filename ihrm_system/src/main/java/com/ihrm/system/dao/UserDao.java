package com.ihrm.system.dao;

import com.ihrm.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Frogchar
 * @version 1.0
 * @date 2020/6/17 15:30
 * @Email songchao_ss@163.com
 */
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {
}
