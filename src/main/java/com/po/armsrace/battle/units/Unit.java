package com.po.armsrace.battle.units;

import java.util.ArrayList;

import com.po.armsrace.battle.BattleUtils;

public class Unit {
	public UnitType type;
	
	public Health health;
	public int[] loc;
	public int shotsRemaining;
	public int lastNumMoves;
	public int lastMoveTime;
	public int lastShotTime;
	public int n;
	
	public Unit(UnitType type) {
		this.type = type;
	}
	
	public Unit(UnitType type, int n, int[] location) {
		this.type = type;
		this.n      = n;
		this.health = new Health(n, type.maxHealth, type.maxHealth);
		this.loc    = location;
		this.lastNumMoves = 0;
		this.lastMoveTime = -1000;
		this.lastShotTime = -1000;
		this.shotsRemaining = type.maxShots;
	}
	
	public Unit copy() {
		Unit u = new Unit(this.type);
		u.n      = n;
		u.health = this.health;
		u.loc    = this.loc;
		u.lastNumMoves = lastNumMoves;
		u.lastMoveTime = lastMoveTime;
		u.lastShotTime = lastShotTime;
		u.shotsRemaining = shotsRemaining;
		return u;
	}
	
	public int getTotalHP() {
		return (health.numUnits - 1) * health.unitHP + health.weakestHP;
	}
	
	public int getNumAliveUnits() {
		return health.numUnits;
	}
	
	public boolean isDead() {
		return health.numUnits <= 0;
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
			if (t.isDead()) continue;
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
		if (shotsRemaining == 0) return false;
		if (turn - lastShotTime <= type.reloadTime) return false;
		if (lastMoveTime == turn && ! type.shootWhileMoving) return false;
		
		return true;
	}
	
	public int getNumMovesLeft(int turn) {
		if (lastMoveTime != turn) return type.movementSpeed;
		return type.movementSpeed - lastNumMoves;
	}
	
	public boolean canMove(int turn) {
		if (getNumMovesLeft(turn) == 0) {
			return false;
		}
		if (type.shootWhileMoving) {
			return true;
		} else {
			// only move when unit is not reloading (or shot)
			return turn - lastShotTime > type.reloadTime;
		}
	}

	public int[] getNewLocation(ArrayList<Unit> targets, int turn) {
		if (lastShotTime == turn) {
			// just shot this turn, now moving away from target:
			Unit target = this.getClosestPreferredTarget(targets);
			if (target == null)
				return null;
			if (BattleUtils.dist100(this, target) < this.type.range100) {
				// moving away
				return BattleUtils.moveToward(loc, target.loc, -1);
			}
			// moving closer
			return BattleUtils.moveToward(loc, target.loc, 1);
		}
		// moving closer to target:
		Unit target = this.getClosestPreferredTarget(targets);
		if (target == null)
			return null;
		return BattleUtils.moveToward(loc, target.loc, 1);
	}
	
	/**
	 * @param targets
	 * @return closest target. Chooses target that is preferred
	 */
	public Unit getClosestPreferredTarget(ArrayList<Unit> targets) {
		Unit target = null;
		for (Unit u : targets) {
			if (u.isDead()) continue;
			if (target == null) {
				target = u;
				continue;
			}
			target = preferredTarget(target, u);
		}
		return target;
	}
}
