package com.po.armsrace.battle.units;

public class Marine extends UnitType {
	public Marine() {
		this.type = "marine";

		this.maxHealth  = 300;
		this.dps        = 30;
		this.armoredDPS = 10;

		movementSpeed  = 1;
		reloadTime     = 0;
		shootingRadius = 5;
		maxShots       = -1;

		armored          = false;
		splashDamage     = false;
		shootWhileMoving = false;
		targetsArmored   = true;
	}
}
