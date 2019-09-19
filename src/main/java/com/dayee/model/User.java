package com.dayee.model;

import com.dayee.annotation.NoColumn;

public class User {

    @NoColumn
    private String ip;
    
	private Integer id;
	
	private String name;
	
	private String password;

	private Integer isXiaoJianRen;
	
	private String phone;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getIsXiaoJianRen() {
		return isXiaoJianRen;
	}

	public void setIsXiaoJianRen(Integer isXiaoJianRen) {
		this.isXiaoJianRen = isXiaoJianRen;
	}
    
    public String getPhone() {
    
        return phone;
    }
    
    public void setPhone(String phone) {
    
        this.phone = phone;
    }

    public String getIp() {
    
        return ip;
    }
    
    public void setIp(String ip) {
    
        this.ip = ip;
    }
}