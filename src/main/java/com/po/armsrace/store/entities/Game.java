package com.po.armsrace.store.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Game {
	public @Id Long id;
	
	public Ref<User> player1;
	public Ref<User> player2;
	
	public long startTime;
	public long endTime;
	
	public long attackTime;
	public int attacker;
	
	public boolean peaceOffer1;
	public boolean peaceOffer2;
	
	public String state1;
	public String state2;

	// 0 == not finished, 1 == p1 won, 2 == p2 won, 3 == draw
	public int winner;
	public boolean finished;
}
