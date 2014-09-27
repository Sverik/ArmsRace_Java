package com.po.armsrace;

import org.restlet.data.CookieSetting;
import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.po.armsrace.store.OS;
import com.po.armsrace.store.SecretGenerator;
import com.po.armsrace.store.entities.User;

public class RegisterResource extends ServerResource {
	
	/**
	 * @param o
	 * @return registers a new user
	 */
	@Post("json")
	public User register(Object o) {
		String username = getAttribute("username");
		if (username.length() <= 0) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return null;
		}
		
		User u = new User();
		u.username = username;
		u.secret = SecretGenerator.nextSessionId();
		
		OS.ofy().save().entity(u);
		
		CookieSetting cs = new CookieSetting("secret", u.secret);
		this.getResponse().getCookieSettings().add(cs);
		// cookie age is 1000 days
		cs.setMaxAge(1000*24*3600);
		cs.setPath("/");
		setStatus(Status.SUCCESS_OK);
		return u;
	}
}
