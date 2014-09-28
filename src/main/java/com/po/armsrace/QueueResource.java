package com.po.armsrace;

import org.restlet.data.Cookie;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.Key;
import com.po.armsrace.store.OS;
import com.po.armsrace.store.entities.User;

/*
class QueueResult {
	Long gameId;
	boolean inqueue;
}*/

public class QueueResource extends ServerResource {
	public static final long VALID_QUEUE_MS = 10000;
	public static final long START_DELAY_MS = 30000;
	public static final long GAME_DURATION_MS = 4*60*1000;
	
	@Post("json")
	public String queue(Object o) {
		final User user = getUser(this);
		return null;

		/*
		if (user.activeGame != null) {
			return user.activeGame.get();
		}
		
		// fetch queue or create new
		Game game = OS.ofy().transact(new Work<Game>() {
			@Override
			public Game run() {
				Queue q = OS.ofy().load().key( Key.create(Queue.class, Queue.ID) ).now();
				
				if (q == null) {
					// adding new queue
					Queue queue = new Queue();
					queue.updatedTime = System.currentTimeMillis();
					queue.user = Ref.create(user);
					
					OS.ofy().save().entity(queue);
					return null;
				}
				
				if (q.user.key().compareTo(Key.create(user)) == 0) {
					// same user, updating time
					q.updatedTime = System.currentTimeMillis();
					OS.ofy().save().entity(q);
					return null;
				} else {
					if (System.currentTimeMillis() - q.updatedTime < VALID_QUEUE_MS) {
						// too old queue, replacing
						q.updatedTime = System.currentTimeMillis();
						q.user = Ref.create( Key.create(user) );
						OS.ofy().save().entity(q);
						return null;
					} else {
						// found up-to-date match, starting game
						Game game = new Game();
						game.startTime = System.currentTimeMillis() + START_DELAY_MS;
						game.endTime   = game.startTime + GAME_DURATION_MS;
						game.player1   = q.user;
						game.player2   = Ref.create(Key.create(user));
						
						OS.ofy().save().entity(game).now();
						// adding game to users'
						User p1 = q.user.get();
						p1.activeGame = Ref.create(game);
						user.activeGame = p1.activeGame;
						OS.ofy().save().entities(user, p1);

						return game;
					}
				}
			}
		});
		return game;
		*/
	}
	
	public User getUser(ServerResource resource) {
		Cookie secretCookie = resource.getCookies().getFirst("secret");
		if (secretCookie == null) {
			return null;
		}
		String secret = secretCookie.getValue();
		// fetching user from GAE datastore
		User u = OS.ofy().load().key( Key.create(User.class, secret) ).now();
		return u;
	}

}
