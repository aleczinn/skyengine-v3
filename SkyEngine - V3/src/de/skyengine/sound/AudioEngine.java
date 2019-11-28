package de.skyengine.sound;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import javax.sound.sampled.LineUnavailableException;

import de.skyengine.core.internal.IDisposable;
import de.skyengine.utils.logging.Logger;

public class AudioEngine implements IDisposable {

	private Logger logger = Logger.getLogger(AudioEngine.class.getName());
	
	
	private AudioMusicPlayback music;
	private final Collection<AudioMusicPlayback> allMusic = ConcurrentHashMap.newKeySet();
	private final Collection<AudioSFXPlayback> sounds = ConcurrentHashMap.newKeySet();
	
	private float sfxVolume = 1.0F;
	private float musicVolume = 1.0F;
	
	public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(new ThreadFactory() {
		private int id = 0;

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "Audio Thread " + id++);
		}
	});
	
	/**
	 * Gets the "main" music that is playing. This usually means the last call to
	 * {@code playMusic}, though if the music has been stopped it will be
	 * {@code null}.
	 *
	 * @return The main music, which could be {@code null}.
	 */
	public synchronized AudioMusicPlayback getMusic() {
		return this.music;
	}

	public synchronized Collection<AudioMusicPlayback> getAllMusic() {
		return Collections.unmodifiableCollection(this.allMusic);
	}
	
	public AudioMusicPlayback playMusic(Audio music) {
		return playMusic(new AudioLoop(music));
	}

	/**
	 * Sets the currently playing track to the specified track. This has no effect
	 * if the specified track is already playing.
	 *
	 * @param track The track to play
	 * @return The playback of the music
	 */
	public AudioMusicPlayback playMusic(Track track) {
		return playMusic(track, null, false, true);
	}
	
	/**
	 * Sets the currently playing track to the specified track.
	 *
	 * @param track   The track to play
	 * @param restart Whether to restart if the specified track is already playing,
	 *                determined by {@link Object#equals(Object)}
	 * @return The playback of the music
	 */
	public AudioMusicPlayback playMusic(Track track, boolean restart) {
		return playMusic(track, null, restart, true);
	}
	
	/**
	 * Plays the specified track.
	 *
	 * @param track   The track to play
	 * @param restart Whether to restart if the specified track is already playing,
	 *                determined by {@link Object#equals(Object)}
	 * @param stop    Whether to stop an existing track if present
	 * @return The playback of the music
	 */
	public AudioMusicPlayback playMusic(Track track, boolean restart, boolean stop) {
		return playMusic(track, null, restart, stop);
	}
	
	/**
	 * Plays the specified track, optionally configuring it before starting.
	 *
	 * @param track   The track to play
	 * @param config  A call to configure the playback prior to starting, which can
	 *                be {@code null}
	 * @param restart Whether to restart if the specified track is already playing,
	 *                determined by {@link Object#equals(Object)}
	 * @param stop    Whether to stop an existing track if present
	 * @return The playback of the music
	 */
	public synchronized AudioMusicPlayback playMusic(Track track, Consumer<? super AudioMusicPlayback> config, boolean restart, boolean stop) {
		if (!restart && music != null && music.isPlaying() && music.getTrack().equals(track)) {
			return music;
		}
		AudioMusicPlayback playback;
		try {
			playback = new AudioMusicPlayback(track);
		} catch (LineUnavailableException e) {
			this.logger.error("Could not open a line!", e);
			return null;
		}
		if (config != null) {
			config.accept(playback);
		}
		if (stop) {
			stopMusic();
		}
		this.allMusic.add(playback);
		playback.start();
		music = playback;
		return playback;
	}
	
	public AudioSFXPlayback playSound(Audio sound) {
		return this.playSound(sound, false);
	}
	
	public AudioSFXPlayback playSound(Audio sound, boolean loop) {
		if(sound == null) return null;
		
		try {
			AudioSFXPlayback sfx = new AudioSFXPlayback(sound, loop);
			sfx.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Stops the playback of the current background music.
	 */
	public synchronized void stopMusic() {
		for(AudioMusicPlayback track : this.allMusic) {
			track.cancel();
		}
	}
	
	public void addSound(AudioSFXPlayback playback) {
		this.sounds.add(playback);
	}
	
	public float getSFXVolume() {
		return sfxVolume;
	}
	
	public void setSFXVolume(float volume) {
		this.sfxVolume = volume;
	}
	
	public float getMusicVolume() {
		return musicVolume;
	}
	
	public void setMusicVolume(float volume) {
		this.musicVolume = volume;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	@Override
	public void dispose() {
		if(this.music != null && this.music.isPlaying()) {
			this.music.cancel();
			this.music = null;
		}

		EXECUTOR.shutdown();
		synchronized (this.sounds) {
			for(AudioSFXPlayback playback : this.sounds) {
				playback.cancel();
			}
			this.sounds.clear();
		}
	}
}
