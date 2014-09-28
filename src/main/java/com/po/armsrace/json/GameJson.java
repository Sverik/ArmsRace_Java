package com.po.armsrace.json;


public class GameJson {
	public long id;
	
	public int yourNumber;
	
	public String player1;
	public String player2;
	
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
