package com.po.armsrace.battle.units;

public class Tank extends UnitType {
	public Tank() {
		this.type = "tank";
		
		this.maxHealth  = 3000;
		this.dps        = 600;
		this.armoredDPS = 1000;

		movementSpeed  = 2;
		reloadTime     = 0;
		range100 = 700;

		armored          = true;
		splashDamage     = false;
		shootWhileMoving = true;
		targetsArmored   = false;
	}
}
