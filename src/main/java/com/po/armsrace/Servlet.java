package com.po.armsrace;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends org.restlet.ext.servlet.ServerServlet {
	private static final long serialVersionUID = 1L;

	public Servlet() {
		System.out.println("DEBUGXYZ 1");
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		super.service(request, response);
	}
/*
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("DEBUGXYZ 3");
	    resp.setHeader("Access-Control-Allow-Origin", "*");
	    resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
	    resp.setHeader("Access-Control-Allow-Headers", "access-control-allow-origin");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("DEBUGXYZ 4");
		resp.setHeader("Access-Control-Allow-Origin", "*");
//		super.doPost(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("DEBUGXYZ 5");
//		super.doGet(req, resp);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Debug-Get", "Yeah");
	}
*/
}
