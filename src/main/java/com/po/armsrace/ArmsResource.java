package com.po.armsrace;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ArmsResource extends ServerResource {

	@Get
	public String represent() {
		return "Arms Racer REST service is here!";
	}
}
