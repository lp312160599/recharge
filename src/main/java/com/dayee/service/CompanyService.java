package com.dayee.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dayee.dao.CompanyDao;
import com.dayee.model.Company;
import com.dayee.model.CompanySystem;
import com.dayee.model.PaidAccount;
import com.dayee.model.SecurityCode;
import com.dayee.model.Systems;
import com.dayee.model.User;
import com.dayee.utils.EncryptUtils;
import com.dayee.utils.ObjectUtil;
import com.dayee.utils.QueryUtil;
import com.dayee.utils.StringUtils;
import com.dayee.vo.Orders;
import com.dayee.vo.Page;
import com.dayee.vo.QueryParameter;

@Service
public class CompanyService extends BaseService<Company>{

	@Resource
	protected CompanyDao companyDao;
	@Resource
	protected PaidAccountService pidAccountService;
	
	public List<Company> queryCompany(QueryParameter queryParameter,Page page,Orders orders) throws Exception{
		String name = StringUtils.isEmpty(queryParameter.getName())?null:"%"+queryParameter.getName()+"%";
		String sysName = StringUtils.isEmpty(queryParameter.getSystemName())?null:"%"+queryParameter.getSystemName()+"%";
		List<Company> companyList = companyDao.selectCompany(name,sysName,getOrderBy(orders),page);
		int count  = 0;
		if(companyList.size()>0){
			 count  = companyDao.getCompanyCount(name,sysName);
		}
		page.setCount(count);
		return companyList;
	}
	
	public CompanySystem getCompanySystem(String systemName,int companyId) throws Exception{
		QueryUtil queryUtil = QueryUtil.createQuery().eq("name", systemName)
                .eq("companyId", companyId);
        return query(queryUtil.get(),CompanySystem.class).get(0);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public synchronized void updateCompany(Company company,User user,List<CompanySystem> companySystemList) throws Exception{
	   log.info("更新企业信息========>company:"+getColumnValues(company)+",=====>user:"+user);
		if(company.getId()==null){
			company.setCreateUser(user.getId());
			addObject(company); 
		}else{
			company = queryById(company.getId());
		}
		
		for (int i = 0; i < companySystemList.size(); i++) {
			CompanySystem companySystem = companySystemList.get(i);  
			 log.info("更新企业信息========>companySystem:"+getColumnValues(companySystem));
			Integer addNumber = companySystem.getSecurityCodeCountNumber();
			if(companySystem.getId()!=null){
				CompanySystem updateCompanySystem =  queryById(companySystem.getId(), CompanySystem.class);
				if(addNumber!=null){
					companySystem.setSecurityCodeCountNumber(addNumber+updateCompanySystem.getSecurityCodeCountNumber());
				}
				companySystem = ObjectUtil.merge(companySystem, updateCompanySystem);
				updateObjectById(companySystem);
			}else{
				companySystem.setCompanyId(company.getId());
				companySystem.setCompanyName(company.getName());
				companySystem.setCreateUserId(user.getId());
				companySystem.setCreateUserName(user.getName());
				companySystem.setSecretKey(EncryptUtils.encode(company.getCode()+"_"+companySystem.getSystemId()));
				
				Systems system = queryById(companySystem.getSystemId(),Systems.class);
				companySystem.setName(system.getName());
				system.setUseCompany(system.getUseCompany()+1);
				addObject(companySystem);
				updateObjectById(system);
				if(companySystem.getSystemId().equals(CompanySystem.TYPE_DAYEE_VIDEO)){
				    pidAccountService.createNewPidAccount("默认账号", PaidAccount.EFFECTIVE,companySystem.getId());
                }
			}
			if(addNumber!=null){
				for (int z = 0; z < addNumber; z++) {
					SecurityCode securityCode = new SecurityCode();
					securityCode.setCompanySystemId(companySystem.getId());
					securityCode.setSecretKey(companySystem.getSecretKey());
					securityCode.setState("未使用");
					addObject(securityCode);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(EncryptUtils.encode("KINGHOOD_6"));
	}
	
	public void deleteCompany(int id) throws Exception{
		
		QueryUtil queryUtil = QueryUtil.createQuery().addColumn("companyId",id);
		List<CompanySystem> companySystemList = query(queryUtil.get(),CompanySystem.class);
		
		for (int i = 0; i < companySystemList.size(); i++) {
			CompanySystem  companySystem  = companySystemList.get(i);
			Systems system = queryById(companySystem.getSystemId(),Systems.class);
			system.setUseCompany(system.getUseCompany()-1);
			updateObjectById(system);
			deleteObject(companySystem,CompanySystem.class);
		}
		deleteObjectById(id);
	}
	
	
}