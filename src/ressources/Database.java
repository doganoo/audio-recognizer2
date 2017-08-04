package ressources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import model.DataPoint;
import model.Song;

public class Database {
	private Connection connection = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Database() {
		this.connect();
	}

	private void connect() {
		// TODO insert your credentials
		String host = "";
		String database = "";
		String password = "";
		String user = "";
		try {
			// create a mysql database connection
			String myDriver = "org.gjt.mm.mysql.Driver";
			String myUrl = "jdbc:mysql://" + host + "/" + database;
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, user, password);
			this.connection = conn;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<DataPoint> getSongsByHash(long hash) {
		ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();
		int _songId = -2;
		long _hash = -1;
		int _time = -1;
		try {
			String query = "select song_id, hash, time from hashes where hash = ?;";
			PreparedStatement statement = this.connection.prepareStatement(query);
			statement.setLong(1, hash);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			while (resultSet.next()) {
				_songId = (int) resultSet.getInt(1);
				_hash = (long) resultSet.getLong(2);
				_time = (int) resultSet.getInt(1);

				DataPoint dp = new DataPoint();
				dp.setHash(_hash);
				dp.setSongId(_songId);
				dp.setTime(_time);
				dataPoints.add(dp);
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return dataPoints;
	}

	public Song getSongById(int id) {
		Song song = new Song();
		try {
			String query = "select id, name, insert_date from songs where id = ? limit 1;";
			PreparedStatement statement = this.connection.prepareStatement(query);
			statement.setInt(1, id);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			while (resultSet.next()) {
				int _id = resultSet.getInt(1);
				String _name = resultSet.getString(2);
				String _insertDate = resultSet.getString(3);
				song.setId(_id);
				song.setName(_name);
				song.setInsertDate(_insertDate);
				song.setProcessable(true);
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return song;
	}

	public void insertSong(Song song) {
		try {
			// the mysql insert statement
			String query = " insert into songs (id, name, insert_date) values (?, ?, ?)";

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStmt.setLong(1, song.getId());
			preparedStmt.setString(2, song.getName());
			preparedStmt.setString(3, sdf.format(Calendar.getInstance().getTime()));
			// execute the preparedstatement
			preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertHashes(Long hash, int songId, int time) {
		String insertDateTime = sdf.format(Calendar.getInstance().getTime());
		try {
			// the mysql insert statement
			String query = " insert into hashes (song_id, hash, time, insert_date) values (?, ?, ?, ?)";
			// create the mysql insert preparedstatement
			PreparedStatement statement = this.connection.prepareStatement(query);
			statement.setInt(1, songId);
			statement.setLong(2, hash);
			statement.setInt(3, time);
			statement.setString(4, insertDateTime);
			// execute the preparedstatement
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteSong(int songId) {
		try {
			// the mysql insert statement
			String query = "delete from songs where id = ?;";
			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = this.connection.prepareStatement(query);
			preparedStmt.setInt(1, songId);
			preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.connection.close();
	}
}
