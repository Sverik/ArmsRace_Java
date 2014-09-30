package com.po.armsrace;

import java.util.Map;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.po.armsrace.battle.units.UnitType;

public class ArmsResource extends ServerResource {

	@Get("json")
	public Map<String, UnitType> arms() {
		return GameLogic.units;
	}
}
