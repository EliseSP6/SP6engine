package Bleach.PhysicsEngine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import Bleach.EntityTranslatable;
import Bleach.LevelInteractable;

public class Physique {

	static long timestamp = System.nanoTime();

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

		// Flag that represents whether if a collision has occured during the
		// physics calculation step
		boolean collisionPresent = false;

		// List that will contain all the entities present on the level
		List<EntityTranslatable> entities = new ArrayList<EntityTranslatable>();

		// Accumulate objects on scene
		entities.addAll(currentLevelSetting.getLoots());
		entities.addAll(currentLevelSetting.getMobiles());
		entities.addAll(currentLevelSetting.getPlayers());

		// Current time in nanoseconds
		long currentTime = System.nanoTime();

		// Iterate over objects and calculate physics
		for (EntityTranslatable entity : entities) {
			// Gets current values
			double vectorAngle = entity.getVectorAngle();
			double velocity = entity.getVelocity();
			Point2D.Double currentPosition = entity.getPosition();

			// Calculates the next position
			Point2D.Double nextPosition = new Point2D.Double();
			nextPosition.x = Math.cos(vectorAngle) * (velocity * (timestamp - currentTime));
			nextPosition.y = Math.sin(vectorAngle) * (velocity * (timestamp - currentTime));

			// Sets the position to the newly calculated one
			entity.setPosition(nextPosition);

			// Checks whether if the new position collides with any object in
			// its way
			for (EntityTranslatable otherEntity : entities)

				// As long as it doesn't check for a collision with itself...
				if (entity != otherEntity)
					if (collides(entity, otherEntity)) {

						// Flag sets true
						collisionPresent = true;

						// Distance between the object's previous position and
						// the position of the object it has collided with
						double distanceBeforeCollision = distanceSquared(currentPosition.x, currentPosition.y, otherEntity.getPosition().x, otherEntity.getPosition().y);

						// Repositions the object to the position just before it
						// collides
						nextPosition.x = Math.cos(vectorAngle) * distanceBeforeCollision;
						nextPosition.y = Math.sin(vectorAngle) * distanceBeforeCollision;

						// Breaks out of the loop that checks for collisions
						break;
					}

		}

		// Update timestamp
		timestamp = System.nanoTime();

		return collisionPresent;
	}
}
