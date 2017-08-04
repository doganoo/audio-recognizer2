package core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import interfaces.Audio;
import view.AudioRecognizerView;

public class AudioRecognizer {
	private TargetDataLine line = null;
	private AudioFormat format = null;
	private int mode = -1;
	private AudioInputStream audioInputStream = null;
	private int NUMBER_OF_SECONDS_TO_RECORD = 0;

	/**
	 * initializes the audio type (microphone or mp3) for processing the
	 * retrieved data. It is required to pass a MP3Audio or a MicrophoneAudio
	 * object, otherwise the function will throw a IllegalArgumentException.
	 * This function defines also the total duration of recording. A microphone
	 * recording has a different amount of seconds then a mp3 recording.
	 * 
	 * @param audio
	 * @return
	 */
	public byte[] start(Audio audio) {
		this.line = audio.getTargetDataLine();
		this.format = audio.getAudioFormat();
		this.mode = audio.getMode();
		// determining the recording type
		if (this.mode == Audio.MP3) {
			// if this is a MP3 recording, the whole file
			// will be matched
			try {
				this.audioInputStream = audio.getAudioInputStream();
				this.NUMBER_OF_SECONDS_TO_RECORD = Math.round(audio.getDurationInSeconds());
				// this.NUMBER_OF_SECONDS_TO_RECORD = 2;
			} catch (NullPointerException npe) {
				// actually the audioInputStream object can only
				// be set by MP3Audio class
				npe.printStackTrace();
			}
		} else if (this.mode == Audio.MICROPHONE) {
			// if this is a live recording, the software
			// will only record the first 3 seconds
			this.audioInputStream = null;
			this.NUMBER_OF_SECONDS_TO_RECORD = 10;
		}
		ByteArrayOutputStream byteArrayOutputStream = this.listenSound();
		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * This function reads the audio data as a byte array from the audio object.
	 * Related to the recording type the data will be retrieved from different
	 * objects and written to a byte array. The process of reading the data is
	 * implemented in a thread.
	 * 
	 * @return
	 */
	private ByteArrayOutputStream listenSound() {
		ByteArrayOutputStream byteArrayOutputStream = null;
		// the line has to be opened if the recording is from the microphone
		if (this.mode == Audio.MICROPHONE) {
			try {
				line.open(format);
				line.start();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		/*
		 * creating the thread. The Callable object allows to get a value back
		 * from the thread. The thread reads the data out of the recording. The
		 * amount of seconds is defined in the NUMBER_OF_SECONDS_TO_RECORD
		 * variable of this class.
		 */
		Callable<ByteArrayOutputStream> callable = new Callable<ByteArrayOutputStream>() {

			@Override
			public ByteArrayOutputStream call() throws Exception {
				AudioRecognizerView av = AudioRecognizerView.getInstance();
				av.setActionText("recording...");
				System.out.println("recording ....");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int delta = 0;
				byte[] buffer = new byte[(int) 1024];
				long start = System.currentTimeMillis();
				int count = 0;
				try {
					while (delta < NUMBER_OF_SECONDS_TO_RECORD) {
						long actual = System.currentTimeMillis();
						delta = (int) (actual - start) / 1000;
						if (mode == Audio.MICROPHONE) {
							count = line.read(buffer, 0, buffer.length);
						} else if (mode == Audio.MP3) {
							count = audioInputStream.read(buffer, 0, buffer.length);
						}
						// if there is data
						if (count > 0) {
							out.write(buffer, 0, count);
						}
					}
					out.close();
					line.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("finished");
				av.setActionText("finished...");
				return out;
			}
		};
		// calling the thread (Callable)
		try {
			byteArrayOutputStream = callable.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteArrayOutputStream;
	}
}