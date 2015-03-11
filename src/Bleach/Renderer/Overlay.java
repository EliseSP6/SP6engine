package Bleach.Renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Overlay {

	private long timeStart, timeFadein, timeFadeout, timeToLive;
	private BufferedImage image;
	
	public Overlay(BufferedImage overlay, int timeFadein, int timeFadeout, int timeToLive){
		this.timeFadein = timeFadein;
		this.timeFadeout = timeFadeout;
		this.timeToLive = timeToLive;
		timeStart = System.currentTimeMillis();
		image = overlay;
	}
	
	public void doExit(){
		timeToLive = (int)(System.currentTimeMillis() - timeStart + timeFadeout);
	}
	
	public BufferedImage getImage(){
		/*
		 * Returns the image with alpha depending on if we're fading in or not or fading out.
		 * */
		BufferedImage result = image;
		long now = System.currentTimeMillis();
		
		if(now - timeStart <= timeFadein){
			double fadeInValue = (now - timeStart) / timeFadein;
			return imageSetAlpha(result, fadeInValue);
		}else if(now - timeStart < (timeToLive - timeFadeout) || timeToLive == 0){
			return result;
		}else{
			double fadeOut = (1 - ((now - (timeStart + timeToLive - timeFadeout)) / (double) timeFadeout)) * timeFadein;
			return imageSetAlpha(result, fadeOut);
		}
	}
	
	private BufferedImage imageSetAlpha(BufferedImage image, double stage){
		/*
		 * Returns the image with alpha set to stage (double between 0 to 1)
		 * */
		BufferedImage resultImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		stage = stage < 0 ? 0 : (stage > 1 ? 1 : stage);
		//int p = (int) (255 * stage);
		
		for (int y = 0; y < resultImg.getHeight(); y++) {
		    for (int x = 0; x < resultImg.getWidth(); x++) {
		    	Color c = new Color(image.getRGB(x, y), true);
		    	Color alphaColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(c.getAlpha() * stage));
		    	resultImg.setRGB(x, y, alphaColor.getRGB());
	
		    }
		}
		
		return resultImg;
	}
}
