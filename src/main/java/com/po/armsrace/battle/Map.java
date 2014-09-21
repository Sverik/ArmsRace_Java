package com.po.armsrace.battle;

import java.util.ArrayList;
import java.util.List;

import com.po.armsrace.battle.events.Event;
import com.po.armsrace.battle.events.MoveEvent;
import com.po.armsrace.battle.events.UnitDiedEvent;
import com.po.armsrace.battle.events.WinEvent;
import com.po.armsrace.battle.units.Unit;

public class Map {
	int[] size;
	int maxTurns;
	int turn;
	
	ArrayList<ArrayList<Unit>> units;
	ArrayList<List<Event>> log = new ArrayList<List<Event>>();
	Integer winner = null;
	
	public Map() {
		units = new ArrayList<>();
		units.add(new ArrayList<Unit>());
		units.add(new ArrayList<Unit>());
		turn = 0;
		maxTurns = 120;
		size = new int[]{20, 10};
	}
	
	public void addUnit(int side, Unit u) {
		units.get(side).add(u);
	}
	
	public ArrayList<List<Event>> getLog() {
		return log;
	}
	
	/**
	 * Main loop
	 * @return winner: 0 or 1. Or null if a draw
	 */
	public Integer doBattle() {
		log = new ArrayList<List<Event>>(); 
		for (int turn = 0; turn < maxTurns; turn++) {
			log.add( doTurn(turn) );
			Integer winner = getWinner();
			if (winner != null) {
				this.winner = winner;
				return winner;
			}
		}
		winner = whoHasMoreHP();
		return winner;
	}
	
	private Integer whoHasMoreHP() {
		int[] hp = new int[]{getTotalHP(0), getTotalHP(1)};
		if (hp[0] > hp[1]) return 1;
		if (hp[0] < hp[1]) return 0;
		return null;
	}

	/**
	 * @param side
	 * @return total HP of all units for the given side
	 */
	private int getTotalHP(int side) {
		int hp = 0;
		for (Unit u : units.get(side)) {
			if (u.dead) continue;
			hp += u.getTotalHP();
		}
		return hp;
	}
	
	/**
	 * @param side
	 * @return true iff side has alive units
	 */
	private boolean isAlive(int side) {
		for (Unit u : units.get(side)) {
			if (u.dead) continue;
			if (u.getTotalHP() > 0)
				return true;
		}
		return false;
	}

	/**
	 * @return null if no winner or 0 or 1, depending on which player won
	 */
	public Integer getWinner() {
		boolean[] alive = new boolean[]{isAlive(0), isAlive(1)};
		if (alive[0] && alive[1]) return null;
		if (alive[0]) return 0;
		return 1;
	}

	/**
	 * main turn logic
	 * @param turn
	 */
	public List<Event> doTurn(int turn) {
		ArrayList<Event> events = new ArrayList<Event>();
		
		// all units of side 0 move right:
		int[] target = new int[]{19, 2};
		for (Unit u : units.get(0)) {
			int[] newLoc = moveToward(u.loc, target);
			events.add(new MoveEvent(u.loc, newLoc));
			u.loc = newLoc;
		}
		
		// all units of side 1 die:
		for (Unit u : units.get(1)) {
			u.dead = true;
			events.add(new UnitDiedEvent(u.loc));
		}
		
		events.add(new WinEvent(0, false));
		
		return events;
	}
	
	/**
	 * @param from
	 * @param destination
	 * @return new location for moving 1 step to toward destination
	 */
	private int[] moveToward(int[] from, int[] toward) {
		int direction[] = new int[]{toward[0] - from[0], toward[1] - from[1]};
		
		int[] to = new int[]{from[0], from[1]};
		if (direction[0] == 0 && direction[1] == 0) {
			return to;
		}
		if (Math.abs(direction[0]) >= Math.abs(direction[1])) {
			// moving along 0 axis
			to[0] += direction[0] > 0 ? 1 : -1;
		} else  {
			to[1] += direction[1] > 0 ? 1 : -1;
		}
		return to;
	}
	
}
