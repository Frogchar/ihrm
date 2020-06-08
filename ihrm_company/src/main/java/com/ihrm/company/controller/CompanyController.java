package com.ihrm.company.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author SC
 * @Date 2020/6/9 0:46
 **/
@RestController
@RequestMapping(value="/company")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    //保存企业
    @RequestMapping(value="",method = RequestMethod.POST)
    public Result save(@RequestBody Company company)  {
        //业务操作
        companyService.add(company);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id更新企业
    /**
     * 1.方法
     * 2.请求参数
     * 3.响应
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id, @RequestBody Company company ) {
        //业务操作
        company.setId(id);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id删除企业
    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        companyService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id查询企业
//    @RequestMapping(value="/{id}",method = RequestMethod.GET)
//    public Result findById(@PathVariable(value="id") String id) throws CommonException {
//        throw new CommonException(ResultCode.UNAUTHORISE);
//        Company company = companyService.findById(id);
//        Result result = new Result(ResultCode.SUCCESS);
//        result.setData(company);
//        return result;
//    }

    //查询全部企业列表
    @RequestMapping(value="",method = RequestMethod.GET)
    public Result findAll() {
        int i = 1/0;
        List<Company> list = companyService.findAll();
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(list);
        return result;
    }
}
