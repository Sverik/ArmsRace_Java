package com.po.armsrace;

import org.restlet.data.Cookie;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.Key;
import com.po.armsrace.store.OS;
import com.po.armsrace.store.entities.User;

public class PlayerResource extends ServerResource {
	
	/**
	 * Checks users cookies, if secret is fine, returns user info.
	 * If not you get HTTP unauthorized error (401).
	 * @param o
	 * @return
	 */
	@Get("json")
	public User checkPlayer(Object o) {
		User u = getUser(this);
		
		if (u == null) {
			setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		setStatus(Status.SUCCESS_OK);
		return u;
	}
	
	public static User getUser(ServerResource resource) {
		Cookie secretCookie = resource.getCookies().getFirst("secret");
		if (secretCookie == null) {
			return null;
		}
		String secret = secretCookie.getValue();
		// fetching user from GAE datastore
		User u = OS.ofy().load().key( Key.create(User.class, secret) ).now();
		return u;
	}
}
