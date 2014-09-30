package com.po.armsrace;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class DefaultResource extends ServerResource {

	@Get("html")
	public String res() {
		return "<html><head><title>Arms Race</title></head><body></body></html>";
	}

}
