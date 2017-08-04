package model;

public class DataPoint {
	private int songId;
	private int offset;
	private int count;
	private long hash;
	private int time;

	public DataPoint() {
		this.songId = 0;
		this.offset = 0;
		this.count = 0;
	}

	public int getSongId() {
		return this.songId;
	}

	public int getOffset() {
		return this.offset;
	}

	public int getCount() {
		return this.count;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getHash() {
		return hash;
	}

	public void setHash(long hash) {
		this.hash = hash;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return Integer.toString(this.songId);
	}
}
