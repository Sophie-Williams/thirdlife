package thirdlife.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import thirdlife.bot.App;

public final class Database
{	
	public static void initializeDatabases()
	{
		System.out.print("Setting up databases... ");
		
		User.createTable();
		Character.createTable();
		CharacterType.createTable();
		
		CharacterType.truncate();
		Character.truncate();
		
		CharacterType.seed();
		
		System.out.println("done.");
	}
	
	public static Connection connection()
	{
		Connection connection = null;
		
		try
		{
			Class.forName("org.sqlite.JDBC");
		
			connection = DriverManager.getConnection("jdbc:sqlite:SQLiteDB.db");
		}
		catch(Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
		return connection;
	}
	
	public static PreparedStatement prepareStatement(String sql)
	{
		Connection connection = Database.connection();
		
		PreparedStatement statement = null;
		
		try
		{
			App.sqlDebug(sql);
			
			statement = connection.prepareStatement(sql);

		}
		catch(SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			
			System.err.println("SQL: " + sql);
			
			System.exit(1);
		}
		
		return statement;
	}
	
	public static void setString(PreparedStatement statement, Integer parameterIndex, String value)
	{
		try
		{
			statement.setString(parameterIndex, value);
		}
		catch(SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			
			System.exit(1);
		}
	}
	
	public static void setInteger(PreparedStatement statement, Integer parameterIndex, Integer value)
	{
		try
		{
			statement.setInt(parameterIndex, value);
		}
		catch(SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			
			System.exit(1);
		}
	}
	
	public static void execute(PreparedStatement statement)
	{		
		try
		{
			statement.execute();
			
			closeAll(statement);
		}
		catch(SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			
			System.exit(1);
		}
	}
	
	public static ResultSet executeQuery(PreparedStatement statement)
	{
		ResultSet resultSet = null;
		
		try
		{
			resultSet = statement.executeQuery();
		}
		catch(SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			
			System.exit(1);
		}
		
		return resultSet;
	}
	
	public static void executeUpdate(PreparedStatement statement)
	{		
		try
		{
			statement.executeUpdate();
			
			closeAll(statement);
		}
		catch(SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			
			System.exit(1);
		}
	}
	
	public static Boolean getNext(ResultSet resultSet)
	{
		try
		{
			return resultSet.next();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
		return false;
	}
	
	public static String getStringColumnValue(ResultSet resultSet, String columnLabel)
	{
		try
		{
			return resultSet.getString(columnLabel);
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
		return null;
	}
	
	public static Integer getIntegerColumnValue(ResultSet resultSet, String columnLabel)
	{
		try
		{
			return resultSet.getInt(columnLabel);
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
		return null;
	}
	
	public static void closeAll(PreparedStatement statement)
	{		
		try
		{
			statement.close();

			statement.getConnection().close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
