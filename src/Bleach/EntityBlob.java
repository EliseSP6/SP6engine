package Bleach;

import Bleach.Loader.Discette;
import Bleach.SoundEngine.Boom;

public class EntityBlob extends EntityLiving {

	public EntityBlob(double x, double y) {
		super(Discette.getImage("greenblob"),
				x,
				y,
				32,		// radius
				11,		// health
				2,		// attach power
				50);	// speed 
	}

	@Override
	void AI(LevelInteractable activeLevel) {

		// BS AI
		if (System.currentTimeMillis() % 100 == 0) {
			bMoving = !bMoving;
			if (bMoving)
				getForce().setVectorAngle(((int)(Math.random()*2)==1?Math.PI:0));
				//getForce().setVectorAngle((Math.random()) * (2 * Math.PI));
		}
		if (System.currentTimeMillis() % 50 == 0) {
			double px, py;
			double magnitude = getRadius();
			
			px = x + (Math.cos(getForce().getVectorAngle()) * magnitude);
			py = y + (Math.sin(getForce().getVectorAngle()) * magnitude);
			
			((Level) activeLevel).addProjectile(new ProjectileBullet(px, py, getForce().getVectorAngle(), this));
			Boom.playSound("shoot5");
		}
		// end BS AI
	}

	@Override
	double dealDamage() {
		// modifiers?
		return attackPower;
	}

	@Override
	double takeDamage(double amount) {
		health = Math.max(0, health - amount);
		// animation? sound?
		if(health == 0){
			die();
		}
		return health;
	}
}
