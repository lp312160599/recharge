package com.dayee.model;

import java.util.Date;

public class PrePaid {

	public static String STATUS_LOCKING = "锁定";
	public static String STATUS_UNLOCKING = "解锁";
	
	private Integer id;
	
	private Double money;
	
	private Date addDate;
	
	private String uniqueCode;
	
	private String type;
	
	private double balance;
	

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
