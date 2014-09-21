package com.po.armsrace;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.po.armsrace.battle.Map;
import com.po.armsrace.battle.events.Event;
import com.po.armsrace.battle.units.Marine;
import com.po.armsrace.battle.units.Tank;
import com.po.armsrace.battle.units.Unit;

public class BattleResource extends ServerResource {
	@Get("json")
	public List<List<Event>> battle(Object o) {
		Map map = new Map();
		map.addUnit(0, new Unit(new Tank(), 10, new int[]{2, 8}) );
		map.addUnit(0, new Unit(new Marine(), 50, new int[]{4, 5}) );
		
		map.addUnit(1, new Unit(new Tank(), 20, new int[]{17, 7}) );
		map.addUnit(1, new Unit(new Marine(), 10, new int[]{15, 5}) );
		map.addUnit(1, new Unit(new Tank(), 20, new int[]{17, 2}) );
		map.doBattle();
		return map.getLog();
	}

}
