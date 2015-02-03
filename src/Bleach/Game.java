package Bleach;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import Bleach.InputManager.Receptionist;
import Bleach.InputManager.Receptionist.KeyBinding;

/*
 * This is for testing the game engine.
 * This is where the game developer resides.
 * 
 * */

public class Game {

	public static void main(String[] args) {

		Bleach myGame = new Bleach();


		myGame.loadImages("assets/images/assets.json");
		myGame.loadSounds("assets/sounds/assets.json");

		myGame.setFPS(60);

		myGame.setSize(800, 600);
		myGame.setTitle("My super game!");

		Level firstLevel = new Level(2800, 1200, "Town");

		firstLevel.addBackground(myGame.getTexture("clouds"));
		firstLevel.addBackground(myGame.getTexture("sky"));
		EntityBlob blobby = new EntityBlob(myGame.getSprite("blob"), 200, 264);
		Player player = new Player(myGame.getSprite("mushi"), 100, 100);
		firstLevel.addMobile(blobby);
		firstLevel.addPlayer(player);

		// firstLevel.setMusicTrack("melody7");

		myGame.addLevel(firstLevel);

		myGame.init();

		// Adding a hot receptionist
		Receptionist receptionist = new Receptionist() {

			@Override
			public void handleEvent(ActionEvent event) {
				// TODO Auto-generated method stub
			}

			@Override
			public void handleEvent(MouseEvent event) {
				//System.out.println("X: " + event.getX() + " Y: " + event.getY());
			}
		};
		
		// Telling to receptionist to listen for whenever the LEFT-arrow button is pushed. When it is, blobby's vector-angle is set to 180 degrees.
		receptionist.addKeyBinding(new KeyBinding(KeyStroke.getKeyStroke("RIGHT"), "RIGHT", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				blobby.setVectorAngle(Math.toRadians(180));
			}
		}));

		myGame.addReceptionist(receptionist);
		
		
		
		myGame.run();
	}

}
