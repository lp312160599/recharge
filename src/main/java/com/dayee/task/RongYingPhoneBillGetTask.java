package com.dayee.task;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dayee.service.PhoneInterFaceServie;

@Component
public class RongYingPhoneBillGetTask {

	@Resource
	private PhoneInterFaceServie phoneInterFaceServie;
	
	public void run() throws Exception{
		phoneInterFaceServie.billGetByRongYing();
	}
}
