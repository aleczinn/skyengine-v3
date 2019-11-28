package de.skyengine.sound;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

import de.skyengine.core.SkyEngine;

public class AudioSFXPlayback extends AudioPlayback {

	private Audio sound;
	private boolean loop;
	
	private FloatControl panControl;
	private AudioVolume sfxVolume;
	
	public AudioSFXPlayback(Audio sound, boolean loop) throws LineUnavailableException {
		super(sound.getFormat());
		this.sound = sound;
		this.loop = loop;
		this.sfxVolume = this.createVolumeControl();
		this.sfxVolume.setValue(SkyEngine.sound().getSFXVolume());
	}

	@Override
	protected void play() {
		super.play();
		SkyEngine.sound().addSound(this);
	}
	
	@Override
	public void run() {
		do {
			if (this.play(this.sound)) {
				return;
			}
		} while (this.loop);
		this.finish();
	}
	
	public FloatControl getPanControl() {
		return panControl;
	}
}
