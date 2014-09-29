package com.po.armsrace;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.po.armsrace.store.entities.Game;

public class GameLogic {
	public static final long DEFENDER_BUFFER_MS = 20000;
	public static final long START_DELAY_MS = 10000;
	public static final long GAME_DURATION_MS = 4*60*1000;

	static class CountryState {
		public int money;
		public Map<String, Integer> arms;
		public Map<String, Integer> econs;
	}

	public static void setAttack(Game game, boolean attacked, int player) {
		if (game.attacker > 0) {
			return;
		}
		if (attacked) {
			game.attacker = player;
			game.attackTime = System.currentTimeMillis() + DEFENDER_BUFFER_MS;
		}
	}

	public static void setPeace(Game game, boolean peace, int player) {
		boolean canChangePeace = ! game.peaceOffer1 || ! game.peaceOffer2;
		if (canChangePeace) {
			if (player == 1) {
				game.peaceOffer1 = peace;
			} else {
				game.peaceOffer2 = peace;
			}
		}
	}

	/**
	 *
	 * @param game
	 * @param state
	 * @param player
	 * @return true/false whether state was changed
	 * @throws JsonProcessingException
	 */
	public static boolean setState(Game game, GameResource.State state, int player,
			ObjectMapper om) throws JsonProcessingException {
		if (player != 1 && player != 2) {
			throw new RuntimeException("Can't change game state when player is not in the game.");
		}
		if ( ! GameLogic.canChangeState(game, player)) {
			if (game.endTime < System.currentTimeMillis()) {
				// mäng läbi!
				game.finished = true;
				game.winner = 0;
			}
			return false;
		}
		setPeace(game, state.peaceOffer, player);
		setAttack(game, state.attacked, player);

		setCountryState(game, state, player, om);

		return true;
	}

	private static void setCountryState(Game game, GameResource.State state, int player, ObjectMapper om) throws JsonProcessingException {
		CountryState cs = new CountryState();
		cs.arms  = state.arms;
		cs.econs = state.econs;
		cs.money = state.money;
		String countryState = om.writeValueAsString(cs);
		if (player == 1) {
			game.state1 = countryState;
		} else {
			game.state2 = countryState;
		}
	}

	/**
	 * @param game
	 * @param player
	 * @return true/false whether it is valid to change state of the game
	 */
	public static boolean canChangeState(Game game, int player) {
		long time = System.currentTimeMillis();
		if (time > game.endTime)     return false;
		if (time < game.startTime)   return false;
		if (game.attacker == player) return false;
		if (game.attacker > 0 && time > game.attackTime) {
			return false;
		}
		return true;
	}

	public static Game getNewGame() {
		Game game = new Game();
		game.startTime = System.currentTimeMillis() + START_DELAY_MS;
		game.endTime   = game.startTime + GAME_DURATION_MS;
		return game;
	}

}
