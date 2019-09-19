package com.dayee.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface XiaoJianRenDao {

    
    void updateStatusToAudited(@Param("ids")Integer ids[]); 
    List<String> selectStatusByAudited(@Param("ids")Integer ids[]); 
    
}
