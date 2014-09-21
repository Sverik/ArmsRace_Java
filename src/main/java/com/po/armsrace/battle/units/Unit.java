package com.po.armsrace.battle.units;

import java.util.ArrayList;

import com.po.armsrace.battle.BattleUtils;

public class Unit {
	public UnitType type;
	
	public int[] health;
	public int[] loc;
	public int shotsRemaining;
	public int lastNumMoves;
	public int lastMoveTime;
	public int lastShotTime;
	public boolean dead;
	public int n;
	
	public Unit(UnitType type) {
		this.type = type;
	}
	
	public Unit(UnitType type, int n, int[] location) {
		this.type = type;
		this.n    = n;
		this.health = new int[n];
		for (int i = 0; i < n; i++) {
			health[i] = type.maxHealth;
		}
		this.loc = location;
		this.lastNumMoves = 0;
		this.lastMoveTime = -1000;
		this.lastShotTime = -1000;
		this.shotsRemaining = type.maxShots;
		this.dead = false;
	}
	
	public Unit copy() {
		Unit u = new Unit(this.type);
		u.health = this.health;
		u.loc = this.loc;
		u.shotsRemaining = shotsRemaining;
		u.lastMoveTime = lastMoveTime;
		u.lastShotTime = lastShotTime;
		u.dead = dead;
		u.n    = n;
		return u;
	}
	
	public int getTotalHP() {
		int hp = 0;
		for (int i = 0; i < health.length; i++) {
			hp += health[i];
		}
		return hp;
	}
	
	public int getNumAliveUnits() {
		int alive = 0;
		for (int i = 0; i < health.length; i++) {
			if (health[i] > 0)
				alive += 1;
		}
		return alive;
	}
	
	public int getTotalDPS(Unit target) {
		int alive = getNumAliveUnits();
		int dps = target.type.armored ? type.armoredDPS : type.dps;
		return dps * alive;
	}
	
	/**
	 * @param targets
	 * @return preferred target to shoot or null if no target in range
	 */
	public Unit getTarget(ArrayList<Unit> targets) {
		Unit target = null;
		for (Unit t : targets) {
			if (t.dead) continue;
			int dist100 = BattleUtils.dist100(this, t);
			if (dist100 > this.type.range100) {
				continue;
			}
			if (target == null) {
				target = t;
				continue;
			}
			target = preferredTarget(target, t);
		}
		return target;
	}
	
	public Unit preferredTarget(Unit u1, Unit u2) {
		if (u1.type.armored == u2.type.armored) {
			if (BattleUtils.dist100(this, u1) < BattleUtils.dist100(this, u2)) {
				return u1;
			} else {
				return u2;
			}
		}
		if (this.type.targetsArmored) {
			if (u1.type.armored) return u1;
			else  				 return u2;
		} else {
			if (u1.type.armored) return u2;
			else  				 return u1;
		}
	}

	public boolean canShoot(int turn) {
		if (turn - lastShotTime <= type.reloadTime) return false;
		if (lastMoveTime == turn && ! type.shootWhileMoving) return false;
		
		return true;
	}
}
