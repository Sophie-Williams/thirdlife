package thirdlife.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class User extends Model
{
	public static final String SQL_TABLE_NAME = "users";
	
	public static final String SQL_CREATE_TABLE_STATEMENT =
			"CREATE TABLE IF NOT EXISTS " + SQL_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ " nick TEXT NOT NULL, created_at TEXT NOT NULL)";
	
	public static final String SQL_SELECT_STATEMENT =
			"SELECT * FROM " + SQL_TABLE_NAME + " WHERE nick = ? LIMIT 1";
	
	public static final String SQL_INSERT_STATEMENT =
			"INSERT INTO " + SQL_TABLE_NAME
			+ " (nick, created_at) VALUES (?, datetime('now'))";
	
	public static final String SQL_UPDATE_STATEMENT =
			"UPDATE " + SQL_TABLE_NAME + " SET nick = ?";
	
	public static final String SQL_DELETE_STATEMENT =
			"DELETE FROM " + SQL_TABLE_NAME + " WHERE nick = ?";
	
	private Integer id;
	private String nick;
	private String created_at;
	
	public User(String nick)
	{
		super();
		
		this.nick = nick;
	}
	
	public String getNick()
	{
		return nick;
	}
	
	public String getCreatedAt()
	{
		return created_at;
	}
	
	public static void createTable()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_CREATE_TABLE_STATEMENT);
				
		Database.execute(statement);
	}
	
	@Override
	public User getIfExists()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_SELECT_STATEMENT);
		
		Database.setString(statement, 1, nick);
		
		ResultSet resultSet = Database.executeQuery(statement); 
		
		if(Database.getNext(resultSet))
		{
			this.id = Database.getIntegerColumnValue(resultSet, "id");
			this.created_at = Database.getStringColumnValue(resultSet, "created_at");
			
			return this;
		}
		
		Database.closeAll(statement);
		
		return null;
	}
	
	@Override
	public void create()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_INSERT_STATEMENT);
		
		Database.setString(statement, 1, nick);
		
		Database.executeUpdate(statement);
	}
	
	@Override
	public void update()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_UPDATE_STATEMENT);
		
		Database.setString(statement, 1, nick);
		
		Database.executeUpdate(statement);
	}
	
	@Override
	public void delete()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_DELETE_STATEMENT);
		
		Database.setString(statement, 1, nick);
		
		Database.executeUpdate(statement);
	}
}
