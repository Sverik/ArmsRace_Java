package com.po.armsrace;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Work;
import com.po.armsrace.json.GameJson;
import com.po.armsrace.store.OS;
import com.po.armsrace.store.entities.Game;
import com.po.armsrace.store.entities.Queue;
import com.po.armsrace.store.entities.User;


public class QueueResource extends ServerResource {
	public static final long VALID_QUEUE_MS = 10000;

	@Post("json")
	public GameJson queue(Object o) {
		final User user = PlayerResource.getUser(this);
		if (user == null) {
			setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}

		if (user.activeGame != null && ! user.activeGame.get().finished) {
			return user.activeGame.get().getJson(user);
		}

		// fetch queue or create new
		GameJson gj = OS.ofy().transact(new Work<GameJson>() {
			@Override
			public GameJson run() {
				Queue q = OS.ofy().load().key( Key.create(Queue.class, Queue.ID) ).now();

				if (q == null) {
					// adding new queue
					Queue queue = new Queue();
					queue.updatedTime = System.currentTimeMillis();
					queue.user = Ref.create(user);
					queue.id   = Queue.ID;

					OS.ofy().save().entity(queue);
					return null;
				}

				if (q.user.key().compareTo(Key.create(user)) == 0) {
					// same user, updating time
					q.updatedTime = System.currentTimeMillis();
					OS.ofy().save().entity(q);
					return null;
				} else {
					if (System.currentTimeMillis() - q.updatedTime > VALID_QUEUE_MS) {
						// too old queue, replacing
						q.updatedTime = System.currentTimeMillis();
						q.user = Ref.create(user);
						OS.ofy().save().entity(q);
						return null;
					} else {
						// found up-to-date match, starting game
						Game game = GameLogic.getNewGame();
						game.player1   = q.user;
						game.player2   = Ref.create(user);

						OS.ofy().save().entity(game).now();


						// adding game to users
						User p1 = q.user.get();
						p1.activeGame = Ref.create(game);
						user.activeGame = p1.activeGame;
						OS.ofy().save().entities(user, p1);

						// removing queue
						OS.ofy().delete().entity(q);

						return game.getJson(user);
					}
				}
			}
		});
		return gj;
	}

}
