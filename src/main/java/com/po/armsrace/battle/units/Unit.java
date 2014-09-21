package com.po.armsrace.battle.units;

public class Unit {
	public UnitType type;
	
	public int[] health;
	public int[] loc;
	public int shotsRemaining;
	public int lastMoveTime;
	public int lastShotTime;
	public boolean dead;
	
	public Unit(UnitType type, int n, int[] location) {
		this.type = type;
		this.health = new int[n];
		for (int i = 0; i < n; i++) {
			health[i] = type.maxHealth;
		}
		this.loc = location;
		this.lastMoveTime = -1000;
		this.lastShotTime = -1000;
		this.shotsRemaining = type.maxShots;
		this.dead = false;
	}
	
	public int getTotalHP() {
		int hp = 0;
		for (int i = 0; i < health.length; i++) {
			hp += health[i];
		}
		return hp;
	}
	
	public int numAliveUnits() {
		int alive = 0;
		for (int i = 0; i < health.length; i++) {
			if (health[i] > 0)
				alive += 1;
		}
		return alive;
	}
}
