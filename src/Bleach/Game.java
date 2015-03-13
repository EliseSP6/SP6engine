package Bleach;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import Bleach.InputManager.Receptionist;
import Bleach.InputManager.Receptionist.KeyBinding;
import Bleach.PhysicsEngine.CollisionEngine.CollisionListener;
import Bleach.SoundEngine.Boom;

/*
 * This is for testing the game engine.
 * This is where the game developer resides.
 * 
 * */

public class Game {

	public static void main(String[] args) {

		final Bleach myGame = new Bleach();

		myGame.loadImages("SP6engine/assets/images/assets.json");

		try {
			myGame.loadSounds("SP6engine/assets/sounds/assets.json");
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (UnsupportedAudioFileException e2) {
			e2.printStackTrace();
		}

		myGame.setFPS(60);

		myGame.setSize(800, 600);
		myGame.setTitle("Kill the aliens, save the world");

		final Level firstLevel = new Level(myGame.loadLevel("SP6engine/assets/levels/level1.json"));
		
		final Player player = new Player(myGame.getSprite("player"), 200, 464);
		
		firstLevel.addMobile(new EntityBlob(900, 564));
		firstLevel.addMobile(new EntityBlob(1500, 564));
		firstLevel.addMobile(new EntityBlob(1900, 164));
		firstLevel.addMobile(new EntityBlob(2500, 24));
		firstLevel.addMobile(new EntityBlob(2500, 564));
		
		firstLevel.addPlayer(player);

		// firstLevel.setMusicTrack("melody7");

		myGame.addLevel(firstLevel);

		myGame.init();

		// Adding a hot receptionist
		Receptionist receptionist = new Receptionist() {

			@Override
			public void handleEvent(ActionEvent event) {
			}

			@Override
			public void handleEvent(MouseEvent event) {
			}
		};

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("pressed A"), "pressed A", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.getForce().setVectorAngle(Math.PI);
				player.isMoving(true);
				player.sprite = myGame.getSprite("player_walk_left");
			}
		}));

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("released A"), "released A", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.isMoving(false);
				player.sprite = myGame.getSprite("player");
			}
		}));

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("pressed D"), "pressed D", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.getForce().setVectorAngle(0);
				player.isMoving(true);
				player.sprite = myGame.getSprite("player_walk_right");
			}
		}));

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("released D"), "released D", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				player.isMoving(false);
				player.sprite = myGame.getSprite("player");
			}
		}));
		
		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("shift pressed SHIFT"), "shift pressed SHIFT", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				double px, py;
				double magnitude = player.getRadius();
				
				px = player.x + (Math.cos(player.getForce().getVectorAngle()) * magnitude);
				py = player.y + (Math.sin(player.getForce().getVectorAngle()) * magnitude);
				
				firstLevel.addProjectile(new ProjectileBullet(px, py, player.getForce().getVectorAngle(), player));
				Boom.playSound("shoot1");
			}
		}));

		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("pressed SPACE"), "pressed SPACE", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(player.jump(450)){
					Boom.playSound("jump1");
				}
			}
		}));

		((Entity) player).setOnCollision(new CollisionListener() {

			@Override
			public void onCollision(Entity collidedWith) {
			}

		});

		myGame.addReceptionist(receptionist);

		myGame.run();
	}

}
