package Bleach;

public abstract class EntityLiving extends Entity{

	protected double health;		// Current HP
	protected double healthMax;		// Maximum HP
	protected double damage;		// How much damage the entity deals when it attacks.
	
	protected EntityLiving(Sprite sprite, double x, double y, double r, double health, double damage) {
		super(sprite, x, y, r);
		this.health = this.healthMax = health;
		this.damage = damage;
	}
	
	abstract double takeDamage(double amount);	// Returns health after damage.
	abstract double dealDamage();
	public double getHealth(){ return health; }
	public double getHealthMax(){ return healthMax; }
	public double getDamage(){ return damage; }
}
