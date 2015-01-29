package Bleach;

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
		firstLevel.addMobile(new EntityFly(myGame.getSprite("brown_fly"), 200, 264));
		
		firstLevel.setMusicTrack("melody7");
		
		myGame.addLevel(firstLevel);
		
		myGame.init();
		

	}

}
