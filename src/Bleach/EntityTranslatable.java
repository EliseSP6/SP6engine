package Bleach;

import java.awt.geom.Point2D;

public interface EntityTranslatable {

	public Point2D.Double getPosition();

	public void setPosition(Point2D.Double position);

	public double getRadius();

	public void setVectorAngle(double vectorAngle);

	public double getVectorAngle();

	public void setVelocity(double velocity);

	public double getVelocity();

	public void setMass(double mass);

	public double getMass();
}
