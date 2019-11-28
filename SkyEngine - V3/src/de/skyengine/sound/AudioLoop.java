package de.skyengine.sound;

import java.util.Iterator;
import java.util.Objects;

import javax.sound.sampled.AudioFormat;

public class AudioLoop  implements Track, Iterator<Audio> {

	private Audio sound;
	
	public AudioLoop(Audio sound) {
		Objects.requireNonNull(sound);
		this.sound = sound;
	}
	
	@Override
	public Iterator<Audio> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Audio next() {
		return this.sound;
	}

	@Override
	public AudioFormat getFormat() {
		return this.sound.getFormat();
	} 
	
	@Override
	public boolean equals(Object anObject) {
		return this == anObject || anObject instanceof AudioLoop && ((AudioLoop) anObject).sound.equals(this.sound);
	}
}
