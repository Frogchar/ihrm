package com.ihrm.company.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author SC
 * @Date 2020/6/9 0:43
 **/
@Service
public class CompanyService {

    @Resource
    private CompanyDao companyDao;

    @Resource
    private IdWorker idWorker;

    public void add(Company company) {
        String id = idWorker.nextId() + "";
        company.setId(id);
        //默认的状态
        company.setAuditState("0");//0：未审核，1：已审核
        company.setState(1); //0.未激活，1：已激活
        companyDao.save(company);
    }

    public void update(Company company) {
        Company temp = companyDao.findById(company.getId()).get();
        temp.setName(company.getName());
        temp.setCompanyPhone(company.getCompanyPhone());
        companyDao.save(temp);
    }

    /**
     * 删除企业
     */
    public void deleteById(String id) {
        companyDao.deleteById(id);
    }

    /**
     * 根据id查询企业
     */
    public Company findById(String id) {
        return companyDao.findById(id).get();
    }

    /**
     * 查询企业列表
     */
    public List<Company> findAll() {
        return companyDao.findAll();
    }

}
