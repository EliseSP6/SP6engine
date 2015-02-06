package Bleach;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import Bleach.PhysicsEngine.Physique.CollisionListener;
import Bleach.PhysicsEngine.Physique.ExternalForce;
import Bleach.PhysicsEngine.Physique.Force;

public interface EntityTranslatable {

	public void addExternalForce(ExternalForce externalForce);

	public Rectangle2D.Double getBoundary();

	public CollisionListener getCollisionListener();

	public List<ExternalForce> getExternalForces();

	public Force getForce();

	public double getMass();
	
	public double getWeight();
	public void setWeight(double weight);

	public Point2D.Double getPosition();

	public double getRadius();

	public boolean isLanded();

	public boolean isMoving();

	public void isMoving(boolean setMoving);

	public void setLanded(boolean isLanded);

	public void setMass(double mass);

	public void setOnCollision(CollisionListener onCollision);

	public void setPosition(Point2D.Double position);

}
