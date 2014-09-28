package com.po.armsrace;

import java.io.IOException;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.representation.InputRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.Key;
import com.po.armsrace.json.GameJson;
import com.po.armsrace.store.OS;
import com.po.armsrace.store.entities.Game;
import com.po.armsrace.store.entities.User;

public class GameResource extends ServerResource {

	public static class State {
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
	public GameJson game(InputRepresentation input) {
		User u = PlayerResource.getUser(this);
		if (u == null) {
			setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		
		// checking game exists & user is in game
		String gameIdStr = getAttribute("gameId");
		if (gameIdStr == null) {
			setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		long gameId = Long.parseLong(gameIdStr);
		Game game = OS.ofy().load().key( Key.create(Game.class, gameId) ).now();
		if (game == null || ! game.isInGame(u)) {
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
			// updating game state
			GameLogic.setState(game, s, game.whichPlayer(u), om);
			System.out.println(s);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return game.getJson(u);
	}

}
