package Bleach;

public class EntityBlob extends EntityLiving {

	public EntityBlob(Sprite sprite, double x, double y) {
		super(sprite, x, y, 11, 5, 2, 50); // radius: 11, hp: 5, attackdamage:
											// 2, speed: 50
	}

	@Override
	double takeDamage(double amount) {
		health = Math.max(0, health - amount);
		// animation? sound?
		return health;
	}

	@Override
	double dealDamage() {
		// modifiers?
		return attackPower;
	}

	@Override
	void AI(LevelInteractable activeLevel) {

		// BS AI
		if (System.currentTimeMillis() % 1000 == 0) {
			System.out.println("Blob AI!");
			bMoving = !bMoving;
			if (bMoving)
				setVectorAngle((Math.random()) * (2 * Math.PI));
		}
		// end BS AI
	}
}
