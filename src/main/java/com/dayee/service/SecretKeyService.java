package com.dayee.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dayee.dao.SecreKeyDao;
import com.dayee.model.CompanySystem;

@Component
public class SecretKeyService extends BaseService<Object>{

	@Resource
	private SecreKeyDao secreKeyDao;
	
	@Transactional(rollbackFor=Exception.class)
	public void setsetSecurityCode(String secretKey,List<String> codes,CompanySystem companySystem) throws Exception{
		secreKeyDao.updateSecurityCodeByNotUse(companySystem.getId());
        for (int i = 0; i < codes.size(); i++) {
        	secreKeyDao.updateSecurityCodeByUse(codes.get(i));
		}
        companySystem.setUseSecurityCode(codes.size());
        updateObjectById(companySystem);
	}
}
