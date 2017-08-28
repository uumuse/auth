package com.kuke.auth.ssologin.quartz;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.auth.ssologin.quartz.org.ApplicationOrg;

public class InitSSO extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static Logger logger = LoggerFactory.getLogger(InitSSO.class);

	public InitSSO() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void init() throws ServletException {
		//启动初始化机构
		ApplicationOrg.getInstance().reloadInit();
		
	}
}
