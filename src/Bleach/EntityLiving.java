package Bleach;

import java.awt.geom.Point2D;

public abstract class EntityLiving extends Entity{

	protected double health;							// Current HP
	protected double healthMax;							// Maximum HP
	protected double attackPower;						// How much damage the entity deals when it attacks.
	protected double speed;								// How fast the entity can move.
	protected boolean bMoving;							// Is the entity currently moving.
	
	protected EntityLiving(Sprite sprite, double x, double y, double r, double health, double attackPower, double speed) {
		super(sprite, x, y, r);
		this.health = this.healthMax = health;
		this.attackPower = attackPower;
		this.speed = speed;
		bMoving = false;
	}
	
	abstract double takeDamage(double amount);			// Returns health after damage.
	abstract double dealDamage();
	abstract void AI(LevelInteractable activeLevel);
	public double getHealth(){ return health; }
	public double getHealthMax(){ return healthMax; }
	public double getDamage(){ return attackPower; }
	public boolean isMoving(){ return bMoving; }
	
	@Override
	public void tick(LevelInteractable activeLevel){
		super.tick(activeLevel);
		
		AI(activeLevel);
		
		if(isMoving()){
			long deltaTime = System.nanoTime() - timePreviousTick;
			double magnitude = (deltaTime / 1000000.0) * speed;
			Point2D.Double position = getPosition();
			
			position.x += Math.cos(vectorAngle) * magnitude;
			position.y += Math.sin(vectorAngle) * magnitude;
			
			setPosition(position);
		}
	}
}
