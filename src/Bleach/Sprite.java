package Bleach;

import java.awt.image.BufferedImage;

public class Sprite {

	private BufferedImage image;
	
	Sprite(BufferedImage image){
		this.image = image;
	}
	
	public BufferedImage getFrame(){
		return image;
	}
}
