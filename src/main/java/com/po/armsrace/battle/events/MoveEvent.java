package com.po.armsrace.battle.events;

public class MoveEvent implements Event {
	public final String event = "move";
	public int[] from;
	public int[] to;
	
	public MoveEvent() {}
	public MoveEvent(int[] from, int[] to) {
		this.from = from;
		this.to = to;
	}
}
