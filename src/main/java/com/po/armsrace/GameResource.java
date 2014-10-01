package com.po.armsrace;

import java.io.IOException;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.representation.InputRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Work;
import com.po.armsrace.json.GameJson;
import com.po.armsrace.store.OS;
import com.po.armsrace.store.entities.Game;
import com.po.armsrace.store.entities.GameLog;
import com.po.armsrace.store.entities.User;

public class GameResource extends ServerResource {

	public static class State {
		public int money;
		public Map<String, Integer> arms;
		public Map<String, Integer> econs;
		public boolean peaceOffer;
		public boolean attacked;

		@Override
		public String toString() {
			return "State [money=" + money + ", arms=" + arms + ", econs="
					+ econs + ", peaceOffer=" + peaceOffer + ", attacked="
					+ attacked + "]";
		}

	}

	ObjectMapper om = new ObjectMapper();

	@Post("json")
	public GameJson game(InputRepresentation input) {
		final User u = PlayerResource.getUser(this);
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
		final long gameId = Long.parseLong(gameIdStr);

		setStatus(Status.SUCCESS_OK);

		String text = "";
		try {
			text = input.getText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(text);
		Game g = null;
		try {
			final State s = om.readValue(text, State.class);
			// updating game state
			g = OS.ofy().transact(new Work<Game>() {
				@Override
				public Game run() {
					Game game = OS.ofy().load()
							.key(Key.create(Game.class, gameId)).now();
					if (game == null || !game.isInGame(u)) {
						setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
						return null;
					}
					GameLog gl = game.current.get().clone();
					OS.ofy().save().entities(gl).now();
					game.current = Ref.create(gl);
					gl.time = System.currentTimeMillis();
					try {
						if (game.replayGame != null) {
							replayOpponent(game, om);
						}
						GameLogic.setState(game, s, game.whichPlayer(u), om);
						if (game.finished) {
							// untie users from the game
							User p1 = game.player1.get();
							User p2 = game.player2.get();
							if (p1.activeGame != null && p1.activeGame.compareTo(Ref.create(game)) == 0) {
								p1.activeGame = null;
								OS.ofy().save().entities(p1);
							}
							if (p2.activeGame != null && p2.activeGame.compareTo(Ref.create(game)) == 0) {
								p2.activeGame = null;
								OS.ofy().save().entities(p2);
							}
						}
					} catch (JsonProcessingException e) {
						setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
						e.printStackTrace();
						return null;
					}
					OS.ofy().save().entities(game, gl).now();
					return game;
				}
			});
			System.out.println(s);
		} catch (JsonParseException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			e.printStackTrace();
			return null;
		} catch (JsonMappingException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			e.printStackTrace();
			return null;
		}
		return g.getJson(u);
	}

	private void replayOpponent(Game game, ObjectMapper om) throws JsonParseException, JsonMappingException, IOException {
		Game replayGame = game.replayGame.get();
		long timeInReplayGame = replayGame.startTime + (game.current.get().time - game.startTime);
		GameLog gl = OS.ofy().load().type( GameLog.class ).ancestor(game.replayGame).filter("time <=", timeInReplayGame).order("-time").first().now();
		if (gl == null) {
			return;
		}
		// Replay players are always player1
		if (gl.state1 != null && ! gl.state1.trim().isEmpty()) {
			State opponentState = om.readValue(gl.state1, State.class);
			if (replayGame.attacker == 1 && replayGame.attackTime >= timeInReplayGame) {
				opponentState.attacked = true;
			}
			GameLogic.setState(game, opponentState, 1, om);
		}
	}

}
