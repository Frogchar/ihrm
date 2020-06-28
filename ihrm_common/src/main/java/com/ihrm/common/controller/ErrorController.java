package com.ihrm.common.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songc
 * @version 1.0
 * @date 2020/6/28 10:26
 * @description
 * @Email songchao_ss@163.com
 */
@RestController
@CrossOrigin
public class ErrorController {

    @RequestMapping("/autherror")
    public Result autherror(int code) {
        return code==1? new Result(ResultCode.UNAUTHENTICATED):new Result(ResultCode.UNAUTHORISE);
    }
}
