package interfaces;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.TargetDataLine;

public interface Audio {
	public static final int MICROPHONE = 1;
	public static final int MP3 = 2;

	public TargetDataLine getTargetDataLine();

	public AudioFormat getAudioFormat();

	public AudioInputStream getAudioInputStream() throws NullPointerException;

	public int getMode();

	public long getDurationInSeconds();
}
