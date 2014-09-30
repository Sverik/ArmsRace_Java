package com.po.armsrace.battle.units;

public class UnitType {
	public String name;
	public int cost;

	/** initial health */
	public int maxHealth;
	/** damage per second (per unit), total damage is dps*live.units */
	public int dps;
	/** damage per second against armored units */
	public int armoredDPS;
	public int movementSpeed;
	public int reloadTime;
	public int range100;
	/* -1 if no limit */
	public int maxShots = -1;

	public boolean splashDamage     = false;
	public boolean armored          = false;
	public boolean shootWhileMoving = false;
	public boolean targetsArmored   = false;

}
