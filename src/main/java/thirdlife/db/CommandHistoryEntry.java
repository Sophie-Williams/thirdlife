package thirdlife.db;

import java.sql.PreparedStatement;

public final class CommandHistoryEntry extends Model
{
	public static final String SQL_TABLE_NAME = "command_history_entries";
	
	public static final String SQL_CREATE_TABLE_STATEMENT =
			"CREATE TABLE IF NOT EXISTS " + SQL_TABLE_NAME
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ " nick TEXT NOT NULL, "
			+ " command TEXT NOT NULL, "
			+ " created_at TEXT NOT NULL)";
	
	public static final String SQL_INSERT_STATEMENT =
			"INSERT INTO " + SQL_TABLE_NAME
			+ " (nick, command, created_at) VALUES (?, ?, datetime('now'))";
	
	private String nick;
	private String command;
	
	public CommandHistoryEntry(String nick, String command)
	{
		super();
		
		this.nick = nick;
		this.command = command;
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
		
		Database.setString(statement, 1, nick);
		Database.setString(statement, 2, command);
		
		Database.executeUpdate(statement);
	}
}
