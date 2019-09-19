package com.dayee.dao;

import org.apache.ibatis.annotations.Param;



public interface PaidAccountDao {

    public int getAllPaidAccountRemainingTime(int companySystemId);
    public void updateState(@Param("state")String state,@Param("ids") String[] ids);
}
