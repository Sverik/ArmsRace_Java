package com.po.armsrace.battle;

import java.util.ArrayList;
import java.util.List;

import com.po.armsrace.battle.events.Event;
import com.po.armsrace.battle.units.Unit;

public class Battle {
	public ArrayList<ArrayList<Unit>> initialUnits;
	public ArrayList<List<Event>> log = new ArrayList<List<Event>>();
	public Integer winner = null;
	public boolean draw = false;
	
	public Battle() {}
	public Battle(
			ArrayList<ArrayList<Unit>> initialUnits, 
			ArrayList<List<Event>> log, 
			Integer winner) {
		
	}
}
