package com.po.armsrace.battle;

import com.po.armsrace.battle.units.Unit;

public class BattleUtils {
	public static int[] healthAfterShooting(Unit shooter, Unit target) {
		int[] health = new int[target.getNumAliveUnits()];
		for (int i = 0; i < health.length; i++) {
			health[i] = target.health[i];
		}
		int dps = shooter.getTotalDPS(target);
		
		if (shooter.type.splashDamage) {
			// dps is done against each unit
			for (int i = 0; i < health.length; i++) {
				health[i] = (health[i] > dps) ? health[i] - dps : 0;
			}
		} else {
			// dps is for single unit
			for (int i = health.length-1; i >= 0; i--) {
				if (health[i] >= dps) {
					// dps run out on this unit
					health[i] -= dps;
					break;
				} else {
					// unit dies 
					dps -= health[i];
					health[i] = 0;
				}
			}
		}
		
		return health;
	}

	public static int dist100(Unit unit, Unit t) {
		return (int)Math.floor( 100 * Math.sqrt(
				Math.pow(unit.loc[0] - t.loc[0], 2) + 
				Math.pow(unit.loc[1] - t.loc[1], 2)
		));
	}
}
