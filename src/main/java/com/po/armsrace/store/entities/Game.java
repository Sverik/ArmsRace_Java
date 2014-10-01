package com.po.armsrace.store.entities;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfTrue;
import com.po.armsrace.json.GameJson;

@Entity
@Cache
public class Game {
	public @Id Long id;

	public Ref<GameLog> current;

	public Ref<User> player1;
	public Ref<User> player2;

	public long startTime;
	public long endTime;

	public long attackTime;
	// 0 == no attack, 1 == p1 attacks, 2 == p2 attacks
	public int attacker;

	// 0 == not finished, 1 == p1 won, 2 == p2 won, 3 == draw
	public int winner;
	@Index(IfTrue.class)
	public boolean finished;

	public Ref<Game> replayGame;
	/** Kumma mängija mäng replayGame'ist uuesti mängitakse. */
	public int replayPlayer;

	public GameJson getJson(User user) {
		GameLog gl = current.get();

		GameJson gj = new GameJson();

		gj.yourNumber = whichPlayer(user);

		gj.id = id;
		gj.player1 = player1.get().username;
		gj.player2 = player2.get().username;

		gj.startTime = startTime;
		gj.endTime = endTime;
		gj.currentTime = System.currentTimeMillis();

		gj.attackTime = attackTime;
		gj.attacker = attacker;

		gj.peaceOffer1 = gl.peaceOffer1;
		gj.peaceOffer2 = gl.peaceOffer2;

		gj.state1 = gl.state1;
		gj.state2 = gl.state2;

		gj.winner = winner;
		gj.finished = finished;
		return gj;
	}

	/**
	 * @param user
	 * @return 1 if user is player1
	 *         2 if user is player2
	 *         -1 if neither
	 */
	public int whichPlayer(User user) {
		// players cannot be in first position in games that are replayed
		if (replayGame == null && Key.create(user).compareTo(player1.getKey()) == 0) {
			return 1;
		}
		if (Key.create(user).compareTo(player2.getKey()) == 0) {
			return 2;
		}
		return -1;
	}

	public boolean isInGame(User user) {
		int n = whichPlayer(user);
		return n == 1 || n == 2;
	}

}
