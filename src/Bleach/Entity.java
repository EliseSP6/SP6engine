package Bleach;

import java.awt.geom.Point2D;

public class Entity implements EntityTranslatable {
	protected Sprite sprite;
	protected double x, y;
	protected final double r;

	protected double vectorAngle = 0;
	protected double velocity = 0;
	protected double mass = 0;

	protected long timePreviousTick;
	protected boolean bMoving; // Is the entity currently moving?

	protected Entity(Sprite sprite, double x, double y, double r) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.r = r;
		timePreviousTick = System.nanoTime();
		bMoving = false;
	}

	@Override
	public Point2D.Double getPosition() {
		return new Point2D.Double(x, y);
	}

	@Override
	public void setPosition(Point2D.Double position) {
		this.x = position.x;
		this.y = position.y;
	}

	@Override
	public double getRadius() {
		return r;
	}

	@Override
	public void setVectorAngle(double vectorAngle) {
		this.vectorAngle = vectorAngle;
	}

	@Override
	public double getVectorAngle() {
		return vectorAngle;
	}

	@Override
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	@Override
	public double getVelocity() {
		return velocity;
	}

	@Override
	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	public double getMass() {
		return mass;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public boolean isMoving() {
		return bMoving;
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
