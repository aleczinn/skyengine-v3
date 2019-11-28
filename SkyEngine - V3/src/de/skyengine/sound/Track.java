package de.skyengine.sound;

import javax.sound.sampled.AudioFormat;

public interface Track extends Iterable<Audio> {
	
	public AudioFormat getFormat();
}
