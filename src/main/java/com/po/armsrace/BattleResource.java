package com.po.armsrace;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.po.armsrace.battle.Battle;
import com.po.armsrace.battle.Map;
import com.po.armsrace.battle.units.Marine;
import com.po.armsrace.battle.units.Sniper;
import com.po.armsrace.battle.units.Unit;

public class BattleResource extends ServerResource {
	@Get("json")
	public Battle battle(Object o) {
		Map map = new Map();
		//map.addUnit(0, new Unit(new Tank(), 10, new int[]{2, 8}) );
		map.addUnit(0, new Unit(new Marine(), 50, new int[]{4, 5}) );
		
		map.addUnit(1, new Unit(new Sniper(), 10, new int[]{17, 2}) );
		//map.addUnit(1, new Unit(new TankDestroyer(), 10, new int[]{17, 3}) );
		//map.addUnit(1, new Unit(new ChemicalTroops(), 10, new int[]{17, 3}) );
		//map.addUnit(1, new Unit(new Tank(), 20, new int[]{17, 2}) );
		return map.doBattle();
	}

}
