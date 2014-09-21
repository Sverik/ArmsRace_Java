package com.po.armsrace.battle.units;

public class UnitType {
	String type;
	
	/** initial health */
	int maxHealth;
	/** damage per second (per unit), total damage is dps*live.units */
	int dps;
	/** damage per second against armored units */
	int armoredDPS;
	int movementSpeed;
	int reloadTime;
	int shootingRadius;
	/* -1 if no limit */
	int maxShots;

	boolean splashDamage;
	boolean armored;
	boolean shootWhileMoving;
	boolean targetsArmored;


}
