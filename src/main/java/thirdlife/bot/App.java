package thirdlife.bot;

import java.io.Console;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.SASLCapHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import thirdlife.command.Command;
import thirdlife.command.CommandManager;
import thirdlife.db.Database;

/*
 * General TODO List:
 * - Relational integrity in the database (once there are relationships).
 * - Probably should write some unit tests.
 * - Show "biggest fan" in "info" command.
 * - Command Help
 */
public final class App extends ListenerAdapter
{   
	// TODO: Create configuration file?
	public static final Boolean SQL_DEBUG = true;
	
    public static String server = "irc.freenode.net";
    public static String channel = "#thirdlife_testing";

    public static void main(String[] args) throws Exception
    {
    	Database.initializeDatabases();
    	
    	Console console = System.console();
    	
    	String nick = console.readLine("Enter the IRC nick for your bot: ");
    	String password = new String(console.readPassword("Enter a password: "));
    	
    	System.out.println("Connecting to server " + server + ", channel " + channel + "...");

        Configuration config = new Configuration.Builder()
            .setName(nick) 
            .setLogin(nick)
            .setRealName(nick)
            .setAutoNickChange(true)
            .setAutoReconnect(true)
            .setFinger(nick)
            .addServer(server)
            .addAutoJoinChannel(channel)
            .addListener(new App())
            .addCapHandler(new SASLCapHandler(nick, password))
            .buildConfiguration();

        @SuppressWarnings("resource")
		PircBotX bot = new PircBotX(config);

        bot.startBot();
    }
    
    @Override
    public void onGenericMessage(GenericMessageEvent event)
    {
    	Command command = CommandManager.parse(event);

    	if(command == null)
    	{
    		event.respond("wat?");
    	}
    	else
    	{
    		command.execute(event);
    		
    		event.respond(command.getResponse());
    	}
    }
    
    public static void sqlDebug(String sql)
    {
    	if(SQL_DEBUG) System.out.println("SQL_DEBUG: " + sql);
    }
}
