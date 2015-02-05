package Bleach;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public interface LevelInteractable {
	public List<EntityTranslatable> getMobiles();

	public List<EntityTranslatable> getLoots();

	public List<EntityTranslatable> getPlayers();

	public List<EntityTranslatable> getProjectiles();

	public List<BufferedImage> getBackgrounds();

	public List<TerrainBlock> getTerrains();

	public int getBackgroundParallaxDistance();

	public Point2D.Double getViewport();

	public void removeMobile(EntityTranslatable mobile);

	public void removeLoot(EntityTranslatable loot);

	public void removeProjectile(EntityTranslatable projectile);

	public void removePlayer(EntityTranslatable player);

	public void removeTerrain(TerrainBlock terrain);
}
