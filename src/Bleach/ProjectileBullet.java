package Bleach;

import java.util.List;
import java.util.ArrayList;

import Bleach.PhysicsEngine.Physique;

public class ProjectileBullet extends Projectile{

	public ProjectileBullet(Sprite sprite, double x, double y, double r, EntityLiving owner){
		super(sprite, x, y, r, owner);
		
	}

	@Override
	double dealDamage() {
		/* Calculate the amount of damage this projectile does. owner could be used to modify the damage (buffs etc) */
		
		return 5;
	}
	
	@Override
	public void tick(LevelInteractable activeLevel){
		// Assemble a list of all Entities that we want this projectile to interact with.
		List<EntityLiving> interactors = new ArrayList<EntityLiving>();
		for (EntityTranslatable entity : activeLevel.getMobiles()) {
			interactors.add((EntityLiving)entity);
		}
		
		/*// Should we include playes here? I'm not sure atm.
		for (EntityTranslatable entity : activeLevel.getPlayers()) {
			interactors.add((EntityLiving)entity);
		}
		*/
		
		for (EntityLiving entity : interactors) {
			if(Physique.collides(this, entity)){
				entity.takeDamage(dealDamage());
				// sound engine play sound!
				activeLevel.removeProjectile(this);		// This projectile should die now.
				break;
			}
		}
	}
}
