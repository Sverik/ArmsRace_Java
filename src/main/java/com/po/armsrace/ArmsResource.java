package com.po.armsrace;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ArmsResource extends ServerResource {

	@Get
	public String represent(Object o) {
		o = getAttribute("armId");
		return "Arms Racer REST service is here! [" + (o != null ? o.getClass().getCanonicalName() + ":" + o.toString() : null) + "]";
	}
}
