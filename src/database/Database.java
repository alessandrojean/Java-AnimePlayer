package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Episode;

public class Database
{

	public static final String FILE_NAME = "database.db";

	private static final String CLASS_SQLITE = "org.sqlite.JDBC";

	private static final String SQL_CREATE_TABLES = "create table if not exists episodes (id_episode integer primary key AUTOINCREMENT not null,number_episode int not null, name_episode varchar(100) not null,path_episode longtext not null,viewed_episode bool not null);";
	private static final String SQL_SELECT_EPISODES = "select * from episodes order by number_episode;";
	private static final String SQL_INSERT_EPISODE = "insert into episodes values (null,?,?,?,?);";
	private static final String SQL_UPDATE_EPISODE = "update episodes set number_episode=?,name_episode=?,path_episode=?,viewed_episode=? where id_episode=?;";
	private static final String SQL_DELETE_EPISODE = "delete from episodes where id_episode=?;";
	
	private File databaseFile;

	public Database()
	{
		super();
	}

	public Database(File databaseFile)
	{
		super();
		this.databaseFile = databaseFile;
	}

	public void createTables() throws ClassNotFoundException, SQLException
	{
		Connection lConnection = null;
		try
		{
			Class.forName(CLASS_SQLITE);
			lConnection = DriverManager.getConnection(getConnectionURL());
			lConnection.setAutoCommit(false);
			Statement lStatement = lConnection.createStatement();

			lStatement.executeUpdate(SQL_CREATE_TABLES);

			lStatement.close();

		}
		catch (ClassNotFoundException | SQLException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				lConnection.commit();
				lConnection.close();
			}
			catch (Exception e)
			{
				throw e;
			}
		}
	}

	public Episode getEpisode() throws ClassNotFoundException, SQLException
	{
		Episode result = null;
		Connection lConnection = null;
		try
		{
			Class.forName(CLASS_SQLITE);
			lConnection = DriverManager.getConnection(getConnectionURL());
			lConnection.setAutoCommit(false);

			Statement lStatement = lConnection.createStatement();
			ResultSet lResultSet = lStatement.executeQuery(SQL_SELECT_EPISODES);
			
			if (lResultSet.next())
			{
				result = new Episode(lResultSet.getInt("id_episode"));
				result.setName(lResultSet.getString("name_episode"));
				result.setNumber(lResultSet.getInt("number_episode"));
				result.setFile(new File(lResultSet.getString("path_episode")));
				result.setViewed(lResultSet.getBoolean("viewed_episode"));
			}

			lResultSet.close();
			lStatement.close();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				lConnection.commit();
				lConnection.close();
			}
			catch (Exception e)
			{
				throw e;
			}
		}

		return result;
	}
	
	public List<Episode> getEpisodes() throws ClassNotFoundException, SQLException
	{
		List<Episode> result = new ArrayList<>();
		Connection lConnection = null;
		try
		{
			Class.forName(CLASS_SQLITE);
			lConnection = DriverManager.getConnection(getConnectionURL());
			lConnection.setAutoCommit(false);

			Statement lStatement = lConnection.createStatement();
			ResultSet lResultSet = lStatement.executeQuery(SQL_SELECT_EPISODES);

			while (lResultSet.next())
			{
				Episode lEpisode = new Episode(lResultSet.getInt("id_episode"));
				lEpisode.setName(lResultSet.getString("name_episode"));
				lEpisode.setNumber(lResultSet.getInt("number_episode"));
				lEpisode.setFile(new File(lResultSet.getString("path_episode")));
				lEpisode.setViewed(lResultSet.getBoolean("viewed_episode"));
				
				result.add(lEpisode);
			}

			lResultSet.close();
			lStatement.close();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				lConnection.commit();
				lConnection.close();
			}
			catch (Exception e)
			{
				throw e;
			}
		}

		return result;
	}

	public void insertEpisode(Episode episode) throws ClassNotFoundException, SQLException
	{
		Connection lConnection = null;
		try
		{
			Class.forName(CLASS_SQLITE);
			lConnection = DriverManager.getConnection(getConnectionURL());
			lConnection.setAutoCommit(false);

			PreparedStatement lPreparedStatement = lConnection.prepareStatement(SQL_INSERT_EPISODE, Statement.RETURN_GENERATED_KEYS);
			lPreparedStatement.setInt(1, episode.getNumber());
			lPreparedStatement.setString(2, episode.getName());
			lPreparedStatement.setString(3, episode.getFile().toString());
			lPreparedStatement.setBoolean(4, episode.isViewed());
			lPreparedStatement.executeUpdate();

			try (ResultSet generatedKeys = lPreparedStatement.getGeneratedKeys()) {
	            if (generatedKeys.next()) 
	            	episode.setId(generatedKeys.getInt(1));
	            else
	                throw new SQLException("Creating episode failed, no ID obtained.");
	        }

		}
		catch (ClassNotFoundException | SQLException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				lConnection.commit();
				lConnection.close();
			}
			catch (Exception e)
			{
				throw e;
			}
		}
	}

	public void updateEpisode(Episode episode) throws ClassNotFoundException, SQLException
	{
		Connection lConnection = null;
		try
		{
			Class.forName(CLASS_SQLITE);
			lConnection = DriverManager.getConnection(getConnectionURL());
			lConnection.setAutoCommit(false);

			PreparedStatement lPreparedStatement = lConnection.prepareStatement(SQL_UPDATE_EPISODE);
			lPreparedStatement.setInt(1, episode.getNumber());
			lPreparedStatement.setString(2, episode.getName());
			lPreparedStatement.setString(3, episode.getFile().toString());
			lPreparedStatement.setBoolean(4, episode.isViewed());
			lPreparedStatement.setInt(5, episode.getId());
			lPreparedStatement.executeUpdate();

		}
		catch (ClassNotFoundException | SQLException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				lConnection.commit();
				lConnection.close();
			}
			catch (Exception e)
			{
				throw e;
			}
		}
	}
	
	public void deleteEpisode(Episode episode) throws ClassNotFoundException, SQLException
	{
		Connection lConnection = null;
		try
		{
			Class.forName(CLASS_SQLITE);
			lConnection = DriverManager.getConnection(getConnectionURL());
			lConnection.setAutoCommit(false);

			PreparedStatement lPreparedStatement = lConnection.prepareStatement(SQL_DELETE_EPISODE);
			lPreparedStatement.setInt(1, episode.getId());
			lPreparedStatement.executeUpdate();

		}
		catch (ClassNotFoundException | SQLException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				lConnection.commit();
				lConnection.close();
			}
			catch (Exception e)
			{
				throw e;
			}
		}
	}
	
	private String getConnectionURL()
	{
		return "jdbc:sqlite:" + (databaseFile == null ? FILE_NAME : databaseFile.toString());
	}

	public File getDatabaseFile()
	{
		return databaseFile;
	}

	public void setDatabaseFile(File databaseFile)
	{
		this.databaseFile = databaseFile;
	}
}
