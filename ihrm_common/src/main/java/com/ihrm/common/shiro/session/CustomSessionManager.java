package com.ihrm.common.shiro.session;

import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author songc
 * @version 1.0
 * @date 2020/6/28 10:43
 * @description 自定义sessionManager
 * @Email songchao_ss@163.com
 */
public class CustomSessionManager extends DefaultWebSessionManager {

    /**
     * 获取sessionId
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //获取请求头Authorization中的数据
        String id = WebUtils.toHttp(request).getHeader("Authorization");
        if (StringUtils.isEmpty(id)) {
            //如果没有携带，生成新的sessionId
            return super.getSessionId(request,response);
        } else {
            // 返回id
            id = id.replaceAll("Bearer ","");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }

    }


}
