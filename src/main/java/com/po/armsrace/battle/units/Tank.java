package com.po.armsrace.battle.units;

public class Tank extends UnitType {
	public Tank() {
		this.type = "tank";
		
		this.maxHealth  = 3000;
		this.dps        = 300;
		this.armoredDPS = 500;

		movementSpeed  = 2;
		reloadTime     = 0;
		shootingRadius = 7;

		armored          = true;
		splashDamage     = false;
		shootWhileMoving = true;
		targetsArmored   = false;
	}
}
