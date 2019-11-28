package de.skyengine.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.skyengine.core.SkyEngine;
import de.skyengine.utils.StreamUtilities;

public class Audio {

	private String name;
	private AudioFormat format;
	private AudioInputStream audioStream;
	
	private byte[] data;
	private byte[] streamData;
	
	/**
	 *	Creates a new Sound instance by the specified file path. Loads the sound data
	 * into a byte array and also retrieves information about the format of the
	 * sound file.
	 * 
	 * @param name
	 * @param stream
	 */
	public Audio(String name, InputStream stream) {
		this.name = name;
		
		this.data = StreamUtilities.getBytes(stream);
		
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(stream);
			if(inputStream != null) {
				AudioFormat baseFormat = inputStream.getFormat();
				AudioFormat decodeFormat = this.getOutFormat(baseFormat);
				
				inputStream = AudioSystem.getAudioInputStream(decodeFormat, inputStream);
				this.audioStream = inputStream;
				this.streamData = StreamUtilities.getBytes(this.audioStream);
				this.format = this.audioStream.getFormat();
			}
			stream.close();
			SkyEngine.sound().getLogger().info("Sound " + this.name + " initialized.");
		} catch (IOException e) {
			SkyEngine.sound().getLogger().fatal("Sound - Sound could not be found! - [" + this.name + "]");
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			SkyEngine.sound().getLogger().fatal("Sound - Unsupported Audioformat! - [" + this.name + "]");
			e.printStackTrace();
		}
	}
	
	public Audio(String name, File file) throws FileNotFoundException {
		this(name, new BufferedInputStream(new FileInputStream(file)));
	}
	
	public Audio(String name, String path) throws FileNotFoundException {
		this(name, new BufferedInputStream(new FileInputStream(new File(path))));
	}
	
	public String getName() {
		return name;
	}
	
	public AudioFormat getFormat() {
		return format;
	}
	
	public AudioInputStream getAudioStream() {
		return audioStream;
	}
	
	public byte[] getRawData() {
		return data;
	}
	
	public byte[] getStreamData() {
		if(this.streamData == null) {
			return new byte[0];
		}
		return this.streamData.clone();
	}
	
	private AudioFormat getOutFormat(AudioFormat inFormat) {
		int ch = inFormat.getChannels();
		float rate = inFormat.getSampleRate();
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
	}
}
