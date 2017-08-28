
package com.kuke.auth.userCenter.dto;

/**
 * 
 *<pre>
 * 概　述:
 *  	定义异步js调用返回的结果
 * @author zhjt
 * @version 最后修改日期 2009-9-17
 * @see 需要参见的其它类
 *</pre>
 */
public class Result {

	/**
	 * 返回结果,error、waring、success、failed
	 */
	private String result;
	
	/**
	 * 返回信息
	 */
	private String message;
	
	/**
	 * 执行结果后的Url
	 */
	private String redirecturl;

	public Result(){
		
	}
	public Result(String result,String message,String redirecturl){
		this.setMessage(message);
		this.setResult(result);
		this.setRedirecturl(redirecturl);
	}
	
	public Result(String result,String message){
		this.setResult(result);	
		this.setMessage(message);
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	public String getRedirecturl() {
		return redirecturl;
	}
	public void setRedirecturl(String redirecturl) {
		this.redirecturl = redirecturl;
	}
	
	
	
	
	
}
