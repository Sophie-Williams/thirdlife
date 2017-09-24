package thirdlife.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class User extends Model
{
	public static final String SQL_TABLE_NAME = "users";
	
	public static final String SQL_CREATE_TABLE_STATEMENT =
			"CREATE TABLE IF NOT EXISTS " + SQL_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ " nick TEXT NOT NULL, "
			+ " charisma INTEGER DEFAULT 10, "
			+ " active_character_id INTEGER NULL, "
			+ " created_at TEXT NOT NULL)";
	
	public static final String SQL_SELECT_STATEMENT =
			"SELECT * FROM " + SQL_TABLE_NAME + " WHERE nick = ? LIMIT 1";
	
	public static final String SQL_INSERT_STATEMENT =
			"INSERT INTO " + SQL_TABLE_NAME
			+ " (nick, created_at) VALUES (?, datetime('now'))";
	
	public static final String SQL_UPDATE_STATEMENT =
			"UPDATE " + SQL_TABLE_NAME + " SET charisma = ?, "
			+ " active_character_id = ? WHERE id = ?";
	
	public static final String SQL_DELETE_STATEMENT =
			"DELETE FROM " + SQL_TABLE_NAME + " WHERE nick = ?";
	
	private Integer id;
	private String nick;
	private Integer charisma;
	private Integer active_character_id;
	private String created_at;
	
	public User(String nick)
	{
		super();
		
		this.nick = nick;
		
		User user = this.getIfExists();
		
		if(user != null)
		{
			this.id = user.id;
			this.charisma = user.charisma;
			this.active_character_id = user.active_character_id;
			this.created_at = user.created_at;
		}
	}
	
	public String getNick()
	{
		return nick;
	}
	
	public Integer getCharisma()
	{
		return charisma;
	}
	
	public Integer getActiveCharacterId()
	{
		return active_character_id;
	}
	
	public String getCreatedAt()
	{
		return created_at;
	}
	
	public void endorse()
	{
		this.charisma++;
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
			this.charisma = Database.getIntegerColumnValue(resultSet, "charisma");
			this.active_character_id = Database.getIntegerColumnValue(resultSet, "active_character_id");
			this.created_at = Database.getStringColumnValue(resultSet, "created_at");
			
			return this;
		}
		
		Database.close(statement);
		
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
		
		Database.setInteger(statement, 1, charisma);
		Database.setInteger(statement, 2, active_character_id);
		Database.setInteger(statement, 3, id);

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
