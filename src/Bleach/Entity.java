package Bleach;

import java.awt.geom.Point2D;

public class Entity implements EntityTranslatable {
	Sprite sprite;
	double x, y;
	final double r;

	double vectorAngle = 0;
	double velocity = 0;
	
	long timePreviousTick;

	Entity(Sprite sprite, double x, double y, double r) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.r = r;
		timePreviousTick = System.nanoTime();
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

	public Sprite getSprite() {
		return sprite;
	}

	public void tick(LevelInteractable activeLevel) {
	} // For overriding in subclasses.
}
