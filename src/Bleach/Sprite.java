package Bleach;

import java.awt.image.BufferedImage;

public class Sprite {

	protected BufferedImage image;
	protected int width;
	protected int height;

	public Sprite(BufferedImage image, int width, int height) {
		this.width = width;
		this.height = height;
		this.image = new BufferedImage(width, height, image.getType());
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
