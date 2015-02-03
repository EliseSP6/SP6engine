package Bleach;

import Bleach.PhysicsEngine.Physique;

public class InventoryItem extends Entity{

	public InventoryItem(Sprite sprite, double x, double y){
		super(sprite, x, y, 16);
	}
	
	@Override
	public void tick(LevelInteractable activeLevel){
		for (EntityTranslatable player : activeLevel.getPlayers()) {
			if(Physique.collides(this, player)){
				((EntityLiving)player).getInventory().addItem(this);
				// TODO delete this from the world.
				break;
			}
		}
	}
}