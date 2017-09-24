package thirdlife.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pircbotx.hooks.types.GenericMessageEvent;

import thirdlife.db.CommandHistoryEntry;

public final class CommandManager
{
	protected static String commandRegex = "^!(tl|thirdlife) ([^ ]+) ?(.*)?";
	
	public static Command parse(GenericMessageEvent event)
	{
		Pattern commandPattern = Pattern.compile(commandRegex);
    	
    	Matcher commandMatcher = commandPattern.matcher(event.getMessage());
    	
        if (commandMatcher.find())
        {
        	CommandHistoryEntry commandHistoryEntry = new CommandHistoryEntry(event.getUser().getNick(), commandMatcher.group(0));
        	commandHistoryEntry.create();
        	
        	/* TODO: If sender is bot owner and is properly identified, accept
        	 *       additional commands, like 'debug enable', etc.
        	 * TODO: Require registration to run commands other than 'register'.
        	 */        	
        	switch(commandMatcher.group(2))
            {
            	case "register":
            		return new Register(commandMatcher);
            		
            	case "info":
            		return new Info(commandMatcher);
            		
            	case "endorse":
            		return new Endorse(commandMatcher);
            }
        }
        return null;
	}
}
