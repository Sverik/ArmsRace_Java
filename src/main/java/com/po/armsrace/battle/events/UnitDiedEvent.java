package com.po.armsrace.battle.events;

public class UnitDiedEvent implements Event {
	public final String event = "unitdied";
	public int[] location;
	
	public UnitDiedEvent() {}
	public UnitDiedEvent(int[] location) {
		this.location = location;
	}
}
