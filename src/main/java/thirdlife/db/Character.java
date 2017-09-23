package thirdlife.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

// TODO: A lot of this needs to relate to a User to be unique.
public final class Character extends Model
{
	public static final String SQL_TABLE_NAME = "characters";
	
	public static final String SQL_CREATE_TABLE_STATEMENT =
			"CREATE TABLE IF NOT EXISTS " + SQL_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ " character_type_id INTEGER NOT NULL, "
			+ " name TEXT NOT NULL, created_at TEXT NOT NULL)";
	
	public static final String SQL_SELECT_STATEMENT =
			"SELECT * FROM " + SQL_TABLE_NAME + " WHERE name = ? LIMIT 1";
	
	public static final String SQL_INSERT_STATEMENT =
			"INSERT INTO " + SQL_TABLE_NAME
			+ " (name, character_type_id, created_at) VALUES (?, ?, datetime('now'))";
	
	public static final String SQL_UPDATE_STATEMENT =
			"UPDATE " + SQL_TABLE_NAME + " SET name = ?, character_type_id = ?";
	
	public static final String SQL_DELETE_STATEMENT =
			"DELETE FROM " + SQL_TABLE_NAME + " WHERE name = ? AND character_type_id = ?";
	
	public static final String SQL_TRUNCATE_STATEMENT =
			"DELETE FROM " + SQL_TABLE_NAME;
	
	private Integer id;
	private String name;
	private Integer character_type_id;
	
	public Character(String name, Integer character_type_id)
	{
		super();
		
		this.name = name;
		this.character_type_id = character_type_id;
	}
	
	public static void createTable()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_CREATE_TABLE_STATEMENT);
				
		Database.execute(statement);
	}
	
	@Override
	public Character getIfExists()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_SELECT_STATEMENT);
		
		Database.setString(statement, 1, name);
		Database.setInteger(statement, 2, character_type_id);
		
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
		Database.setInteger(statement, 2, character_type_id);
		
		Database.executeUpdate(statement);
	}
	
	@Override
	public void update()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_UPDATE_STATEMENT);
		
		Database.setString(statement, 1, name);
		Database.setInteger(statement, 2, character_type_id);
		
		Database.executeUpdate(statement);
	}
	
	@Override
	public void delete()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_DELETE_STATEMENT);
		
		Database.setString(statement, 1, name);
		Database.setInteger(statement, 2, character_type_id);
		
		Database.executeUpdate(statement);
	}
	
	public static void truncate()
	{
		Database.executeUpdate(
			Database.prepareStatement(SQL_TRUNCATE_STATEMENT));
	}
}
