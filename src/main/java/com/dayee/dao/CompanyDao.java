package com.dayee.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dayee.model.Company;
import com.dayee.vo.Page;

public interface CompanyDao {

	List<Company> selectCompany(@Param("companyName")String companyName,
			                    @Param("sysName") String sysName,
			                    @Param("orderBy")String orderBy,
			                    @Param("page")Page page);
	
	int getCompanyCount(@Param("companyName")String companyName,
			            @Param("sysName") String sysName);
	
	
}
