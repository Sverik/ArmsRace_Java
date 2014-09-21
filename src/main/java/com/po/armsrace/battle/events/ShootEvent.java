package com.po.armsrace.battle.events;

public class ShootEvent implements Event {
	public String event = "shoot";
	public int fromSide;
	public int[] from;
	public int[] to;
	public int[] newHealth;
	public boolean targetDied;
	
	public ShootEvent() {}
	public ShootEvent(int fromSide, int[] from, int[] to, int[] newHealth, boolean targeDied) {
		this.fromSide = fromSide;
		this.from = from;
		this.to = to;
		this.newHealth = newHealth;
		this.targetDied = targeDied;
	}
}
