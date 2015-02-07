package Bleach.SoundEngine;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

public class Boom {

	private static Sound ambientSoundtrack = null;

	public static Sound getAmbientSoundtrack() {
		return ambientSoundtrack;
	}

	public static void setAmbientSoundtrack(Sound ambientSoundtrack) throws LineUnavailableException {
		Boom.ambientSoundtrack = ambientSoundtrack;
		Clip ambientSoundtrackClip = createClip(ambientSoundtrack);
		ambientSoundtrackClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public static void playSound(Sound sound) throws LineUnavailableException {

		Clip clip = createClip(sound);
		clip.start();
	}
	
	private static Clip createClip(Sound sound) throws LineUnavailableException {
		Clip clip = (Clip) AudioSystem.getLine((DataLine.Info) sound.getInfo());
		clip.open((AudioFormat) sound.getAudioInputStream().getFormat(), (byte[])sound.getAudioData(), 0, sound.getSize());
		return clip;
	}
}
