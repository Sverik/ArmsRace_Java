package com.po.armsrace;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ArmsRaceApplication extends Application {

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public Restlet createInboundRoot() {
		// Create a router Restlet that routes each call to a
		// new instance of HelloWorldResource.
		Router router = new Router(getContext());

		// Defines only one route
		router.attachDefault(ArmsResource.class);
		router.attach("/arms/{armId}", ArmsResource.class);
		router.attach("/battle/{battleId}", BattleResource.class);
		router.attach("/register/{username}", RegisterResource.class);
		router.attach("/player/", PlayerResource.class);
		router.attach("/queue/", QueueResource.class);

		return router;
	}

}
