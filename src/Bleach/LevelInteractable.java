package Bleach;

import java.util.List;

public interface LevelInteractable {
	public List<EntityTranslatable> getMobiles();

	public List<EntityTranslatable> getLoots();

	public List<EntityTranslatable> getPlayers();
	
	public List<EntityTranslatable> getProjectiles();
	
	public void removeMobile(EntityTranslatable mobile);
	
	public void removeLoot(EntityTranslatable loot);
	
	public void removeProjectile(EntityTranslatable projectile);
	
	public void removePlayer(EntityTranslatable player);
}
