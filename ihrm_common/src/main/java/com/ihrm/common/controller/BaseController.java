package com.ihrm.common.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Frogchar
 * @version 1.0
 * @date 2020/6/17 15:51
 * @Email songchao_ss@163.com
 */

public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;

    @ModelAttribute
    public void setResAnReq(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        /**
         * 目前使用 companyId = 1
         *         companyName = "传智播客"
         */
        this.companyId = "1";
        this.companyName = "传智播客";
    }
}
