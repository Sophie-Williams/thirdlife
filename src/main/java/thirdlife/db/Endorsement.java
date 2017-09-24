package thirdlife.db;

import java.sql.PreparedStatement;

public final class Endorsement extends Model
{
	public static final String SQL_TABLE_NAME = "endorsements";
	
	public static final String SQL_CREATE_TABLE_STATEMENT =
			"CREATE TABLE IF NOT EXISTS " + SQL_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ " by_nick TEXT NOT NULL, "
			+ " to_nick TEXT NOT NULL, "
			+ " created_at TEXT NOT NULL)";
	
	public static final String SQL_INSERT_STATEMENT =
			"INSERT INTO " + SQL_TABLE_NAME
			+ " (by_nick, to_nick, created_at) VALUES (?, ?, datetime('now'))";
	
	private String by_nick;
	private String to_nick;
	
	public Endorsement(String by_nick, String to_nick)
	{
		super();
		
		this.by_nick = by_nick;
		this.to_nick = to_nick;
	}
	
	public static void createTable()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_CREATE_TABLE_STATEMENT);
				
		Database.execute(statement);
	}
	
	@Override
	public void create()
	{
		PreparedStatement statement = Database.prepareStatement(SQL_INSERT_STATEMENT);
		
		Database.setString(statement, 1, by_nick);
		Database.setString(statement, 2, to_nick);
		
		Database.executeUpdate(statement);
	}
}
