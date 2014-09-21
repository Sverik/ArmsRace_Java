package com.po.armsrace.battle.units;

public class TankDestroyer extends UnitType {
	public TankDestroyer() {
		this.type = "tank destroyer";
		
		this.maxHealth  = 3000;
		this.dps        = 100;
		this.armoredDPS = 3000;

		movementSpeed  = 2;
		reloadTime     = 1;
		shootingRadius = 8;

		armored          = true;
		splashDamage     = false;
		shootWhileMoving = false;
		targetsArmored   = true;
	}

}
