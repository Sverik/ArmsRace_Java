package com.po.armsrace;

import java.util.Random;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;
import com.po.armsrace.json.GameJson;
import com.po.armsrace.store.OS;
import com.po.armsrace.store.entities.Game;
import com.po.armsrace.store.entities.GameLog;
import com.po.armsrace.store.entities.Queue;
import com.po.armsrace.store.entities.User;


public class QueueResource extends ServerResource {
	public static final long VALID_QUEUE_MS = 10000;

	private static final Random rand = new Random();

	@Post("json")
	public GameJson queue(Object o) {
		String reuseString = getAttribute("reuse");
		final boolean reuse = reuseString != null && "1".equals(reuseString);

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

				if (q == null && ! reuse) {
					// adding new queue
					Queue queue = new Queue();
					queue.updatedTime = System.currentTimeMillis();
					queue.user = Ref.create(user);
					queue.id   = Queue.ID;

					OS.ofy().save().entity(queue);
					return null;
				}

				if (reuse) {
					if (q != null) {
						OS.ofy().delete().entity(q);
					}

					return null;
				}

				if (q == null /* && reuse */) {
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
						Game game = startNewGame(q.user, user);
						// removing queue
						OS.ofy().delete().entity(q);

						return game.getJson(user);
					}
				}
			}
		});


		if (reuse && gj == null) {
			Game game = reuseOldGame(user);
			if (game == null) {
				return null;
			}
			gj = game.getJson(user);
		}
		return gj;
	}

	private Game startNewGame(Ref<User> player1, User player2) {
		Game game = startNewGameForP2(player1, player2);

		User p1 = player1.get();
		p1.activeGame = Ref.create(game);
		OS.ofy().save().entity(p1);

		return game;
	}

	private Game startNewGameForP2(Ref<User> player1, User player2) {
		Game game = GameLogic.getNewGame();
		game.player1   = player1;
		game.player2   = Ref.create(player2);

		OS.ofy().save().entity(game).now();

		GameLog gl = new GameLog();
		gl.game = Ref.create(game);
		OS.ofy().save().entity(gl).now();
		game.current = Ref.create(gl);
		OS.ofy().save().entity(game).now();

		// adding game to users
		player2.activeGame = Ref.create(game);
		OS.ofy().save().entity(player2);

		return game;
	}

	private Game reuseOldGame(User player2) {
		Query<Game> all = OS.ofy().load().type( Game.class ).filter("finished =", true);
		int count = all.count();
		System.out.println("replayableGames: " + count);
		if (count <= 0) {
			return null;
		}
		int i = rand.nextInt(count);
		System.out.println("replayableGame i " + i);
		Game replayableGame = all.offset(i).first().now();
		System.out.println("replayableGame id " + replayableGame.id);

		boolean replayFirstPlayer = rand.nextBoolean();
		// Do not replay replayPlayers again from a game that was already replayed.
		if (replayableGame.replayGame != null) {
			replayFirstPlayer = false;
		}
		Ref<User> player1 = replayFirstPlayer ? replayableGame.player1 : replayableGame.player2;

		Game game = startNewGameForP2(player1, player2);

		game.replayGame = Ref.create(replayableGame);
		game.replayPlayer = replayFirstPlayer ? 1 : 2;

		OS.ofy().save().entity(game).now();

		return game;
	}

}
