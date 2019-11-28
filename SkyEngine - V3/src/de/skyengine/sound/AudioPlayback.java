package de.skyengine.sound;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public abstract class AudioPlayback implements Runnable {

	protected final SourceDataLine line;
	private FloatControl gainControl;
	private BooleanControl muteControl;
	
	private boolean started = false;
	private volatile boolean cancelled = false;
	
	private AudioVolume masterVolume;
	private final Collection<AudioVolume> volumeControls = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
	private volatile AtomicInteger miscVolume = new AtomicInteger(0x3f800000); // floatToIntBits(1f)
	
	public AudioPlayback(AudioFormat format) throws LineUnavailableException {
		this.line = AudioSystem.getSourceDataLine(format);
		this.line.open();
		this.line.start();
		
		this.gainControl = (FloatControl) this.line.getControl(FloatControl.Type.MASTER_GAIN);
		this.muteControl = (BooleanControl) this.line.getControl(BooleanControl.Type.MUTE);
		this.masterVolume = this.createVolumeControl();
	}

	/**
	 * Starts playing the audio.
	 *
	 * @throws IllegalStateException if the audio has already been started
	 */
	public synchronized void start() {
		if (this.started) {
			throw new IllegalStateException("already started");
		}
		this.play();
		this.started = true;
	}
	
	protected void play() {
		AudioEngine.EXECUTOR.submit(this);
	}
	
	/**
	 * Plays a sound to this object's data line.
	 *
	 * @param sound The sound to play
	 * @return Whether the sound was cancelled while playing
	 */
	protected boolean play(Audio sound) {
		byte[] data = sound.getStreamData();
		int len = this.line.getFormat().getFrameSize();
		len = (this.line.getBufferSize() / len / 2 + 1) * len;
		for (int i = 0; i < data.length; i += this.line.write(data, i, Math.min(len, data.length - i))) {
			if (this.cancelled) {
				return true;
			}
		}
		return this.cancelled;
	}
	
	/**
	 * Sets the paused state of this playback to the provided value.
	 * 
	 * @param paused Whether to pause or resume this playback
	 */
	public void setPaused(boolean paused) {
		if (paused) {
			this.pausePlayback();
		} else {
			this.resumePlayback();
		}
	}
	
	/**
	 * Pauses this playback. If this playback is already paused, this call has no
	 * effect.
	 */
	public void pausePlayback() {
		if (this.line.isOpen()) {
			this.line.stop();
		}
	}

	/**
	 * Resumes this playback. If this playback is already playing, this call has no
	 * effect.
	 */
	public void resumePlayback() {
		if (this.line.isOpen()) {
			this.line.start();
		}
	}
	
	/**
	 * Determines if this playback is paused.
	 * 
	 * @return Whether this playback is paused
	 */
	public boolean isPaused() {
		return !this.line.isActive();
	}

	/**
	 * Determines if this playback has sound to play. If it is paused but still in
	 * the middle of playback, it will return {@code true}, but it will return
	 * {@code false} if it has finished or it has been cancelled.
	 * 
	 * @return Whether this playback has sound to play
	 */
	public boolean isPlaying() {
		return this.line.isOpen();
	}

	/**
	 * Attempts to cancel the playback of this audio. If the playback was
	 * successfully cancelled, it will notify listeners.
	 */
	public synchronized void cancel() {
		if (!this.started) {
			throw new IllegalStateException("not started");
		}
		if (!this.cancelled && this.line.isOpen()) {
			this.line.stop();
			this.cancelled = true;
			this.line.flush();
			this.line.close();
		}
	}
	
	/**
	 * Gets the current volume of this playback, considering all
	 * {@code VolumeControl} objects created for it.
	 * 
	 * @return The volume
	 */
	public float getMasterVolume() {
		if (this.muteControl.getValue()) {
			return 0f;
		}
		return (float) Math.pow(10.0, this.gainControl.getValue() / 20.0);
	}
	
	/**
	 * Gets the current master volume of this playback. This will be approximately
	 * equal to the value set by a previous call to {@code setVolume}, though
	 * rounding errors may occur.
	 * 
	 * @return The settable volume
	 */
	public float getVolume() {
		return this.masterVolume.getValue();
	}

	/**
	 * Sets the master volume of this playback.
	 * 
	 * @param volume The new volume
	 */
	public void setVolume(float volume) {
		this.masterVolume.setValue(volume);
	}
	
	public AudioVolume getMasterVolumeControl() {
		return this.masterVolume;
	}
	
	public AudioVolume createVolumeControl() {
		AudioVolume control = new AudioVolume();
		this.volumeControls.add(control);
		return control;
	}
	
	public void updateVolume() {
		synchronized (this.volumeControls) {
			float volume = Float.intBitsToFloat(this.miscVolume.get());
			for (AudioVolume control : this.volumeControls) {
				volume *= control.getValue();
			}
			float dbGain = (float) (20.0 * Math.log10(volume));
			if (dbGain < this.gainControl.getMinimum()) {
				this.muteControl.setValue(true);
			} else {
				this.gainControl.setValue(dbGain);
				this.muteControl.setValue(false);
			}
		}
	}
	
	/**
	 * Finishes the playback. If this playback was not cancelled in the process, it
	 * will notify listeners.
	 */
	protected void finish() {
		this.line.drain();
		synchronized (this) {
			this.line.close();
		}
	}
	
	public class AudioVolume {
		private volatile float value = 1.0F;
		
		public void setValue(float value) {
			if(value < 0.0F) {
				value = 0.0F;
			}
			this.value = value;
			AudioPlayback.this.updateVolume();
		}
		
		public float getValue() {
			return value;
		}
		
		@Override
		protected void finalize() {
			AudioPlayback.this.miscVolume.accumulateAndGet(Float.floatToRawIntBits(this.value), (a, b) -> Float.floatToRawIntBits(Float.intBitsToFloat(a) * Float.intBitsToFloat(b)));
		}
	}
}
