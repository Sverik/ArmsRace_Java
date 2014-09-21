package com.po.armsrace.battle.units;

public class TankDestroyer extends UnitType {
	public TankDestroyer() {
		this.type = "tank destroyer";
		
		this.maxHealth  = 2000;
		this.dps        = 100;
		this.armoredDPS = 6000;

		movementSpeed  = 2;
		reloadTime     = 1;
		range100       = 800;

		armored          = true;
		splashDamage     = false;
		shootWhileMoving = false;
		targetsArmored   = true;
	}

}
