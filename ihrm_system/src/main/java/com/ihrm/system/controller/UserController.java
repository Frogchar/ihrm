package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Frogchar
 * @version 1.0
 * @date 2020/6/17 15:47
 * @Email songchao_ss@163.com
 */
@CrossOrigin
@RestController
@RequestMapping(value="/sys")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    PermissionService permissionService;

    /**
     * 获取用户信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/profile",method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception {

        String userId = claims.getId();
        User user = userService.findById(userId);

        ProfileResult profileResult = null;

        if ("user".equals(user.getLevel())) {
            profileResult = new ProfileResult(user);
        } else {
            Map map = new HashMap();
            if ("coAdmin".equals(user.getLevel())) {
                map.put("enVisible", "1");
            }
            List<Permission> perms = permissionService.findAll(map);
            profileResult = new ProfileResult(user, perms);
        }
        return new Result(ResultCode.SUCCESS, profileResult);
    }

    /**
     * 登录
     */
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String, String> loginMap) throws CommonException {
        String mobile = loginMap.get("mobile");
        String password = loginMap.get("password");
        // 改造后
        try {
            password = new Md5Hash(password, mobile, 3).toString();
            UsernamePasswordToken passwordToken = new UsernamePasswordToken(mobile, password);
            // shiro 调用releam 完成登录
            Subject subject = SecurityUtils.getSubject();
            subject.login(passwordToken);
            // 获取sessionid
            String sessionId = (String)subject.getSession().getId();
            return new Result(ResultCode.SUCCESS, sessionId);
        } catch (Exception e) {
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }

        // 改造前
//        User user = userService.findByMobile(mobile);
//        if (user == null || !password.equals(user.getPassword())) {
//            throw new CommonException(ResultCode.MOBILEORPASSWORDERROR);
//        }
//        Map<String,Object> map = new HashMap<>();
//        StringBuilder sb = new StringBuilder();
//        // 添加权限
//        for (Role role : user.getRoles()) {
//            for (Permission permission : role.getPermissions()) {
//                // api权限
//                if (PermissionConstants.PY_API == permission.getType()) {
//                    String code = permission.getCode();
//                    sb.append(code).append(",");
//                }
//            }
//        }
//        map.put("apis", sb.toString());
//        map.put("companyId",user.getCompanyId());
//        map.put("companyName",user.getCompanyName());
//        String jwt = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
//        return new Result(ResultCode.SUCCESS, jwt);
    }

    /**
     * 分配角色
     */
    @RequestMapping(value = "/user/assignRoles", method = RequestMethod.PUT)
    public Result assignRoles(@RequestBody Map<String,Object> map) {
        //1.获取被分配的用户id
        String userId = (String) map.get("id");
        //2.获取到角色的id列表
        List<String> roleIds = (List<String>) map.get("roleIds");
        //3.调用service完成角色分配
        userService.assignRoles(userId,roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Result save(@RequestBody User user) {
        //1.设置保存的企业id
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        //2.调用service完成保存企业
        userService.save(user);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的部门列表
     * 指定企业id
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Result findAll(int page, int size, @RequestParam Map map) {
        //1.获取当前的企业id
        map.put("companyId",companyId);
        //2.完成查询
        Page<User> pageUser = userService.findAll(map,page,size);
        //3.构造返回结果
        PageResult pageResult = new PageResult(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS, pageResult);
    }

    /**
     * 根据ID查询user
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) {
        User user = userService.findById(id);
        return new Result(ResultCode.SUCCESS, user);
    }

    /**
     * 修改User
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id, @RequestBody User user) {
        //1.设置修改的部门id
        user.setId(id);
        //2.调用service更新
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE,name = "point-user-delete")
    public Result delete(@PathVariable(value = "id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
