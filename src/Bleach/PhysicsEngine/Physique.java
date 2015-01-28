package Bleach.PhysicsEngine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import Bleach.EntityTranslatable;
import Bleach.LevelInteractable;

public class Physique {

	private double distanceSquared(double x1, double y1, double x2, double y2) {
		double dX = x2 - x1;
		double dY = y2 - y1;
		return dX * dX + dY * dY;
	}

	private boolean collides(EntityTranslatable first, EntityTranslatable second) {

		// Closest point on collision box
		Point2D.Double closestPoint = new Point2D.Double(0, 0);

		// Find closest x offset
		if (first.getPosition().x < second.getPosition().x) {
			closestPoint.x = second.getPosition().x;
		} else if (first.getPosition().x > second.getPosition().x + second.getRadius()) {
			closestPoint.x = second.getPosition().x + second.getRadius();
		} else {
			closestPoint.x = first.getPosition().x;
		}

		// Find closest y offset
		if (first.getPosition().y < second.getPosition().y) {
			closestPoint.y = second.getPosition().y;
		} else if (first.getPosition().y > second.getPosition().y + second.getRadius()) {
			closestPoint.y = second.getPosition().y + second.getRadius();
		} else {
			closestPoint.y = first.getPosition().y;
		}

		// If the closest point is inside the circle
		if (distanceSquared(first.getPosition().x, first.getPosition().y, closestPoint.x, closestPoint.y) < first.getRadius() * 2) {
			// This box and the circle have collided
			return true;
		}
		return false;
	}

	public boolean step(LevelInteractable currentLevelSetting) {
		List<EntityTranslatable> entities = new ArrayList<EntityTranslatable>();

		// Accumulate objects on scene
		entities.addAll(currentLevelSetting.getLoots());
		entities.addAll(currentLevelSetting.getMobiles());
		entities.addAll(currentLevelSetting.getPlayers());

		// Iterate over objects and calculate physics
		for (EntityTranslatable entity : entities) {

		}
	}
}
