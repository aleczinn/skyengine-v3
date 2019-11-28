package de.skyengine.sound;

import javax.sound.sampled.LineUnavailableException;

import de.skyengine.core.SkyEngine;

public class AudioMusicPlayback extends AudioPlayback {
	
	private Track track;
	private AudioVolume musicVolume;
	
	public AudioMusicPlayback(Track track) throws LineUnavailableException {
		super(track.getFormat());
		this.track = track;
		this.musicVolume = this.createVolumeControl();
		this.musicVolume.setValue(SkyEngine.sound().getMusicVolume());
	}

	@Override
	public void run() {
		for(Audio sound : this.track) {
			if(this.play(sound)) {
				return;
			}
		}
		this.finish();
	}
	
	public Track getTrack() {
		return track;
	}
	
	public void setMusicVolume(float volume) {
		this.musicVolume.setValue(volume);
	}
}
