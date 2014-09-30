package com.po.armsrace.battle.units;

public class ChemicalTroops extends UnitType {
	public ChemicalTroops() {
		this.type = "chemical troops";
		cost = 1500;

		this.maxHealth  = 600;
		this.dps        = 10;
		this.armoredDPS = 10;

		movementSpeed  = 1;
		reloadTime     = 0;
		range100       = 400;

		armored          = false;
		splashDamage     = true;
		shootWhileMoving = false;
		targetsArmored   = false;
	}
}
