package Bleach.Loader;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;

import com.google.gson.Gson;

import Bleach.Sprite;

public class Discette {
	private static Map<String, Sprite> images;
	private static Map<String, AudioInputStream> sounds;

	public static void loadImage(String pathToJSON) {
		
		final class JsonImages{	// A class to match the json file structure.
			private String key;
			private String file;
			private int width;
			private int height;
		}
		
		JsonImages x;
		
		File file = new File(pathToJSON);
		FileReader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CharBuffer b = null;
		try {
			
			reader.read(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String txt = b.toString();
		
		Gson json = new Gson();
		
		x = json.fromJson(txt, JsonImages.class);
		
		
		Sprite s = new Sprite(imgLoader(x.file), x.width, x.height);
		images.put(x.key, s);
	}

	public static Sprite getImage(String imageID) {
		return images.get(imageID);
	}

	public static void loadSound(String pathToJSON) {
		// TODO
	}

	public static AudioInputStream getSound(String soundID) {
		return sounds.get(soundID);
	}
	
	private static BufferedImage imgLoader(String filename){
		/*
		 * Loads an image file and return it as a "BufferedImage".
		 * Optionally optimize it (which might mess up alpha channel on some systems).
		 * 
		 * */
		
		BufferedImage compatImg = null;
		
		try{
			return toCompatibleImage(ImageIO.read(new File(filename)));
		}catch(IOException e){
			System.err.println("[IMAGE] Error loading file: \"" + filename + "\" " + e);
		}
		
		return compatImg;
	}
	
	private static BufferedImage toCompatibleImage(BufferedImage image){
		/*
		 * Optimizes (set image color model) image so that it's compatible with the current system.
		 * This is an absolute must to get java 2D graphics to run smoothly.
		 *  
		 * */
		
		// obtain the current system graphical settings
		GraphicsConfiguration gfx_config = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getDefaultScreenDevice().
			getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system 
		 * settings, simply return it
		 */
		if (image.getColorModel().equals(gfx_config.getColorModel()))
			return image;

		// image is not optimized, so create a new image that is
		BufferedImage new_image = gfx_config.createCompatibleImage(
				image.getWidth(), image.getHeight(), image.getTransparency());

		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		// return the new optimized image
		return new_image; 
	}
}
