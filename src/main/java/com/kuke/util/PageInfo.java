package com.kuke.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PageInfo {

	/**
	 * 当前页
	 */
	private int currentPage = 1;

	/**
	 * 每页数量
	 */
	private int pageSize = 10;
	
	/**
	 * 总共几页
	 */
	private int pageCount;
	/**
	 * 数据总个数
	 */
	private int resultCount;
	
	/**
	 * 数据
	 */
	private List<Object> resultList;
	
	
	private List<String> pageList;
	
	
	/**
	 * 服务对象
	 */
	private Object serviceObj;
	/**
	 * 服务方法名
	 */
	private String serviceMethodName;
	/**
	 * 服务参数
	 */
	private Map<String, String> paramMap;
	
	
	public PageInfo() {
		this.toPagination();
	}
	
	public PageInfo(int currentPage, int pageSize, Object serviceObj,
			String serviceMethodName, Map<String, String> paramMap) {
		this.setCurrentPage(currentPage);
		this.setPageSize(pageSize);
		
		this.serviceObj = serviceObj;
		this.serviceMethodName = serviceMethodName;
		this.paramMap = paramMap;
		
		this.setResultList(this.getOffset(), this.pageSize);
		//为空时用默认查询size，避免数据量过大造成查询时间缓慢
		if(paramMap.get("flag")==null){
			this.setResultCount();
		}
		this.toPagination();
		this.toSelectPagination();
	}
	
	private List<String> selectPageList;
	
	public List<String> getSelectPageList() {
		return selectPageList;
	}

	public void setSelectPageList(List<String> selectPageList) {
		this.selectPageList = selectPageList;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public int getResultCount() {
		return resultCount;
	}
	
	public List<Object> getResultList() {
		return resultList;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	private void setResultList(int offset, int rows) {
		this.resultList = this.queryData(offset, rows);
	}
	
	public void setResultCount() {
		this.resultCount = this.queryData(0, 0).size();
	}
	
	private int getOffset() {
		return (this.currentPage - 1) * this.pageSize;
	}
	
	public int getPageCount() {
		try {
			return (this.resultCount + this.pageSize - 1) / this.pageSize;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

	public void setResultList(List<Object> resultList) {
		this.resultList = resultList;
	}
	

	public List<String> getPageList() {
		return pageList;
	}

	public void setPageList(List<String> pageList) {
		this.pageList = pageList;
	}

	/**
	 * 处理数据
	 * @param offset
	 * @param rows
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Object> queryData(int offset, int rows) {
		List<Object> queryList = new ArrayList<Object>();
		try {
			Method m = this.serviceObj.getClass().getMethod(
					this.serviceMethodName, Map.class, int.class, int.class);
			queryList = (List<Object>) m.invoke(this.serviceObj, paramMap, offset,rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryList;
	}
	
	public void toPagination() {
		pageList = new ArrayList<String>();
		int start_space=3;
		int end_space=3;
		int start_page=1;
		int end_page=this.getPageCount();
		boolean flag_a=false;
		boolean flag_b=false;
		if(this.getPageCount()>1){
		//开始页码
		if(this.getCurrentPage()-start_space>1){
			start_page=this.getCurrentPage()-start_space;
			flag_a=true;
		}else{
			start_page=1;
		}
		if(flag_a){
			pageList.add(String.valueOf("1"));
			pageList.add(String.valueOf("..."));
		}
		/**
		 * 
		 */
		for (int i = start_page; i < this.getCurrentPage(); i++) {
			pageList.add(String.valueOf(i));
		}
		
		//结束码
		if(this.getPageCount()-this.getCurrentPage()>3){
			end_page=this.getCurrentPage()+end_space;
			flag_b=true;
		}else{
			end_page=this.getCurrentPage()+(this.getPageCount()-this.getCurrentPage());
		}
		for (int i = this.getCurrentPage(); i <= end_page; i++) {
			pageList.add(String.valueOf(i));
		}
		
		if(flag_b){
			pageList.add(String.valueOf("..."));
			pageList.add(String.valueOf(this.getPageCount()));
		}
		
		}
	}
	public void toSelectPagination() {
		selectPageList = new ArrayList<String>();
		int start_page=1;
		int end_page=this.getPageCount();
		for (int i = start_page; i <= end_page; i++) {
			selectPageList.add(String.valueOf(i));
		}
		
	}
}
