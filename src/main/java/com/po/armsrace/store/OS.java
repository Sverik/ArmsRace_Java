package com.po.armsrace.store;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.po.armsrace.store.entities.User;

/**
 * Our OctifyService provider (short name to keep code clean)
 * @author jaak
 *
 */
public class OS {
	static {
		factory().register(User.class);
	}
	
	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}
	
	public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
