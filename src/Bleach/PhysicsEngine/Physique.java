package Bleach.PhysicsEngine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import Bleach.Entity;
import Bleach.EntityTranslatable;
import Bleach.LevelInteractable;

public class Physique {

	private static long timestamp = System.nanoTime();
	private static long timeMS = System.currentTimeMillis();
	private static double gravity = 0.01;

	public static double distanceSquared(double x1, double y1, double x2, double y2) {
		double dX = x2 - x1;
		double dY = y2 - y1;
		return dX * dX + dY * dY;
	}

	public static boolean collides(EntityTranslatable first, EntityTranslatable second) {

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

		// If the closest point is inside the circle, the circles have collided
		if (distanceSquared(first.getPosition().x, first.getPosition().y, closestPoint.x, closestPoint.y) < first.getRadius() * 2) {
			return true;
		}
		return false;
	}

	public static boolean step(LevelInteractable currentLevelSetting) {

		// Flag that represents whether if a collision has occured during the
		// physics calculation step
		boolean collisionPresent = false;

		// List that will contain all the entities present on the level
		List<EntityTranslatable> entities = new ArrayList<EntityTranslatable>();

		// Accumulate objects on scene
		entities.addAll(currentLevelSetting.getLoots());
		entities.addAll(currentLevelSetting.getMobiles());
		entities.addAll(currentLevelSetting.getPlayers());
		entities.addAll(currentLevelSetting.getProjectiles());

		// Current time in nanoseconds
		long currentTime = System.nanoTime();
		double deltaTime = currentTime - timestamp;
		
	
	// Print delta time using System.nanoTime()
	System.out.print("Tick time(render): " + ((System.nanoTime() - timestamp)/1000000.0) + " seconds (" + (System.nanoTime() - timestamp) + " ns)");
	// Print delta time using System.currentTimeMillis()
	System.out.println(" which equals to: " + ((System.currentTimeMillis() - timeMS)/1000.0) + " seconds (" + (System.currentTimeMillis() - timeMS) + " ms)");
	
	
		// Iterate over objects and calculate physics
		for (EntityTranslatable entity : entities) {
			// Gets current values
			
			double vectorAngle = entity.getVectorAngle();
			double velocity = entity.getVelocity();
			double magnitude = (deltaTime / 1000000.0) * velocity;

			// Calculates the next position based on velocity
			Point2D.Double nextPosition = entity.getPosition();
			if (((Entity) entity).isMoving()) {
				nextPosition.x += Math.cos(vectorAngle) * magnitude;
				nextPosition.y += Math.sin(vectorAngle) * magnitude;
			}
			// Re-calculates the next Y-position based on velocity + gravity
			nextPosition.y += gravity * Math.pow((deltaTime / 1000000.0), 2);

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
						double distanceBeforeCollision = distanceSquared(nextPosition.x, nextPosition.y, otherEntity.getPosition().x, otherEntity.getPosition().y);

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
		timeMS = System.currentTimeMillis();

		return collisionPresent;
	}

	public static double getGravity() {
		return gravity;
	}

	public static void setGravity(double gravity) {
		Physique.gravity = gravity;
	}
}
