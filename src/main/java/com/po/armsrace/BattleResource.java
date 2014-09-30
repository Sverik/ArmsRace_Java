package com.po.armsrace;

import java.io.IOException;
import java.util.Map.Entry;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Work;
import com.po.armsrace.GameLogic.CountryState;
import com.po.armsrace.battle.Battle;
import com.po.armsrace.battle.Map;
import com.po.armsrace.battle.units.Unit;
import com.po.armsrace.battle.units.UnitType;
import com.po.armsrace.store.OS;
import com.po.armsrace.store.entities.Game;
import com.po.armsrace.store.entities.GameLog;
import com.po.armsrace.store.entities.User;

public class BattleResource extends ServerResource {

	ObjectMapper om = new ObjectMapper();

	@Get("json")
	public String battle(Object o) {
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

		GameLog gl = OS.ofy().transact(new Work<GameLog>() {
			@Override
			public GameLog run() {
				Game game = OS.ofy().load()
						.key(Key.create(Game.class, gameId)).now();
				if (game.endTime > System.currentTimeMillis()) {
					return null;
				}
				boolean gameChanged = false;
				// just in case finishing game if not already finished
				if ( ! game.finished) {
					game.finished = true;
					gameChanged = true;
					User p1 = game.player1.get();
					User p2 = game.player2.get();
					// untying users if still connected
					if (p1.activeGame != null && p1.activeGame.compareTo(Ref.create(game)) == 0) {
						p1.activeGame = null;
						OS.ofy().save().entity(p1);
					}
					if (p2.activeGame != null && p2.activeGame.compareTo(Ref.create(game)) == 0) {
						p2.activeGame = null;
						OS.ofy().save().entity(p2);
					}
				}

				GameLog gameLog = OS.ofy().load()
						.key(Key.create(GameLog.class, game.id)).now();
				if (gameLog == null) {
					// battle
					gameChanged = true;
					try {
						CountryState cs1 = GameLogic.jsonToCountryState(game.state1, om);
						CountryState cs2 = GameLogic.jsonToCountryState(game.state2, om);

						Map map = new Map();
						int i = 1;
						for (Entry<String, Integer> unit : cs1.arms.entrySet()) {
							UnitType ut = GameLogic.units.get(unit.getKey());
							if (ut == null) continue;
							Unit u = new Unit(ut, unit.getValue(), new int[]{2, i++} );
							map.addUnit(0, u);
						}
						i = 1;
						for (Entry<String, Integer> unit : cs2.arms.entrySet()) {
							UnitType ut = GameLogic.units.get(unit.getKey());
							if (ut == null) continue;
							Unit u = new Unit(ut, unit.getValue(), new int[]{17, i++} );
							map.addUnit(1, u);
						}
						Battle b = map.doBattle();
						if (b.winner == null) {
							// draw
							game.winner = 3;
						} else {
							// winner is 0 or 1 in Battle (should be fixed)
							game.winner = b.winner + 1;
						}
						String battleString = om.writeValueAsString(b);
						gameLog = new GameLog();
						gameLog.id = game.id;
						gameLog.log = battleString;

						OS.ofy().save().entity(gameLog);

					} catch (JsonParseException | JsonMappingException e) {
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}

				if (gameChanged) {
					OS.ofy().save().entity(game);
				}
				return gameLog;
			}
		});

		/*
		Map map = new Map();
		//map.addUnit(0, new Unit(new Tank(), 10, new int[]{2, 8}) );
		map.addUnit(0, new Unit(new Marine(), 50, new int[]{4, 5}) );

		map.addUnit(1, new Unit(new Sniper(), 10, new int[]{17, 2}) );
		//map.addUnit(1, new Unit(new TankDestroyer(), 10, new int[]{17, 3}) );
		//map.addUnit(1, new Unit(new ChemicalTroops(), 10, new int[]{17, 3}) );
		//map.addUnit(1, new Unit(new Tank(), 20, new int[]{17, 2}) );
		return map.doBattle();
		*/
		if (gl != null) {
			return gl.log;
		}
		return null;
	}

}
