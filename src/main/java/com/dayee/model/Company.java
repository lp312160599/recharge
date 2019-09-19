package com.dayee.model;

import java.util.Date;
import java.util.List;

import com.dayee.annotation.NoColumn;

public class Company {

	private Integer id;
	
	private String name;
	
	private String code;
	
	private Date createTime;
	
	private Integer createUser;
	
	private String systemName;
	
	private String consultantEmail;

	@NoColumn
	private List<CompanySystem> system;
	
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public List<CompanySystem> getSystem() {
		return system;
	}

	public void setSystem(List<CompanySystem> system) {
		this.system = system;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

    
    public String getConsultantEmail() {
    
        return consultantEmail;
    }

    
    public void setConsultantEmail(String consultantEmail) {
    
        this.consultantEmail = consultantEmail;
    }
}
