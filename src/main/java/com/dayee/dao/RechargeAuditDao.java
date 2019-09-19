package com.dayee.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dayee.model.RechargeAudit;
import com.dayee.vo.RechargeFreezeInfo;

public interface RechargeAuditDao {

	public List<RechargeAudit>  queryCommonlyUsed();
	public RechargeFreezeInfo  sumRechargeUnderReview(@Param("systemId")Integer systemId);
}
