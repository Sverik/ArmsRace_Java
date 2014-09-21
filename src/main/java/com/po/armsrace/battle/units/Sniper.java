package com.po.armsrace.battle.units;

public class Sniper extends UnitType {
	
	public Sniper() {
		this.type = "sniper";
		
		this.maxHealth  = 400;
		this.dps        = 1000;
		this.armoredDPS = 500;

		movementSpeed  = 1;
		reloadTime     = 2;
		range100       = 1000;

		armored          = false;
		splashDamage     = false;
		shootWhileMoving = false;
		targetsArmored   = false;
	}

}
