package com.po.armsrace.store.entities;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.po.armsrace.json.GameJson;

@Entity
@Cache
public class Game {
	public @Id Long id;
	
	public Ref<User> player1;
	public Ref<User> player2;
	
	public long startTime;
	public long endTime;
	
	public long attackTime;
	// 0 == no attack, 1 == p1 attacks, 2 == p2 attacks
	public int attacker;
	
	public boolean peaceOffer1;
	public boolean peaceOffer2;
	
	public String state1;
	public String state2;

	// 0 == not finished, 1 == p1 won, 2 == p2 won, 3 == draw
	public int winner;
	public boolean finished;
	
	public GameJson getJson(User user) {
		GameJson gj = new GameJson();
		
		if (Key.create(user).compareTo(player1.getKey()) == 0) {
			gj.yourNumber = 1;
		} else {
			gj.yourNumber = 2;
		}
		
		gj.id = id;
		gj.player1 = player1.get().username;
		gj.player2 = player2.get().username;
		
		gj.startTime = startTime;
		gj.endTime = endTime;
		
		gj.attackTime = attackTime;
		gj.attacker = attacker;
		
		gj.peaceOffer1 = peaceOffer1;
		gj.peaceOffer1 = peaceOffer2;
		
		gj.state1 = state1;
		gj.state2 = state2;

		gj.winner = winner;
		gj.finished = finished;
		return gj;
	}
}
