package Bleach;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.HashMap;
import java.util.Map;

import Bleach.PhysicsEngine.Physique.CollisionListener;
import Bleach.PhysicsEngine.Physique.ExternalForce;
import Bleach.PhysicsEngine.Physique.Force;

public class Entity implements EntityTranslatable {
	protected Sprite sprite;
	protected double x, y;
	protected final double r;
	protected boolean hasRectangularCollisionModel = false;
	protected boolean isLanded = false;
	protected Force internalForce = new Force(Math.toRadians(90), 0);
	protected Map<Object, ExternalForce> externalForces = new HashMap<>();
	protected CollisionListener onCollision = null;

	protected double mass = 0.0;
	protected double weight = 0;

	protected long timePreviousTick;
	protected long timeStartFalling;
	protected boolean bMoving; // Is the entity currently moving?

	protected Entity(Sprite sprite, double x, double y, double r) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.r = r;
		timePreviousTick = System.currentTimeMillis();
		timeStartFalling = System.currentTimeMillis();
		bMoving = false;
	}

	@Override
	public void addExternalForce(Object identifier, ExternalForce externalForce) {
		this.externalForces.put(identifier, externalForce);
	}

	@Override
	public Rectangle2D.Double getBoundary() {
		return new Rectangle2D.Double(x - r, y - r, r * 2, r * 2);
	}

	@Override
	public CollisionListener getCollisionListener() {
		return onCollision;
	}

	@Override
	public Map<Object, ExternalForce> getExternalForces() {
		return this.externalForces;
	}

	@Override
	public double getFallingTime() {
		/*
		 * Returns the delta time in seconds since this started falling.
		 */

		if (isLanded)
			return 0;
		else
			return (System.currentTimeMillis() - timeStartFalling) / 1000.0;
	}

	@Override
	public Force getForce() {
		return internalForce;
	}

	@Override
	public double getMass() {
		return mass;
	}

	@Override
	public Point2D.Double getPosition() {
		return new Point2D.Double(x, y);
	}

	@Override
	public double getRadius() {
		return r;
	}

	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	public boolean hasRectangularCollisionModel() {
		return hasRectangularCollisionModel;
	}

	@Override
	public boolean isLanded() {
		return isLanded;
	}

	@Override
	public boolean isMoving() {
		return bMoving;
	}

	@Override
	public void isMoving(boolean setMoving) {
		bMoving = setMoving;
	}

	public void setHasRectangularCollisionModel(boolean hasRectangularCollisionModel) {
		this.hasRectangularCollisionModel = hasRectangularCollisionModel;
	}

	@Override
	public void setLanded(boolean isLanded) {
		if (this.isLanded && !isLanded) {
			timeStartFalling = System.currentTimeMillis();
		}

		if (isLanded) {
			this.getExternalForces().remove(ExternalForce.ForceIdentifier.JUMP);
			this.getExternalForces().remove(ExternalForce.ForceIdentifier.GRAVITY);
		}

		this.isLanded = isLanded;
	}

	@Override
	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	public void setOnCollision(CollisionListener onCollision) {
		this.onCollision = onCollision;
	}

	@Override
	public void setPosition(Point2D.Double position) {
		this.x = position.x;
		this.y = position.y;
	}

	@Override
	public void setWeight(double weight) {
		this.weight = weight;

	}

	public void tick(LevelInteractable activeLevel) {
		// if(isMoving()){
		// long deltaTime = System.nanoTime() - timePreviousTick;
		// double magnitude = (deltaTime / 1000000000.0) * velocity;
		// Point2D.Double position = getPosition();
		//
		// position.x += Math.cos(vectorAngle) * magnitude;
		// position.y += Math.sin(vectorAngle) * magnitude;
		//
		// setPosition(position);
		// }
	}
}
