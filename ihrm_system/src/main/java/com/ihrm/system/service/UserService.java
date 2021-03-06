package com.ihrm.system.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.client.DepartmentFeignClient;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author Frogchar
 * @version 1.0
 * @date 2020/6/17 15:36
 * @Email songchao_ss@163.com
 */
@Service
public class UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private IdWorker idWorker;

    @Resource
    private RoleDao roleDao;

    @Resource
    private DepartmentFeignClient departmentFeignClient;

    /**
     * 1.保存用户
     */
    public void save(User user) {
        String id = idWorker.nextId() + "";
        user.setId(id);
        user.setPassword("123456");//设置初始密码
        user.setEnableState(1);
        userDao.save(user);
    }

    /**
     * 2.更新用户
     */
    public void update(User user) {
        User target = userDao.findById(user.getId()).get();
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        userDao.save(target);
    }

    /**
     * 3.根据id查询用户
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 4.查询全部用户列表
     *      参数：map集合的形式
     *          hasDept
     *          departmentId
     *          companyId
     *
     */
    public Page findAll(Map<String,Object> map, int page, int size) {
        Specification<User> spec = new Specification<User>() {
            /**
             * 动态拼接查询条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if(!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                //根据请求的部门id构造查询条件
                if(!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));
                }
                if(!StringUtils.isEmpty(map.get("hasDept"))) {
                    //根据请求的hasDept判断  是否分配部门 0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                    if("0".equals((String) map.get("hasDept"))) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<User> users = userDao.findAll(spec, new PageRequest(page - 1, size));
        return users;
    }

    /**
     * 5.根据id删除用户
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    public User findByMobile(String mobile) {
        return userDao.findUserByMobile(mobile);
    }

    public void assignRoles(String userId, List<String> roleIds) {
        //1.根据id查询用户
        User user = userDao.findById(userId).get();
        //2.设置用户的角色集合
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和角色集合的关系
        user.setRoles(roles);
        //3.更新用户
        userDao.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAll(List<User> userList, String companyId, String companyName) {
        for (User user : userList) {
            user.setPassword(new Md5Hash("123456",user.getMobile(),3).toString());
            //id
            user.setId(idWorker.nextId()+"");
            //基本属性
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setInServiceStatus(1);
            user.setEnableState(1);
            user.setLevel("user");

            Department department=departmentFeignClient.findByCode(user.getDepartmentId(), companyId);
            if (department != null) {
                user.setDepartmentId(department.getId());
                user.setDepartmentName(department.getName());
            }
            userDao.save(user);
        }
    }
}
