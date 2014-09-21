package com.po.armsrace.battle.events;

import com.po.armsrace.battle.units.Health;

public class ShootEvent implements Event {
	public String event = "shoot";
	public int[] from;
	public int[] to;
	public Health newHealth;
	
	public ShootEvent() {}
	public ShootEvent(int[] from, int[] to, Health newHealth) {
		this.from = from;
		this.to = to;
		this.newHealth = newHealth;
	}
}
