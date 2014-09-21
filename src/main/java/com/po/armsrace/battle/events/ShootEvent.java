package com.po.armsrace.battle.events;

public class ShootEvent implements Event {
	public String event = "shoot";
	public int[] from;
	public int[] to;
	public int[] newHealth;
	
	public ShootEvent() {}
	public ShootEvent(int[] from, int[] to, int[] newHealth) {
		this.from = from;
		this.to = to;
		this.newHealth = newHealth;
	}
}
