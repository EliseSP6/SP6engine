package Bleach;

import java.awt.image.BufferedImage;

public class Sprite {

	protected BufferedImage image;
	protected int width;
	protected int height;
	
	Sprite(BufferedImage image){
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
	}
	
	public BufferedImage getFrame(){
		return image;
	}
}
