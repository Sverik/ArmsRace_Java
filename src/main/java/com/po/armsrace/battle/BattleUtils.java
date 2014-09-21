package com.po.armsrace.battle;

import java.util.ArrayList;

import com.po.armsrace.battle.units.Health;
import com.po.armsrace.battle.units.Unit;

public class BattleUtils {
	public static Health healthAfterShooting(Unit shooter, Unit target) {
		Health health = target.health.copy();
		int dps = shooter.getTotalDPS(target);
		
		if (shooter.type.splashDamage) {
			// dps is done against each unit
			health.unitHP -= dps;
			health.weakestHP -= dps;
			if (health.unitHP <= 0) {
				health.setDead();
			} else if (health.weakestHP <= 0) {
				health.numUnits -= 1;
				health.weakestHP = health.unitHP;
			}
		} else {
			// dps is for single unit
			int hpLeft = target.getTotalHP() - dps;
			if (hpLeft <= 0) {
				health.setDead();
			} else {
				health.numUnits  = hpLeft / health.unitHP + 1;
				health.weakestHP = hpLeft % health.unitHP;
				if (health.weakestHP == 0) {
					health.numUnits -= 1;
					health.weakestHP = health.unitHP;
				}
			}
		}
		
		return health;
	}

	public static int dist100(Unit u1, Unit u2) {
		return (int)Math.floor( 100 * Math.sqrt(
				Math.pow(u1.loc[0] - u2.loc[0], 2) + 
				Math.pow(u1.loc[1] - u2.loc[1], 2)
		));
	}
	
	/**
	 * @param from
	 * @param destination
	 * @param forward 1 if forward, -1 then backward (away)
	 * @return new location for moving 1 step to toward destination
	 */
	public static int[] moveToward(int[] from, int[] destination, int forward) {
		if (destination == null) {
			return null;
		}
		int direction[] = new int[]{destination[0] - from[0], destination[1] - from[1]};
		
		int[] to = new int[]{from[0], from[1]};
		if (direction[0] == 0 && direction[1] == 0) {
			return to;
		}
		if (Math.abs(direction[0]) >= Math.abs(direction[1])) {
			// moving along 0 axis
			to[0] += direction[0] > 0 ? 1*forward : -1*forward;
		} else  {
			to[1] += direction[1] > 0 ? 1*forward : -1*forward;
		}
		return to;
	}

	public static boolean isOccupied(ArrayList<ArrayList<Unit>> units, int[] loc) {
		for (ArrayList<Unit> side : units) {
			for (Unit unit : side) {
				if ( ! unit.isDead() && 
						unit.loc[0] == loc[0] && 
						unit.loc[1] == loc[1]) {
					return true;
				}
			}
		}
		return false;
	}
}
