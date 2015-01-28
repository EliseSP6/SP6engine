package Bleach;

import java.util.List;

public interface LevelInteractable {
	public List<EntityTranslatable> getMobiles();

	public List<EntityTranslatable> getLoots();

	public List<EntityTranslatable> getPlayers();
	
	public List<EntityTranslatable> getProjectiles();
}
