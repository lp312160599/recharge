package com.dayee.dao;

public interface SecreKeyDao {

    public void updateSecurityCodeByNotUse(int id);
	
	public void updateSecurityCodeByUse(String code);
	
}
