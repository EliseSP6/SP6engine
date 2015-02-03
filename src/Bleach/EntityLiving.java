package Bleach;

public abstract class EntityLiving extends Entity{

	protected double health;							// Current HP
	protected double healthMax;							// Maximum HP
	protected double attackPower;						// How much damage the entity deals when it attacks.
	protected Inventory inventory;
	
	protected EntityLiving(Sprite sprite, double x, double y, double r, double health, double attackPower, double speed) {
		super(sprite, x, y, r);
		this.health = this.healthMax = health;
		this.attackPower = attackPower;
		this.velocity = speed;
		inventory = new Inventory();
	}
	
	abstract double takeDamage(double amount);			// Returns health after damage.
	abstract double dealDamage();
	abstract void AI(LevelInteractable activeLevel);
	public double getHealth(){ return health; }
	public double getHealthMax(){ return healthMax; }
	public double getDamage(){ return attackPower; }
	public Inventory getInventory(){ return inventory; };
	
	@Override
	public void tick(LevelInteractable activeLevel){
		super.tick(activeLevel);
		
		AI(activeLevel);
		timePreviousTick = System.nanoTime();
	}
}
