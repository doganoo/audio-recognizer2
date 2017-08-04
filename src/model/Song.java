package model;

public class Song {
	private int id = -1;
	private String name = "undefined";
	private String absolutePath = "undefined";
	private boolean processable = false;
	// TODO insertDate to Date/Calendar
	private String insertDate = "undefined";
	private int count = 0;

	public Song() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public boolean isProcessable() {
		return processable;
	}

	public void setProcessable(boolean processable) {
		this.processable = processable;
	}

	public String getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
