package audiorecognizer2;

import java.util.ArrayList;

import core.AudioMatcher;
import core.AudioRecognizer;
import core.MicrophoneAudio;
import core.Spectrum;
import model.DataPoint;
import model.Song;
import ressources.Database;
import view.AudioRecognizerView;

public class Main {

	public static void main(String[] args) {
		Database database = new Database();

		// String absolutePath = "/Volumes/EXTERNEFESTPLATTE/Studium/BaSys
		// Master/Sprache1+2/SS16/Präsentation 2/sound/Alexandra Stan - Mr
		// Saxobeat.mp3";
		// Song baseSong = new Song();
		// baseSong.setAbsolutePath(absolutePath);
		// baseSong.setId(-10);
		// baseSong.setName(absolutePath);
		// MP3Audio audio = new MP3Audio(absolutePath);
		// if (!audio.isProcessable()) {
		// System.err.println("song cannot be processed");
		// System.exit(0);
		// }
		// AudioRecognizer audioRecognizer = new AudioRecognizer();
		// byte[] bytes = audioRecognizer.start(audio);
		// Spectrum spectrum = new Spectrum(bytes, baseSong.getId());
		// ArrayList<DataPoint> dataPoints = spectrum.getRecordPoints();
		// AudioMatcher am = new AudioMatcher(dataPoints);
		// Song song = am.getMatchedSong();
		// System.out.println(song.getName());
		// System.out.println(song.getCount());

		AudioRecognizerView view = AudioRecognizerView.getInstance();

		while (true) {
			MicrophoneAudio audio = new MicrophoneAudio();
			AudioRecognizer audioRecognizer = new AudioRecognizer();
			byte[] byteArray = audioRecognizer.start(audio);
			Spectrum spectrum = new Spectrum(byteArray, -1);
			ArrayList<DataPoint> dataPoints = spectrum.getRecordPoints();
			AudioMatcher am = new AudioMatcher(dataPoints);
			Song song = am.getMatchedSong();
			System.out.println(song.getName());
			view.setNameOfSong(song.getName());
		}

		// FileHandler fileHandler = new FileHandler();
		// ArrayList<Song> songList = fileHandler
		// .iterateDirectory("/Volumes/EXTERNEFESTPLATTE/Musik/Downloaded by
		// MediaHuman");
		// int upperLimit = songList.size();
		// for (int i = 0; i < upperLimit; i++) {
		// Song song = songList.get(i);
		// MP3Audio audio = new MP3Audio(song.getAbsolutePath());
		// if (!audio.isProcessable()) {
		// continue;
		// }
		// int songId = song.getId();
		//
		// database.deleteSong(songId);
		// database.insertSong(song);
		//
		// AudioRecognizer audioRecognizer = new AudioRecognizer();
		// byte[] bytes = audioRecognizer.start(audio);
		// Spectrum spectrum = new Spectrum(bytes, songId);
		// ArrayList<DataPoint> recordPoints = spectrum.getRecordPoints();
		// for (int j = 0; j < recordPoints.size(); j++) {
		// DataPoint rp = recordPoints.get(j);
		// long hash = rp.getHash();
		// int time = rp.getTime();
		// database.insertHashes(hash, songId, time);
		// }
		// }
	}

}
