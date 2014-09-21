package com.po.armsrace.battle.units;

public class Health {
	public int numUnits;
	public int unitHP;
	public int weakestHP;
	
	public Health() {}
	public Health(int numUnits, int unitHP, int weakestHP) {
		this.numUnits  = numUnits;
		this.unitHP    = unitHP;
		this.weakestHP = weakestHP;
	}
	
	public Health copy() {
		return new Health(numUnits, unitHP, weakestHP);
	}
	
	public void setDead() {
		this.numUnits  = 0;
		this.unitHP    = 0;
		this.weakestHP = 0;
	}
}
