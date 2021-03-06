package com.po.armsrace.store;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.po.armsrace.store.entities.BattleLog;
import com.po.armsrace.store.entities.Game;
import com.po.armsrace.store.entities.GameLog;
import com.po.armsrace.store.entities.Queue;
import com.po.armsrace.store.entities.User;

/**
 * Our OctifyService provider (short name to keep code clean)
 * @author jaak
 *
 */
public class OS {
	static {
		factory().register(User.class);
		factory().register(Queue.class);
		factory().register(Game.class);
		factory().register(BattleLog.class);
		factory().register(GameLog.class);
	}

	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}

	public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
