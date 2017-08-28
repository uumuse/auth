package com.kuke.common.utils;

import net.sf.json.JSONObject;

import com.kuke.auth.util.KuKeAuthConstants;


public class ResponseMsg {
	
	private boolean flag;
	
	private String code;

	private String msg;
	
	private String codeDesc;

	private Object data;

	public ResponseMsg() {
		super();
	}
	
	public ResponseMsg(String res) {
		try {
			if(res != null && !"".equals(res.trim())){
				JSONObject json = JSONObject.fromObject(res);
				flag =json.getBoolean("flag");
				code = json.getString("code");
				msg = json.getString("msg");
				codeDesc = json.getString("codeDesc");
				data = json.get("data");
			}
		} catch (Exception e) {
			flag = false;
		}
	}
	
	public ResponseMsg(boolean flag, String msg) {
		super();
		this.flag = flag;
		this.msg = msg;
	}

	public ResponseMsg(boolean flag, String code, String msg, String codeDesc) {
		super();
		this.flag = flag;
		this.code = code;
		this.msg = msg;
		this.codeDesc = codeDesc;
	}
	
	public ResponseMsg(boolean flag,String msg, Object data) {
		super();
		this.flag = flag;
		this.msg = msg;
		this.data = data;
	}

	public ResponseMsg(boolean flag, String code, String msg, String codeDesc, Object data) {
		super();
		this.flag = flag;
		this.code = code;
		this.msg = msg;
		this.codeDesc = codeDesc;
		this.data = data;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public void setResult(boolean flag, String msg){
		setFlag(flag);
		setMsg(msg);
	}
	
	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}

	public void setResult(boolean flag, String msg, Object data){
		setFlag(flag);
		setMsg(msg);
		setData(data);
	}

	@Override
	public String toString() {
		return "{\"flag\"=" + flag + ", \"code=\"" + code + ", \"msg=\"" + msg
				+ ", \"codeDesc=\"" + codeDesc + ", \"data=\"" + data + "}";
	}
}
