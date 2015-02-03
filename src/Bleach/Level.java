package Bleach;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Level implements LevelInteractable{

	private List<EntityTranslatable> mobiles;
	private List<EntityTranslatable> loots;
	private List<EntityTranslatable> players;
	private List<EntityTranslatable> projectiles;
	private List<BufferedImage> backgrounds;				// A list of textures that are to be parallaxed in the background.
	private Point2D.Double viewport;						// Offset for scrolling. This points at the middle of the viewport.
	private int width, height;
	private int parallaxDistance;							// How far away the background layers are. Used for the parallaxing of backgrounds.
	private String key;										// Identifier for this level.
	
	private boolean isScrolling;							// Does the level auto-scroll.
	private double scrollVelocity;							// Auto-scroll speed, pixels per second.
	private double scrollAngle;								// Auto-scroll: scroll towards this angle.
	private long timePreviousScroll;						// Time since last scroll happened. Used to calculate delta-time.
	
	public Level(int width, int height, String key){
		this.width = width;
		this.height = height;
		this.key = key;
		parallaxDistance = 10;
		isScrolling = false;
		scrollVelocity = 0;
		scrollAngle = 0;
		timePreviousScroll = System.nanoTime();
		
		mobiles = new ArrayList<>();
		loots = new ArrayList<>();
		players = new ArrayList<>();
		projectiles = new ArrayList<>();
		backgrounds = new ArrayList<>();
				
		viewport = new Point2D.Double(0, 0);
	}
	
	@Override
	public List<EntityTranslatable> getMobiles() {
		return mobiles;
	}

	@Override
	public List<EntityTranslatable> getLoots() {
		return loots;
	}

	@Override
	public List<EntityTranslatable> getPlayers() {
		return players;
	}
	
	@Override
	public List<EntityTranslatable> getProjectiles() {
		return projectiles;
	}
	
	public void addMobile(EntityTranslatable mob){
		if(mob != null)
			mobiles.add(mob);
	}
	
	public void addLoot(EntityTranslatable loot){
		if(loot != null)
			loots.add(loot);
	}
	
	public void addPlayer(EntityTranslatable player){
		if(player != null)
			players.add(player);
	}
	
	public void addProjectile(EntityTranslatable proj){
		if(proj != null)
			projectiles.add(proj);
	}
	
	public void setViewport(Point2D.Double offset){
		viewport = offset;
	}
	
	public Point2D.Double getViewport(){
		if(isScrolling){
			/* Viewport is set to auto-scroll. Let's calculate the new position based on the delta-time. */
			viewport.x += Math.cos(scrollAngle) * (scrollVelocity * (System.nanoTime() - timePreviousScroll));
			viewport.y += Math.sin(scrollAngle) * (scrollVelocity * (System.nanoTime() - timePreviousScroll));
			timePreviousScroll = System.nanoTime();
		}
		
		return viewport;
	}
	
	public void addBackground(BufferedImage img){
		/* Add a background image to scroll (parallax), add it behind others if some exists already. */
		if(img != null)
			backgrounds.add(img);
	}
	
	public void clearBackgrounds(){
		/* Removes all backgrounds */
		backgrounds.clear();
	}
	
	public List<BufferedImage> getBackgrounds(){
		return backgrounds;
	}
	
	public int setBackgroundParallaxDistance(int dist){
		/* Sets the parallax distance for backgrounds. Returns the old distance. */
		int retval = parallaxDistance;
		parallaxDistance = dist;
		
		return retval;
	}
	
	public int getBackgroundParallaxDistance(){
		return parallaxDistance;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public String getKey(){
		/* Returns the identifier of this level. */
		return key;
	}

	@Override
	public void removeMobile(EntityTranslatable mobile) {
		mobiles.remove(mobile);
	}

	@Override
	public void removeLoot(EntityTranslatable loot) {
		loots.remove(loot);
	}

	@Override
	public void removeProjectile(EntityTranslatable projectile) {
		projectiles.remove(projectile);
	}

	@Override
	public void removePlayer(EntityTranslatable player) {
		players.remove(player);
	}
	
	public void doAutoScroll(boolean doScroll){
		isScrolling = doScroll;
		timePreviousScroll = System.nanoTime();
	}
	
	public void setScrollSpeed(double speedPPS){
		scrollVelocity = speedPPS;
	}
	
	public void setScrollAngle(double angleRad){
		scrollAngle = angleRad;
	}

}
