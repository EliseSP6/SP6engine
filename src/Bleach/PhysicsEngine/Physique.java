package Bleach.PhysicsEngine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Bleach.Entity;
import Bleach.EntityTranslatable;
import Bleach.LevelInteractable;
import Bleach.TerrainBlock;

public class Physique {

	@Deprecated
	private static long timestamp_OLD = System.nanoTime();

	private static long timestamp = System.currentTimeMillis();
	private static double gravity = 1.0;

	public static double distanceSquared(double x1, double y1, double x2, double y2) {
		double dX = x2 - x1;
		double dY = y2 - y1;
		return dX * dX + dY * dY;
	}

	public static boolean collides(EntityTranslatable first, EntityTranslatable second) {

		if (((Entity) first).hasRectangularCollisionModel() || ((Entity) second).hasRectangularCollisionModel())
			return rectangularCollisionDetection(first, second);
		else
			return circularCollisionDetection(first, second);
	}

	private static boolean circularCollisionDetection(EntityTranslatable first, EntityTranslatable second) {
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

	private static boolean rectangularCollisionDetection(EntityTranslatable first, EntityTranslatable second) {
		// The sides of the rectangles
		double leftA, leftB;
		double rightA, rightB;
		double topA, topB;
		double bottomA, bottomB;

		// Calculate the sides of rect A
		leftA = first.getBoundary().x;
		rightA = first.getBoundary().x + first.getBoundary().width;
		topA = first.getBoundary().y;
		bottomA = first.getBoundary().y + first.getBoundary().height;

		// Calculate the sides of rect B
		leftB = second.getBoundary().x;
		rightB = second.getBoundary().x + second.getBoundary().width;
		topB = second.getBoundary().y;
		bottomB = second.getBoundary().y + second.getBoundary().height;

		// If any of the sides from A are outside of B
		if (bottomA <= topB) {
			return false;
		}

		if (topA >= bottomB) {
			return false;
		}

		if (rightA <= leftB) {
			return false;
		}

		if (leftA >= rightB) {
			return false;
		}

		// If none of the sides from A are outside B
		return true;
	}

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
		
		// TODO FPS-bottleneck starts here!
		// Print delta time using System.nanoTime()
		// System.out.print("Tick time(render): " + ((System.nanoTime() -
		// timestamp_OLD) / 1000000.0) + " seconds (" + (System.nanoTime() -
		// timestamp_OLD) + " ns)");
		// Print delta time using System.currentTimeMillis()
		// System.out.println(" which equals to: " +
		// ((System.currentTimeMillis() - timestamp) / 1000.0) + " seconds (" +
		// (System.currentTimeMillis() - timestamp) + " ms)");
		// TODO FPS-bottleneck ends here!

		// Iterate over entities and calculate physics
		for (EntityTranslatable entity : entities) {

			Point2D.Double oldPosition = entity.getPosition();
			// Translate entity
			Point2D.Double newPosition = translate((Entity) entity, deltaTimeSec);

			// Checks whether if the new position collides with any object in
			// its way
			if (!(entity instanceof TerrainBlock && entity.getMass() == 0)) {
				for (EntityTranslatable otherEntity : entities) {

					// As long as it doesn't check for a collision with
					// itself...
					if (entity != otherEntity)
						if (collides(entity, otherEntity)) {

							// Trigger entities' onCollision-actions, if present
							if (((Entity) entity).getCollisionListener() != null)
								((Entity) entity).getCollisionListener().onCollision((Entity) otherEntity);
							if (((Entity) otherEntity).getCollisionListener() != null)
								((Entity) otherEntity).getCollisionListener().onCollision((Entity) entity);

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
								
								double deltaDistanceX = (entity.getBoundary().x + entity.getBoundary().width / 2.0) - (otherEntity.getBoundary().x + otherEntity.getBoundary().width / 2.0);
								double deltaDistanceY = (entity.getBoundary().y + entity.getBoundary().height / 2.0) - (otherEntity.getBoundary().y + otherEntity.getBoundary().height / 2.0);
								
								if(Math.abs(deltaDistanceY) >= Math.abs(deltaDistanceX)){
									if(deltaDistanceY < 0){
										newPosition.y = otherEntity.getBoundary().y - entity.getBoundary().height / 2.0;
										((Entity) entity).setLanded(true);
									}
									else{
										newPosition.y = otherEntity.getBoundary().y + otherEntity.getBoundary().height + entity.getBoundary().height / 2.0;
									}
								}else{
									if(deltaDistanceX < 0){
										newPosition.x = otherEntity.getBoundary().x - entity.getBoundary().width / 2.0;
									}
									else{
										newPosition.x = otherEntity.getBoundary().x + otherEntity.getBoundary().width + entity.getBoundary().width / 2;
									}
								}
								
								
//								// Handle Y first
//								if (entity.getBoundary().y + entity.getBoundary().getHeight() >= otherEntity.getBoundary().y && entity.getBoundary().y <= otherEntity.getBoundary().y + otherEntity.getBoundary().height) {
//									// Collision from above
//									deltaDistanceY = entity.getBoundary().y + entity.getBoundary().getHeight() - otherEntity.getBoundary().y;
//
//									// Houston has landed
//									((Entity) entity).setLanded(true);
//								}
//
//								// Handle X
//								if (entity.getBoundary().x + entity.getBoundary().width >= otherEntity.getBoundary().x && entity.getBoundary().x <= otherEntity.getBoundary().x + otherEntity.getBoundary().width) {
//									deltaDistanceX = entity.getBoundary().x + entity.getBoundary().getWidth() - otherEntity.getBoundary().x;
//								}
//
//								if (deltaDistanceX > deltaDistanceY) {
//									newPosition.y -= deltaDistanceY;
//								} else {
//									newPosition.x -= deltaDistanceX;
//								}
								
							} else {
								double distanceBeforeCollision = distanceSquared(oldPosition.x, oldPosition.y, otherEntity.getPosition().x, otherEntity.getPosition().y);
								distanceBeforeCollision -= (entity.getRadius() + otherEntity.getRadius());

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
							entity.setPosition(newPosition);
							
							// Breaks out of the loop that checks for collisions
							break;
						}
				}
			}
		}

		// Update timestamp
		timestamp_OLD = System.nanoTime();
		timestamp = System.currentTimeMillis();

		return collisionPresent;
	}

	private static Point2D.Double translate(Entity entity, double deltaTime) {
		// Gets current values
		double vectorAngle = entity.getForce().getVectorAngle();
		double deltaVelocity = entity.getForce().getMagnitude(deltaTime);

		// Calculates the next position based on velocity
		Point2D.Double nextPosition = entity.getPosition();
		if (entity.isMoving()) {
			nextPosition.x += Math.cos(vectorAngle) * deltaVelocity;
			nextPosition.y += Math.sin(vectorAngle) * deltaVelocity;
		}

		// Re-calculates the next Y-position based on velocity + gravity
		if (entity.isLanded() == false && entity.getMass() > 0.0) {
			double gravitionalAcceleration = gravity * entity.getMass();
			entity.setWeight(entity.getWeight() + gravitionalAcceleration);
			nextPosition.y +=  gravitionalAcceleration * entity.getWeight() * Math.pow(deltaTime, 2);
		}
		
		Iterator<ExternalForce> externalForceIt = entity.getExternalForces().iterator();
		ExternalForce externalForce;
		while(externalForceIt.hasNext()) {
			externalForce = externalForceIt.next();
			double magnitude = externalForce.getMagnitude(deltaTime);
			nextPosition.x += Math.cos(externalForce.getVectorAngle()) * magnitude;
			nextPosition.y += Math.sin(externalForce.getVectorAngle()) * magnitude;
			
			if (externalForce.isExhaused())
				externalForceIt.remove();
		}
		

		// Sets the position to the newly calculated one
		entity.setPosition(nextPosition);

		return nextPosition;
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

	public static interface CollisionListener {
		public void onCollision(Entity collidedWith);
	}

	public static class Force {
		private double vectorAngle;
		private double velocity;

		public Force(double vectorAngle, double velocity) {
			this.vectorAngle = vectorAngle;
			this.velocity = velocity;
		}

		public double getVectorAngle() {
			return vectorAngle;
		}

		public void setVectorAngle(double vectorAngle) {
			this.vectorAngle = vectorAngle;
		}

		public double getVelocity() {
			return velocity;
		}

		public void setVelocity(double velocity) {
			this.velocity = velocity;
		}

		public double getMagnitude(double deltaTime) {
			return velocity * deltaTime;
		}
	}

	public static class ExternalForce {
		private Force force;
		private boolean isExhausted = false;

		public ExternalForce(double vectorAngle, double deltaVelocity) {
			this.force = new Force(vectorAngle, deltaVelocity);
		}

		public double getVectorAngle() {
			return force.getVectorAngle();
		}

		public double getMagnitude(double deltaTime) {
			double magnitude = force.getMagnitude(deltaTime);

			double newVelocity = force.getVelocity() - magnitude*deltaTime;
			if (newVelocity <= Double.MIN_NORMAL) {
				magnitude = magnitude - newVelocity;
				isExhausted = true;
				force.setVelocity(Double.MIN_NORMAL);
			}

			force.setVelocity(newVelocity);
			return magnitude;
		}
		 
		public boolean isExhaused() {
			return isExhausted;
		}
	}
}
