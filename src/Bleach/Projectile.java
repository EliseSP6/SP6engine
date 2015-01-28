package Bleach;

public abstract class Projectile extends Entity{

	protected EntityLiving owner = null;		// Used for checking if the projectile came from the player or enemies. Makes it so that enemies don't shoot each other and enables "friendly fire" options for players.
	
	public Projectile(Sprite sprite, double x, double y, double r, EntityLiving owner){
		super(sprite, x, y, r);
		this.owner = owner;
	}
	
	abstract void tick();
	abstract double damage();
}
