package thirdlife.bot;

import java.io.Console;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.SASLCapHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import thirdlife.db.CommandHistoryEntry;
import thirdlife.db.Database;
import thirdlife.db.Endorsement;
import thirdlife.db.User;

/*
 * General TODO List:
 * - Relational integrity in the database (once there are relationships).
 * - Probably should write some unit tests.
 * - Show "biggest fan" in "info" command.
 * - Handle endorsing oneself.
 * - Command superclass and individual XXXXXCommand subclasses for command logic.
 */
public final class App extends ListenerAdapter
{   
	// TODO: Create configuration file?
	public static final Boolean SQL_DEBUG = true;
	
    public static String server = "irc.freenode.net";
    public static String channel = "#thirdlife_testing";

    protected String commandRegex = "^!(tl|thirdlife) ([^ ]+) ?(.*)?";
    protected String commandMessage;
    protected String trigger;
    protected String command;
    protected String commandArgument;

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
    	Pattern commandPattern = Pattern.compile(commandRegex);
    	
    	Matcher commandMatcher = commandPattern.matcher(event.getMessage());
    	
        if (commandMatcher.find())
        {
        	commandMessage = commandMatcher.group(0);
        	trigger = commandMatcher.group(1);
        	command = commandMatcher.group(2);
        	commandArgument = commandMatcher.group(3);
        	
        	CommandHistoryEntry commandHistoryEntry = new CommandHistoryEntry(event.getUser().getNick(), commandMessage);
        	commandHistoryEntry.create();
        	
        	/* TODO: If sender is bot owner and is properly identified, accept
        	 *       additional commands, like 'debug enable', etc.
        	 */
        	User user = null;
        	String response = null;
        	
        	// TODO: Require registration to run commands other than 'register'.
        	switch(command)
            {
            	case "register":
            		
            		//TODO: Validate nick.
            		
            		user = new User(event.getUser().getNick());
            		
            		if(user.exists())
            		{
            			response = "You have already registered.";
            		}
            		else
            		{
            			user.create();
            			
            			response = "Welcome to Third Life! You are now registered.";
            		}
            		
            		break;
            		
            	case "info":
            		
            		String nick = null;
            		
            		if(commandArgument.isEmpty())
            		{
            			nick = event.getUser().getNick();
            		}
            		else
            		{
            			Pattern firstWordPattern = Pattern.compile("^([^ ]+)");
            			
            			Matcher firstWordMatcher = firstWordPattern.matcher(commandArgument);
            			
            			if(firstWordMatcher.find())
            			{
            				nick = firstWordMatcher.group(1);
            			}
            		}
            		
            		if(nick == null)
            		{
            			response = "I'm... I can't help you.";
            		}
            		else
            		{
            			user = new User(nick).getIfExists();
            			
            			if(user == null)
            			{
            				response = "No registered users found with nick '" + nick + "'.";
            			}
            			else
            			{
            				if(user.getNick().equals(event.getUser().getNick()))
            				{
            					response = "That's you!";
            					response += " Registered since " + user.getCreatedAt() + ".";
            					response += " Charisma: " + user.getCharisma() + ".";
            				}
            				else
        					{
            					response = user.getNick() + ": ";
            					response += " Registered since " + user.getCreatedAt() + ".";
            					response += " Charisma: " + user.getCharisma() + ".";
        					}
            			}
            		}
            		
            		break;
            		
            	case "endorse":
            		
            		String by_nick = event.getUser().getNick();
            		
            		if(commandArgument.isEmpty())
            		{
            			response = "Endorse a user with '!tl endorse <user>'.";
            		}
            		else
            		{
        				Pattern firstWordPattern = Pattern.compile("^([^ ]+)");
            			
            			Matcher firstWordMatcher = firstWordPattern.matcher(commandArgument);
            			
            			if(firstWordMatcher.find())
            			{
            				nick = firstWordMatcher.group(1);
            				
            				user = new User(nick).getIfExists();
                			
                			if(user == null)
                			{
                				response = "No registered users found with nick '" + nick + "'.";
                			}
                			else
                			{
                				Endorsement endorsement = new Endorsement(by_nick, nick);
                				endorsement.create();
                				
                				user.endorse();
                				user.update();
                			}
            			}
            		}
            		
            		break;
            		
        		default:
        			response = "wat?";
            }
            
        	if(response != null)
        		event.respond(response);
        }
    }
    
    public static void sqlDebug(String sql)
    {
    	if(SQL_DEBUG) System.out.println("SQL_DEBUG: " + sql);
    }
}
