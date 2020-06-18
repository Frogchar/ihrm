package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.system.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Frogchar
 * @version 1.0
 * @date 2020/6/17 15:47
 * @Email songchao_ss@163.com
 */
//1.解决跨域
@CrossOrigin
//2.声明restContoller
@RestController
//3.设置父路径
@RequestMapping(value="/sys")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;

    /**
     * 获取用户信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/profile",method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(authorization)) {
            throw new CommonException(ResultCode.UNAUTHORISE);
        }
        String token = authorization.replace("Bearer ", "");
        Claims claims = jwtUtils.parseJwt(token);
        if (claims == null) {
            throw new CommonException(ResultCode.UNAUTHORISE);
        }
        String userId = claims.getId();
        User user = userService.findById(userId);
        ProfileResult profileResult = new ProfileResult(user);
        return new Result(ResultCode.SUCCESS, profileResult);
    }

    /**
     * 登录
     */
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String, String> loginMap) throws CommonException {
        String mobile = loginMap.get("mobile");
        String password = loginMap.get("password");
        User user = userService.findByMobile(mobile);
        if (user == null || !password.equals(user.getPassword())) {
            throw new CommonException(ResultCode.MOBILEORPASSWORDERROR);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("companyId",user.getCompanyId());
        map.put("companyName",user.getCompanyName());
        String jwt = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
        return new Result(ResultCode.SUCCESS, jwt);
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
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
