package com.po.armsrace.store.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class GameLog {
	@Id
	public Long id;

	public Ref<Game> game;
	public long time;

	public boolean peaceOffer1;
	public boolean peaceOffer2;

	public String state1;
	public String state2;

	@Override
	public GameLog clone() {
		GameLog gl = new GameLog();

		gl.id = null;
		gl.game = Ref.create(game.get());
		gl.time = time;
		gl.peaceOffer1 = peaceOffer1;
		gl.peaceOffer2 = peaceOffer2;
		gl.state1 = state1;
		gl.state2 = state2;

		return gl;
	}
}
