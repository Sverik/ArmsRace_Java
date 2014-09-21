package com.po.armsrace.battle;

import java.util.ArrayList;
import java.util.List;

import com.po.armsrace.battle.events.Event;
import com.po.armsrace.battle.events.MoveEvent;
import com.po.armsrace.battle.events.ShootEvent;
import com.po.armsrace.battle.events.UnitDiedEvent;
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
	public Battle doBattle() {
		Battle battle = new Battle();
		battle.initialUnits = units;
		
		log = new ArrayList<List<Event>>();
		this.winner = null;
		for (turn = 0; turn < maxTurns; turn++) {
			log.add( doTurn() );
			Integer winner = getWinner();
			if (winner != null) {
				this.winner = winner;
				break;
			}
		}
		if (winner == null) {
			winner = whoHasMoreHP();
		}
		battle.log    = log;
		battle.winner = winner;
		battle.draw   = winner == null;

		return battle;
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
			if (u.isDead()) continue;
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
			if ( ! u.isDead()) {
				return true;
			}
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
	public List<Event> doTurn() {
		ArrayList<Event> events = new ArrayList<Event>();
		
		// step 1: acquire targets and shoot
		Update shooting = shoot(units);
		events.addAll(shooting.events);
		units = shooting.units;
		
		// step 2: move units
		Update moving = move(units);
		events.addAll(moving.events);
		units = moving.units;
		
		return events;
	}

	private Update move(ArrayList<ArrayList<Unit>> units) {
		Update update = new Update();
		update.units = copyUnits(units);
		update.events = new ArrayList<Event>();
		
		for (int side = 0; side < 2; side++) {
			int oppSide = (side == 0) ? 1 : 0;
			for (Unit mover : update.units.get(side)) {
				if ( ! mover.canMove(turn)) continue;
				int[] newLoc = mover.getNewLocation(units.get(oppSide), turn);
				if (newLoc == null || BattleUtils.isOccupied(update.units, newLoc)) {
					continue;
				}
				// moving there:
				update.events.add(new MoveEvent(mover.loc, newLoc));
				mover.loc = newLoc;
				if (mover.lastMoveTime == turn) {
					mover.lastNumMoves += 1;
				} else {
					mover.lastMoveTime = turn;
					mover.lastNumMoves = 1;
				}
			}
		}
		return update;
	}

	/**
	 * Acquires targets and shoots. Does not change units but returns new version.
	 * @return
	 */
	private Update shoot(ArrayList<ArrayList<Unit>> units) {
		Update update = new Update();
		update.units = copyUnits(units);
		update.events = new ArrayList<Event>();
		for (int side = 0; side < 2; side++) {
			// shooting only opposing side:
			int oppSide = (side == 0) ? 1 : 0;
			
			for (Unit shooter : units.get(side)) {
				if ( ! shooter.canShoot(turn)) { continue; }
				Unit target = shooter.getTarget(update.units.get(oppSide));
				if (target != null) {
					// shooting logic
					target.health = BattleUtils.healthAfterShooting(shooter, target);
					shooter.lastShotTime = turn;
					if (shooter.shotsRemaining > 0) {
						shooter.shotsRemaining -= 1;
					}
					update.events.add(new ShootEvent(shooter.loc, target.loc, target.health));
					if (target.isDead()) {
						update.events.add(new UnitDiedEvent(target.loc));
					}
				}
			}
		}
		return update;
	}
	
	private static ArrayList<ArrayList<Unit>> copyUnits(ArrayList<ArrayList<Unit>> units) {
		ArrayList<ArrayList<Unit>> copy = new ArrayList<>();
		for (ArrayList<Unit> side : units) {
			ArrayList<Unit> newSide = new ArrayList<>();
			for (Unit u : side) {
				newSide.add( u.copy() );
			}
			copy.add(newSide);
		}
		return copy;
	}
	
}
