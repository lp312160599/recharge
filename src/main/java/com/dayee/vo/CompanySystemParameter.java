package com.dayee.vo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dayee.model.CompanySystem;
import com.dayee.utils.ObjectUtil;
import com.dayee.utils.StringUtils;

public class CompanySystemParameter {

	private  KuaiYongYunVo kuaiYongYunVo;
	
	private  BackgroundVo backgroundVo;
	
	private  XiaoJianRenVo xiaoJianRenVo;
	
	private  YiLuVo yiLuVo;
	
	private  RongYingVo rongYingVo;
	
	private  DayeeVideo dayeeVideo;
	

	public List<CompanySystem> getCompanySystemList(){
		
		@SuppressWarnings("serial")
		List<CompanySystem> list =  new ArrayList<CompanySystem>(){};
		
		Field[] field = getClass().getDeclaredFields();
		
		for (int i = 0; i < field.length; i++) {
			try {
				String methodName= "get"+StringUtils.fistLetterToUpperCase(field[i].getName());
				Method method =  getClass().getMethod(methodName, new Class[]{});
				Object value = method.invoke(this,new Object[]{});
				if(value!=null){
					CompanySystem companySystem = new CompanySystem();
					if(value instanceof CompanySystem) {
					    ObjectUtil.merge(CompanySystem.class,value, companySystem);
					}else{
					    ObjectUtil.merge(value, companySystem);
					}
					list.add(companySystem);
				}
			} catch (Exception e) {
				continue;
			}
		}
		
		return list ;
	}
	
	public KuaiYongYunVo getKuaiYongYunVo() {
		return kuaiYongYunVo;
	}

	public void setKuaiYongYunVo(KuaiYongYunVo kuaiYongYunVo) {
		this.kuaiYongYunVo = kuaiYongYunVo;
	}

	public BackgroundVo getBackgroundVo() {
		return backgroundVo;
	}

	public void setBackgroundVo(BackgroundVo backgroundVo) {
		this.backgroundVo = backgroundVo;
	}

	public XiaoJianRenVo getXiaoJianRenVo() {
		return xiaoJianRenVo;
	}

	public void setXiaoJianRenVo(XiaoJianRenVo xiaoJianRenVo) {
		this.xiaoJianRenVo = xiaoJianRenVo;
	}

	public YiLuVo getYiLuVo() {
		return yiLuVo;
	}

	public void setYiLuVo(YiLuVo yiLuVo) {
		this.yiLuVo = yiLuVo;
	}

	public RongYingVo getRongYingVo() {
		return rongYingVo;
	}

	public void setRongYingVo(RongYingVo rongYingVo) {
		this.rongYingVo = rongYingVo;
	}
    
    public DayeeVideo getDayeeVideo() {
    
        return dayeeVideo;
    }
    
    public void setDayeeVideo(DayeeVideo dayeeVideo) {
    
        this.dayeeVideo = dayeeVideo;
    }	
	
}
