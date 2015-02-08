package Bleach.PhysicsEngine.Force;

import Bleach.PhysicsEngine.CollisionEngine.CollisionListener;

public class ExternalForce {

	public static enum ForceIdentifier {
		GRAVITY, WIND, JUMP;
	}

	private Force force;
	private CollisionListener collisionListener = null;

	public boolean isActive() {
		return force.isActive();
	}

	public void setActive(boolean isActive) {
		force.setActive(isActive);
	}

	private boolean isExhausted = false;

	public ExternalForce(double vectorAngle, double deltaVelocity) {
		this.force = new Force(vectorAngle, deltaVelocity);
	}

	public double getMagnitude(double deltaTime) {
		if (isExhausted)
			return Double.MIN_NORMAL;

		double magnitude = force.getMagnitude(deltaTime);

		double newVelocity = force.getVelocity() - magnitude;
		
		if (newVelocity <= Double.MIN_NORMAL) {
			magnitude = magnitude - newVelocity;
			isExhausted = true;
			force.setVelocity(Double.MIN_NORMAL);
		}

		force.setVelocity(newVelocity);

		return magnitude;
	}

	public double getVectorAngle() {
		return force.getVectorAngle();
	}

	public boolean isExhaused() {
		return isExhausted;
	}

	public void kill() {
		this.isExhausted = true;
	}

	public CollisionListener getCollisionListener() {
		return collisionListener;
	}

	public void setOnCollision(CollisionListener onCollision) {
		this.collisionListener = onCollision;
	}

	public boolean hasCollisionListener() {
		return this.collisionListener != null ? true : false;
	}
}