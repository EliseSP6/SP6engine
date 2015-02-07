package Bleach.SoundEngine;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;

public class Sound {
	private AudioInputStream audioInputStream;
	private int size;
	private DataLine.Info info;
	private byte[] audio;

	public Sound(AudioInputStream audioInputStream, int size, Info info, byte[] audioData) {
		this.audioInputStream = audioInputStream;
		this.size = size;
		this.info = info;
		this.audio = audioData;
	}
	
	public AudioInputStream getAudioInputStream() {
		return audioInputStream;
	}
	
	public int getSize() {
		return size;
	}
	
	public DataLine.Info getInfo() {
		return info;
	}
	
	public byte[] getAudioData() {
		return audio;
	}
}