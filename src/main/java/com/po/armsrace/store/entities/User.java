package com.po.armsrace.store.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class User {
	public @Id String secret;
	public String username;
	public int wins;
	public int played;
	public Ref<Game> activeGame;
	
	/*
	@JsonIgnore
	public Key<User> getKey() {
		return Key.create(User.class, secret);
	}*/
}
