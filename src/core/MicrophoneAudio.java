package core;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import interfaces.Audio;

public class MicrophoneAudio implements Audio {
	private TargetDataLine line = null;
	private AudioFormat format = null;

	/**
	 * initializes the audio format and the target data line.
	 */
	public MicrophoneAudio() {
		this.initializeAudioFormat();
		this.initializeTargetDataLine();
	}

	/**
	 * creates the TargetDataLine object
	 */
	private void initializeTargetDataLine() {
		try {
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, this.format);
			TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			this.line = line;
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * initializes the AudioFormat object. AudioFormat specifies a particular
	 * arrangement of data in a soundsystem. This function creates a AudioFormat
	 * for human hearable frequency ranges.
	 */
	private void initializeAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeInBits = 8;
		int channels = 1; // mono
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		this.format = format;
	}

	/**
	 * returns the TargetDataLine object
	 */
	@Override
	public TargetDataLine getTargetDataLine() {
		return this.line;
	}

	/**
	 * returns the AudioFormat object
	 */
	@Override
	public AudioFormat getAudioFormat() {
		return this.format;
	}

	/**
	 * returns the AudioInputStream object. IMPORTANT!! Do not call this
	 * function because it is useless in this context! If you wish to read data
	 * from a file please use the MP3Audio class instead!
	 */

	@Override
	public AudioInputStream getAudioInputStream() throws NullPointerException {
		throw new NullPointerException(
				"you cannot call this method in class MicrophoneAudio, please switch to MP3Audio");
	}

	/**
	 * returns the mode of this class (microphone).
	 */
	@Override
	public int getMode() {
		return Audio.MICROPHONE;
	}

	/**
	 * defines the total duration of a recording. Because this class handles
	 * data retrieved from the microphone, it is not possible to define the
	 * duration. This is the reason why this function will always return 0.
	 */
	@Override
	public long getDurationInSeconds() {
		return 0;
	}

}
