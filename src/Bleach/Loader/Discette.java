package Bleach.Loader;

import java.util.Map;

import javax.sound.sampled.AudioInputStream;

import Bleach.Sprite;

public class Discette {
	private static Map<String, Sprite> images;
	private static Map<String, AudioInputStream> sounds;

	public static void loadImage(String pathToJSON) {
		// TODO
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
}
