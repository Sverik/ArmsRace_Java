package com.po.armsrace;

import java.io.IOException;
import java.util.Map;

import org.restlet.data.Cookie;
import org.restlet.data.Status;
import org.restlet.representation.InputRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.Key;
import com.po.armsrace.store.OS;
import com.po.armsrace.store.entities.User;

public class GameResource extends ServerResource {

	static class State {
		/*
		 * Katsetus, kliendilt saadetud Json
		 * {"money":229,"arms":{"1":2},"econs":{"1":5,"2":1},"peaceOffer":false,"attacked":false}
		 */
		public int money;
		public Map<String, Integer> arms;
		public Map<String, Integer> econs;
		public boolean peaceOffer;
		public boolean attacked;

		@Override
		public String toString() {
			return "State [money=" + money + ", arms=" + arms + ", econs=" + econs + ", peaceOffer=" + peaceOffer + ", attacked=" + attacked + "]";
		}

	}

	ObjectMapper om = new ObjectMapper();

	@Post("json")
	public User game(InputRepresentation input) {
		Cookie secretCookie = getCookies().getFirst("secret");
		if (secretCookie == null) {
			setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		String secret = secretCookie.getValue();
		// fetching user from GAE datastore
		User u = OS.ofy().load().key( Key.create(User.class, secret) ).now();

		if (u == null) {
			setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		setStatus(Status.SUCCESS_OK);

		String text = "";
		try {
			text = input.getText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(text);
		try {
			State s = om.readValue(text, State.class);
			System.out.println(s);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return u;
	}
}
