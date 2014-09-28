package com.po.armsrace.store.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

/**
 * Used basically as singleton (with id == 1)
 * 
 * @author jaak
 * 
 */
@Entity
@Cache
public class Queue {
	public static final long ID = 1;

	public @Id
	long id;
	public @Load
	Ref<User> user;
	public long updatedTime;
}
