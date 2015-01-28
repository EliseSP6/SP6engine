package Bleach.Loader;

import java.awt.image.BufferedImage;
import java.util.Map;

public class Discette {
	static Map<String, BufferedImage> images;

	static void loadImages(String pathToJSON) {
		// TODO
	}

	static BufferedImage getImage(String code) {
		return images.get(code);
	}
}
