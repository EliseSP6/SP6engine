package Bleach;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sprite {

	protected BufferedImage image;
	protected int width;
	protected int height;

	public Sprite(BufferedImage image, Integer width, Integer height) {
		/* Make a new image with the specified width and height. width and height could be null as we can get data from a JSON parser. */
		this.width = width == null ? image.getWidth() : width;
		this.height = height == null ? image.getHeight() : height;
		
		this.image = new BufferedImage(this.width, this.height, image.getType());
		Graphics g = this.image.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}
	
	public Sprite(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		this.image = image;
	}

	public BufferedImage getFrame() {
		return image;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
