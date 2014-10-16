package com.po.armsrace;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class DefaultResource extends ServerResource {

	@Get("html")
	public String res() {
		return "<html><head><META http-equiv=\"refresh\" content=\"0;URL=/static/armsrace_dart.html\"><title>Arms Race</title></head><body></body></html>";
	}

}
