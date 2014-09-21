package com.po.armsrace.battle.units;

public class Marine extends UnitType {
	public Marine() {
		this.type = "marine";

		this.maxHealth  = 400;
		this.dps        = 60;
		this.armoredDPS = 30;

		movementSpeed  = 1;
		reloadTime     = 0;
		range100 = 500;

		armored          = false;
		splashDamage     = false;
		shootWhileMoving = false;
		targetsArmored   = true;
	}
}
