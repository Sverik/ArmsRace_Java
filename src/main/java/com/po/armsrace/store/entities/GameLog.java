package com.po.armsrace.store.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class GameLog {
	public @Id long id;
	public String log;
}
