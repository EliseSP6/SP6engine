package Bleach;
import java.awt.geom.Point2D;

public interface EntityTranslatable {

	public Point2D.Double getPosition();

	public void setPosition(Point2D.Double position);

	public double getRadius();
}
