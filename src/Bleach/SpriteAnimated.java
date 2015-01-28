package Bleach;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SpriteAnimated extends Sprite{

	private long timeAnim, timeStart;
	private int frameCount;
	
	SpriteAnimated(BufferedImage image, int frameWidth, int frameHeight, long timeAnim){
		super(image);
		width = frameWidth;
		height = frameHeight;
		this.timeAnim = timeAnim;
		timeStart = System.nanoTime();
		frameCount = image.getWidth() / frameWidth;
		frameCount = frameCount == 0 ? 1 : frameCount;	// Minimum frame count should be one.
	}
	
	@Override
	public BufferedImage getFrame(){
		/* Calculate the index based on the current time */
		return getFrame((int)((System.nanoTime() - timeStart) / timeAnim) % frameCount);
	}
	
	public BufferedImage getFrame(int index){
		BufferedImage bi = new BufferedImage(width, height, image.getType());
		Graphics g = bi.getGraphics();
		int sourceX = (index % frameCount) * width;
		
		g.drawImage(image, 0, 0, width, height, sourceX, 0, sourceX + width, height, null);
		g.dispose();
		
		return bi;
	}
}
