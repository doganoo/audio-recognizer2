package core;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.sampled.convert.PCM2PCMConversionProvider;

import interfaces.Audio;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

public class MP3Audio implements Audio {
	private String filePath = null;
	private AudioFormat format = null;
	private TargetDataLine line = null;
	private AudioInputStream audioInputStream = null;
	private boolean supportsProcessing = true;

	public MP3Audio(String filePath) {
		this.filePath = filePath;
		try {
			this.initializeTargetDataLine();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private MP3Audio() {
	}

	private AudioFormat getHumanHearableFormat() {
		float sampleRate = 44100;
		int sampleSizeInBits = 8;
		int channels = 1; // mono
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	private void initializeTargetDataLine()
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		PCM2PCMConversionProvider conversionProvider = new PCM2PCMConversionProvider();
		try {
			File file = new File(filePath);
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
					baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
			if (!conversionProvider.isConversionSupported(this.getHumanHearableFormat(), decodedFormat)) {
				System.out.println("converson not supported");
				this.setProcessable(false);
			}

			try {
				audioInputStream = conversionProvider.getAudioInputStream(this.getHumanHearableFormat(), din);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				this.setProcessable(false);
			}
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, decodedFormat);
			TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			this.format = decodedFormat;
			this.line = line;
		} catch (UnsupportedAudioFileException e) {
			this.setProcessable(false);
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (IOException e) {
			this.setProcessable(false);
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	@Override
	public TargetDataLine getTargetDataLine() {
		return this.line;
	}

	@Override
	public AudioFormat getAudioFormat() {
		return this.format;
	}

	@Override
	public AudioInputStream getAudioInputStream() throws NullPointerException {
		if (this.audioInputStream != null) {
			return this.audioInputStream;
		} else {
			throw new NullPointerException();
		}
	}

	@Override
	public int getMode() {
		return Audio.MP3;
	}

	public void setProcessable(boolean supportsProcessing) {
		this.supportsProcessing = supportsProcessing;
	}

	public boolean isProcessable() {
		return supportsProcessing;
	}

	/**
	 * TODO: Eerklaerung, warum diese Funktion so isoloert hier steht
	 * 
	 * @return
	 */
	@Override
	public long getDurationInSeconds() {
		long duration = -1;
		try {
			AudioFileFormat baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(new File(filePath));
			Map<String, Object> properties = baseFileFormat.properties();
			duration = (Long) properties.get("duration");
			duration = duration / 1000000;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		return duration;
	}

}
