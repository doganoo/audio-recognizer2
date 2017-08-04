package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import model.DataPoint;
import model.Song;
import ressources.Database;

public class AudioMatcher {
	private ArrayList<DataPoint> dataPoints = null;
	private Database database = null;
	private DataPoint matchedRecordPoint = new DataPoint();

	/**
	 * AudioMatcher constructor gets the records and matches them, after
	 * selecting the data from the database. The constructor stores the resulted
	 * Song in a variable, which is accessible by a getter method.
	 * 
	 * @param recordPoints
	 */
	public AudioMatcher(ArrayList<DataPoint> recordPoints) {
		this.dataPoints = recordPoints;
		this.database = new Database();
		this.matchSongs();
	}

	/**
	 * to avoid the using of the standard constructor
	 */
	@SuppressWarnings("unused")
	private AudioMatcher() {

	}

	/**
	 * matches the given hashes against the database to retrieve the best
	 * matched song. After all possible hashes are selected and stored together
	 * in a HashMap, the song with the most matches will be stored in a object
	 * to return it to the caller.
	 * 
	 * @return
	 */
	private void matchSongs() {
		HashMap<Integer, DataPoint> resultMap = new HashMap<Integer, DataPoint>();
		int dataPointsSize = dataPoints.size();
		/*
		 * iterating over all recorded hashes. The result of this loop is a
		 * HashMap, which contains the offset of the recorded and matched songs
		 * and a DataPoint object with the relating song. The offset is the
		 * absolute difference of the recorded songs time and the matched songs
		 * time.
		 */
		for (int i = 0; i < dataPointsSize; i++) {
			DataPoint dataPoint = dataPoints.get(i);
			long recordedHash = dataPoint.getHash();
			int recordedTime = dataPoint.getTime();
			ArrayList<DataPoint> matchedHashes = database.getSongsByHash(recordedHash);

			/*
			 * iterating over all matched hashes to the actual recorded hash. If
			 * there is a entry in the HashMap, the counter will be incremented.
			 * Otherwise a new entry with count = 1 is created.
			 */
			int matchedHashesSize = matchedHashes.size();
			for (int j = 0; j < matchedHashesSize; j++) {
				DataPoint matchedHash = matchedHashes.get(j);
				int matchedTime = matchedHash.getTime();
				int matchedSongId = matchedHash.getSongId();
				int offset = Math.abs(recordedTime - matchedTime);
				if (resultMap.get(matchedSongId) == null) {
					DataPoint rp = new DataPoint();
					rp.setCount(1);
					rp.setOffset(offset);
					rp.setSongId(matchedSongId);
					resultMap.put(matchedSongId, rp);
				} else {
					DataPoint rp = resultMap.get(matchedSongId);
					int newCount = rp.getCount() + 1;
					rp.setCount(newCount);
					resultMap.put(matchedSongId, rp);
				}
			}
		}
		/*
		 * looking for the DataPoint object with the highest count, which is the
		 * best matched song.
		 */
		DataPoint dataPoint = new DataPoint();
		for (Entry<Integer, DataPoint> entry : resultMap.entrySet()) {
			DataPoint recordPoint = entry.getValue();
			if (recordPoint.getCount() > dataPoint.getCount()) {
				dataPoint = recordPoint;
			}
			System.out.println(recordPoint.getSongId() + ": " + recordPoint.getCount());
		}
		this.matchedRecordPoint = dataPoint;
	}

	/**
	 * returns the matched Song object
	 * 
	 * @return song
	 */
	public Song getMatchedSong() {
		int songId = this.matchedRecordPoint.getSongId();
		Song song = this.database.getSongById(songId);
		song.setCount(this.matchedRecordPoint.getCount());
		return song;
	}

}
