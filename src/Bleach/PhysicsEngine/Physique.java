package Bleach.PhysicsEngine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import Bleach.Entity;
import Bleach.EntityTranslatable;
import Bleach.LevelInteractable;
import Bleach.TerrainBlock;
import Bleach.PhysicsEngine.CollisionEngine.Impact;
import Bleach.PhysicsEngine.TranslationEngine.Translatraton;

public class Physique {

	private static long timestamp = System.currentTimeMillis();

	public static boolean step(LevelInteractable currentLevelSetting) {

		// Flag that represents whether if a collision has occured during the
		// physics calculation step
		boolean collisionPresent = false;

		// List that will contain all the entities present on the level
		List<EntityTranslatable> entities = accumulateLevelEntityTranslatables(currentLevelSetting);

		// Current time in nanoseconds
		long currentTime = System.currentTimeMillis();
		long deltaTimeMilli = currentTime - timestamp;
		double deltaTimeSec = deltaTimeMilli / 1000.0;

		// Iterate over entities and calculate physics
		for (EntityTranslatable currentEntity : entities) {

			Point2D.Double oldPosition = currentEntity.getPosition();
			// Translate entity
			Point2D.Double newPosition = Translatraton.translate((Entity) currentEntity, deltaTimeSec);

			// Checks whether if the new position collides with any object in
			// its way
			if (!(currentEntity instanceof TerrainBlock && currentEntity.getMass() == 0)) {
				for (EntityTranslatable otherEntity : entities) {

					// As long as it doesn't check for a collision with
					// itself...
					if (currentEntity != otherEntity)
						if (Impact.collides(currentEntity, otherEntity)) {

							// Trigger entities' onCollision-actions, if present
							if (((Entity) currentEntity).getCollisionListener() != null)
								((Entity) currentEntity).getCollisionListener().onCollision((Entity) otherEntity);
							if (((Entity) otherEntity).getCollisionListener() != null)
								((Entity) otherEntity).getCollisionListener().onCollision((Entity) currentEntity);

							// Flag sets true
							collisionPresent = true;

							// Distance between the object's previous position
							// and
							// the position of the object it has collided with
							if (((Entity) otherEntity).hasRectangularCollisionModel()) {
								/*
								 * Handle collision with rectangles. !!This is
								 * buggy!!
								 */

								double deltaDistanceX = (currentEntity.getBoundary().x + currentEntity.getBoundary().width / 2.0) - (otherEntity.getBoundary().x + otherEntity.getBoundary().width / 2.0);
								double deltaDistanceY = (currentEntity.getBoundary().y + currentEntity.getBoundary().height / 2.0) - (otherEntity.getBoundary().y + otherEntity.getBoundary().height / 2.0);
								double stepDeltaDistanceX = oldPosition.x - newPosition.x;
								double stepDeltaDistanceY = oldPosition.y - newPosition.y;

								if (Math.abs(stepDeltaDistanceY) >= Math.abs(stepDeltaDistanceX)) {
									if (deltaDistanceY < 0) {
										newPosition.y = otherEntity.getBoundary().y - currentEntity.getBoundary().height / 2.0;
										((Entity) currentEntity).setLanded(true);
									} else {
										newPosition.y = otherEntity.getBoundary().y + otherEntity.getBoundary().height + currentEntity.getBoundary().height / 2.0;
									}
								} else {
									if (deltaDistanceX < 0) {
										newPosition.x = otherEntity.getBoundary().x - currentEntity.getBoundary().width / 2.0;
									} else {
										newPosition.x = otherEntity.getBoundary().x + otherEntity.getBoundary().width + currentEntity.getBoundary().width / 2.0;
									}
								}

								// // Handle Y first
								// if (entity.getBoundary().y +
								// entity.getBoundary().getHeight() >=
								// otherEntity.getBoundary().y &&
								// entity.getBoundary().y <=
								// otherEntity.getBoundary().y +
								// otherEntity.getBoundary().height) {
								// // Collision from above
								// deltaDistanceY = entity.getBoundary().y +
								// entity.getBoundary().getHeight() -
								// otherEntity.getBoundary().y;
								//
								// // Houston has landed
								// ((Entity) entity).setLanded(true);
								// }
								//
								// // Handle X
								// if (entity.getBoundary().x +
								// entity.getBoundary().width >=
								// otherEntity.getBoundary().x &&
								// entity.getBoundary().x <=
								// otherEntity.getBoundary().x +
								// otherEntity.getBoundary().width) {
								// deltaDistanceX = entity.getBoundary().x +
								// entity.getBoundary().getWidth() -
								// otherEntity.getBoundary().x;
								// }
								//
								// if (deltaDistanceX > deltaDistanceY) {
								// newPosition.y -= deltaDistanceY;
								// } else {
								// newPosition.x -= deltaDistanceX;
								// }

							} else {
								double distanceBeforeCollision = Impact.distanceSquared(oldPosition.x, oldPosition.y, otherEntity.getPosition().x, otherEntity.getPosition().y);
								distanceBeforeCollision -= (currentEntity.getRadius() + otherEntity.getRadius());

								// Repositions the object to the position just
								// before it
								// collides
								// newPosition.x =
								// Math.cos(entity.getVectorAngle())
								// * distanceBeforeCollision;
								// newPosition.y =
								// Math.sin(entity.getVectorAngle())
								// * distanceBeforeCollision;

								double reverseAngle = Math.atan2(otherEntity.getPosition().y - oldPosition.y, otherEntity.getPosition().x - oldPosition.x) + Math.PI;
								newPosition.x += Math.cos(reverseAngle) * distanceBeforeCollision;
								newPosition.y += Math.sin(reverseAngle) * distanceBeforeCollision;
							}
							// entity.setPosition(newPosition);
							currentEntity.setPosition(newPosition);

							// Breaks out of the loop that checks for collisions
							break;
						}
				}
			}
		}

		// Update timestamp
		timestamp = System.currentTimeMillis();

		return collisionPresent;
	}

	private static List<EntityTranslatable> accumulateLevelEntityTranslatables(LevelInteractable level) {
		List<EntityTranslatable> entities = new ArrayList<>();

		// Accumulate objects on scene
		entities.addAll(level.getLoots());
		entities.addAll(level.getMobiles());
		entities.addAll(level.getPlayers());
		entities.addAll(level.getProjectiles());
		entities.addAll(level.getTerrains());

		return entities;
	}
}
