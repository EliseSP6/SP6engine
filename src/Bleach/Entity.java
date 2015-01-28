package Bleach;

import java.awt.geom.Point2D;

public class Entity implements EntityTranslatable {
	Sprite sprite;
	double x, y, r;

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
		// TODO Auto-generated method stub
		return 0;
	}

}
