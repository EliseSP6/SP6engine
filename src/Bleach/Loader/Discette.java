package Bleach.Loader;

import java.util.Map;

import Bleach.Sprite;

public class Discette {
	static Map<String, Sprite> images;

	static void loadImages(String pathToJSON) {
		// TODO
	}

	static Sprite getImage(String code) {
		return images.get(code);
	}
}
