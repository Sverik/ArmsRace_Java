package com.po.armsrace.store;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class User {
	public @Id String secret;
	public String username;
	public int wins;
	public int played;
}
