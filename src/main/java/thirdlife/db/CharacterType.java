package thirdlife.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class CharacterType extends Model
{
	public static final String SQL_TABLE_NAME = "character_types";
	
	public static final String SQL_CREATE_TABLE_STATEMENT =
			"CREATE TABLE IF NOT EXISTS " + SQL_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)";
	
	public static final String SQL_SELECT_STATEMENT =
			"SELECT * FROM " + SQL_TABLE_NAME + " WHERE name = ? LIMIT 1";
	
	public static final String SQL_INSERT_STATEMENT =
			"INSERT INTO " + SQL_TABLE_NAME + " (name) VALUES (?)";
	
	public static final String SQL_UPDATE_STATEMENT =
			"UPDATE " + SQL_TABLE_NAME + " SET name = ?";
	
	public static final String SQL_DELETE_STATEMENT =
			"DELETE FROM " + SQL_TABLE_NAME + " WHERE name = ?";
	
	public static final String SQL_TRUNCATE_STATEMENT =
			"DELETE FROM " + SQL_TABLE_NAME;
	
	public static final String SQL_SEED_STATEMENT =
			"INSERT INTO " + SQL_TABLE_NAME + " (name) VALUES ('Human'), ('Cat'), ('Dog')";
	
	private Integer id;
	private String name;
	
	public CharacterType(String name)
	{	
		super();
		
		this.name = name;
	}
	
	public static void createTable()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_CREATE_TABLE_STATEMENT);
				
		Database.execute(statement);
	}
	
	@Override
	public CharacterType getIfExists()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_SELECT_STATEMENT);
		
		Database.setString(statement, 1, name);
		
		ResultSet resultSet = Database.executeQuery(statement); 
		
		if(Database.getNext(resultSet))
		{
			this.id = Database.getIntegerColumnValue(resultSet, "id");
			
			return this;
		}
		
		Database.closeAll(statement);
		
		return null;
	}
	
	@Override
	public void create()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_INSERT_STATEMENT);
		
		Database.setString(statement, 1, name);
		
		Database.executeUpdate(statement);
	}
	
	@Override
	public void update()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_UPDATE_STATEMENT);
		
		Database.setString(statement, 1, name);
		
		Database.executeUpdate(statement);
	}
	
	@Override
	public void delete()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_DELETE_STATEMENT);
		
		Database.setString(statement, 1, name);
		
		Database.executeUpdate(statement);
	}
	
	public static void truncate()
	{
		Database.executeUpdate(
			Database.prepareStatement(SQL_TRUNCATE_STATEMENT));
	}
	
	public static void seed()
	{
		Database.executeUpdate(
				Database.prepareStatement(SQL_SEED_STATEMENT));
	}
}
