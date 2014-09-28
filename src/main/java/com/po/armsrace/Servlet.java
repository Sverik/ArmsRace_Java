package com.po.armsrace;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends org.restlet.ext.servlet.ServerServlet {
	private static final long serialVersionUID = 1L;

	public Servlet() {
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
		if ("OPTIONS".equals(request.getMethod())) {
			doOptions(request, response);
		} else {
			super.service(request, response);
		}
	}

}
