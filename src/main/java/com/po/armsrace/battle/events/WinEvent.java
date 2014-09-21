package com.po.armsrace.battle.events;

public class WinEvent implements Event {
	public final String event = "win";
	public Integer winner;
	public boolean draw;
	
	public WinEvent() {
	}
	
	public WinEvent(Integer winner, boolean draw) {
		this.winner = winner;
		this.draw = draw;
	}
}
