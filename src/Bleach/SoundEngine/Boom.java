package Bleach.SoundEngine;

import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

import Bleach.Loader.Discette;

public class Boom {

	public static void playSound(String soundID) throws LineUnavailableException, IOException, InterruptedException {

		AudioListener listener = new AudioListener();
		Clip clip = AudioSystem.getClip();
		clip.open(Discette.getSound(soundID));
		clip.addLineListener(listener);
		try {
			clip.start();
			listener.waitUntilDone();
		} finally {
			clip.close();
		}
	}

	static class AudioListener implements LineListener {
		private boolean done = false;

		@Override
		public synchronized void update(LineEvent event) {
			Type eventType = event.getType();
			if (eventType == Type.STOP || eventType == Type.CLOSE) {
				done = true;
				notifyAll();
			}
		}

		public synchronized void waitUntilDone() throws InterruptedException {
			while (!done) {
				wait();
			}
		}
	}
}
