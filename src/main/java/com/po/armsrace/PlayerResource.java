package com.po.armsrace;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class PlayerResource extends ServerResource {

	@Get("json")
	public Player represent(Object o) {
		Object playerId = getAttribute("playerId");
		Player p = new Player();
		p.name = "Alice-" + playerId;
		return p;
	}

}

class Player {
	public String name;
}