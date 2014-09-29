package com.po.armsrace;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
		String username = decode(getAttribute("username"));
		if (username.length() <= 0) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return null;
		}

		User u = new User();
		u.username = username;
		u.secret = SecretGenerator.nextSessionId();

		OS.ofy().save().entity(u);

		CookieSetting cs = new CookieSetting("secret", u.secret);
		// cookie age is 1000 days
		cs.setMaxAge(1000*24*3600);
		cs.setPath("/");
		this.getResponse().getCookieSettings().add(cs);
		setStatus(Status.SUCCESS_OK);
		return u;
	}

    static String decode(Object value) {
        if (value == null) {
            return null;
        }

        try {
            return URLDecoder.decode(value.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
