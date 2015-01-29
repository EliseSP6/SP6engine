package Bleach.Renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Bleach.Entity;
import Bleach.EntityTranslatable;
import Bleach.LevelInteractable;

public class Picasso {
	private Graphics graphics;
	private int width, height;								// Screen width and height.
	private List<String> debug;								// Debug data to be printed on the screen.
	private boolean doDebug;								// Whether to display the debug data or not.

	public Picasso(Graphics graphics, int width, int height) {
		this.graphics = graphics;
		this.width = width;
		this.height = height;
		debug = new ArrayList<String>();
		doDebug = true;
	}
	
	public void setSize(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public void setDebug(boolean doDebug){
		this.doDebug = doDebug;
	}
	
	public void addDebugLine(String line){
		debug.add(line);
	}
	
	public void clearDebugLines(){
		debug.clear();
	}
	
	public void render(LevelInteractable currentLevelSetting) {
		// Render level backgrounds using the parallax effect.
		int currentBackgroundNumber = 1;
		List<BufferedImage> backgrounds = currentLevelSetting.getBackgrounds();
		for (BufferedImage background : backgrounds) {
			// Calculate the position of this background and tile it if needed.
			int parallaxDistance = currentLevelSetting.getBackgroundParallaxDistance();
			double parallaxModifier = currentBackgroundNumber * (parallaxDistance / 10);		// Do some math to get a modifier that seems to be Ok. This modifier alters the position of the current background in order to create the parallax effect.
			double scrollX = currentLevelSetting.getViewport().getX() / width;
			double scrollY = currentLevelSetting.getViewport().getY() / height;
			int tileCountX = (int) Math.ceil(width / background.getWidth());
			int tileCountY = (int) Math.ceil(height / background.getHeight());
			
			int startX = (int) (background.getWidth() - (background.getWidth() * scrollX * parallaxModifier));
			int startY = (int) (background.getHeight() - (background.getHeight() * scrollY * parallaxModifier));
			
			for(int i = 0; i < tileCountX; i++){
				for(int j = 0; j < tileCountY; j++){
					int x = startX + i * background.getWidth();
					int y = startY + i * background.getWidth();
					
					graphics.drawImage(background, x, y, background.getWidth(), background.getHeight(), null);
				}
			}
			currentBackgroundNumber++;
		}
		
		// List that will contain all the entities present on the level
		List<EntityTranslatable> entities = new ArrayList<EntityTranslatable>();

		// Accumulate objects on scene
		entities.addAll(currentLevelSetting.getLoots());
		entities.addAll(currentLevelSetting.getMobiles());
		entities.addAll(currentLevelSetting.getPlayers());
		entities.addAll(currentLevelSetting.getProjectiles());

		// Iterate over objects and render them
		for (EntityTranslatable entityTranslatable : entities) {
			Entity entity = (Entity) entityTranslatable;
			graphics.drawImage(entity.getSprite().getFrame(), (int) entity.getPosition().x, (int) entity.getPosition().y, entity.getSprite().getWidth(), entity.getSprite().getHeight(), null);
		}
		
		// Handle debug data
		if(doDebug){
			int lineNumber = 0;
			for (String line : debug) {
				graphics.drawString(line, 10, 10 + 10 * lineNumber);
				lineNumber++;
			}
		}
	}
}
