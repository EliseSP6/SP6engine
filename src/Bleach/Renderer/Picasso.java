package Bleach.Renderer;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import Bleach.Entity;
import Bleach.EntityTranslatable;
import Bleach.LevelInteractable;

public class Picasso {
	private Graphics graphics;

	Picasso(Graphics graphics) {
		this.graphics = graphics;
	}

	public void render(LevelInteractable currentLevelSetting) {
		// List that will contain all the entities present on the level
		List<EntityTranslatable> entities = new ArrayList<EntityTranslatable>();

		// Accumulate objects on scene
		entities.addAll(currentLevelSetting.getLoots());
		entities.addAll(currentLevelSetting.getMobiles());
		entities.addAll(currentLevelSetting.getPlayers());
		entities.addAll(currentLevelSetting.getProjectiles());

		// Iterate over objects and render them
		for (EntityTranslatable entityTranslatable : entities) {
			Entity entity = (Entity) entityTranslatable;
			graphics.drawImage(entity.getSprite().getFrame(), (int) entity.getPosition().x, (int) entity.getPosition().y, entity.getSprite().getWidth(), entity.getSprite().getHeight(), null);
		}
	}
}
