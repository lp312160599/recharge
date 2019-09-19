package com.dayee.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dayee.dao.RechargeAuditDao;
import com.dayee.model.RechargeAudit;

@Service
public class RechargeAuditService extends BaseService<RechargeAudit> {

	@Resource
	private RechargeAuditDao rechargeAuditDao;
	
	public List<RechargeAudit>  queryCommonlyUsed(){
		return rechargeAuditDao.queryCommonlyUsed();
	}
}
