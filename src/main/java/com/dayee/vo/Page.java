package com.dayee.vo;

public class Page {

	private Integer pageNumber = 1;
	
	private Integer pageSize   = 10 ;
	
	private Integer count      = 0;
	
	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageCountNumber() {
		int page = count/pageSize;
		page +=(count%pageSize==0?0:1);
		return page;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getStart() {
		return (pageNumber-1)*pageSize;
	}

	public Integer getEnd() {
		return pageNumber*pageSize;
	}

}
