package ressources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.Song;

public class FileHandler {
	private static int i = 1;
	private static ArrayList<Song> songsArray = new ArrayList<Song>();

	public boolean deleteFile(String path) {
		File f = new File(path);
		boolean deleted = f.delete();
		return deleted;
	}

	public ArrayList<Song> iterateDirectory(String path) {
		File dir = new File(path);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.isDirectory()) {
					iterateDirectory(child.getAbsolutePath());
				} else {
					String absolutePath = child.getAbsolutePath();
					String extension = extensionParser(absolutePath);
					if (extension.equalsIgnoreCase("mp3")) {
						Song song = new Song();
						song.setId(i);
						song.setName(child.getName());
						song.setAbsolutePath(child.getAbsolutePath());
						songsArray.add(song);
						i++;
					}
				}
			}
		}
		return songsArray;
	}

	private String extensionParser(String path) {
		String extension = "";

		int i = path.lastIndexOf('.');
		if (i > 0) {
			extension = path.substring(i + 1);
		}
		return extension;
	}

	public void writeInFile(String line, String name) {
		File f = new File(name);
		if (!f.exists()) {
			f.mkdirs();
		}
		try {
			FileWriter fstream = new FileWriter(name, true);
			BufferedWriter outFile = new BufferedWriter(fstream);
			outFile.write(line);
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String readFromFile(String path) {
		String output = "";
		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				output = output + line;
			}
			bufferedReader.close();
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return output;
	}

	public String[] readCSVFile(String path) {
		String text = this.readFromFile(path);
		String delimiter = ";";
		String[] allText = text.split(delimiter);
		return allText;
	}

}
