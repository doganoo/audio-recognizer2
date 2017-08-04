package core;

import java.util.ArrayList;

import model.DataPoint;

public class Spectrum {
	private byte[] audioData = null;
	private Complex[][] results = null;
	private double highscores[][];
	private double recordPoints[][];
	private long points[][];
	private final int UPPER_LIMIT = 300;
	private final int LOWER_LIMIT = 40;
	private final int[] RANGE = new int[] { 40, 80, 120, 180, UPPER_LIMIT + 1 };
	private static final int FUZ_FACTOR = 2;
	private ArrayList<DataPoint> recordPointz = new ArrayList<DataPoint>();
	private int songId = -1;

	public Spectrum(byte[] byteArray, int songId) {
		this.audioData = byteArray;
		this.makeSpectrum();
		this.initializeArrays();
		this.determineKeyPoints();
		this.songId = songId;
	}

	private void makeSpectrum() {
		final int audioDataSize = audioData.length;
		int possibleChunks = audioDataSize / 4096;
		Complex[][] results = new Complex[possibleChunks][];
		for (int times = 0; times < possibleChunks; times++) {
			Complex[] complex = new Complex[4096];
			for (int i = 0; i < 4096; i++) {
				// TODO: is this really a frequence??
				double frequence = this.audioData[(times * 4096) + i];
				complex[i] = new Complex(frequence, 0);
			}
			results[times] = FFT.fft(complex);
		}
		this.results = results;
	}

	private void determineKeyPoints() {
		final int resultLength = results.length;
		for (int time = 0; time < resultLength; time++) {
			for (int freq = LOWER_LIMIT; freq < UPPER_LIMIT - 1; freq++) {
				Complex c = results[time][freq];
				// Get the magnitude:
				double mag = Math.log(c.abs() + 1);
				// Find out which range we are in:
				int index = getIndex(freq);

				// Save the highest magnitude and corresponding frequency:
				if (mag > highscores[time][index]) {
					highscores[time][index] = mag;
					recordPoints[time][freq] = 1;
					points[time][index] = freq;
				}
			}
			long p1 = points[time][0];
			long p2 = points[time][1];
			long p3 = points[time][2];
			long p4 = points[time][3];
			long hash = hash(p1, p2, p3, p4);
			// Logger.logDebug("Spectrum", hash + ";" + time);
			DataPoint rp = new DataPoint();
			rp.setHash(hash);
			rp.setTime(time);
			rp.setSongId(this.songId);
			this.recordPointz.add(rp);
		}

	}

	public ArrayList<DataPoint> getRecordPoints() {
		return this.recordPointz;
	}

	private long hash(long p1, long p2, long p3, long p4) {
		// return (p4 - (p4 % FUZ_FACTOR)) * 100000000 + (p3 - (p3 %
		// FUZ_FACTOR)) * 100000 + (p2 - (p2 % FUZ_FACTOR)) * 100
		// + (p1 - (p1 % FUZ_FACTOR));
		return p4 * 100000000 + p3 * 100000 + p2 * 100 + p1;
	}

	public int getIndex(int freq) {
		int i = 0;
		while (RANGE[i] < freq) {
			i++;
		}
		return i;
	}

	private void initializeArrays() {
		highscores = new double[results.length][5];
		for (int i = 0; i < results.length; i++) {
			for (int j = 0; j < 5; j++) {
				highscores[i][j] = 0;
			}
		}

		recordPoints = new double[results.length][UPPER_LIMIT];
		for (int i = 0; i < results.length; i++) {
			for (int j = 0; j < UPPER_LIMIT; j++) {
				recordPoints[i][j] = 0;
			}
		}

		points = new long[results.length][5];
		for (int i = 0; i < results.length; i++) {
			for (int j = 0; j < 5; j++) {
				points[i][j] = 0;
			}
		}
	}
}
