package thirdlife.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pircbotx.hooks.types.GenericMessageEvent;

import thirdlife.db.User;

public final class Info extends Command
{
	public Info(Matcher commandMatcher)
	{
		super(commandMatcher);
	}
	
	public void execute(GenericMessageEvent event)
	{
		String nick = null;
		
		if(commandArgument.isEmpty())
		{
			nick = event.getUser().getNick();
		}
		else
		{
			Pattern pattern = Pattern.compile("^([^ ]+)");
			
			Matcher matcher = pattern.matcher(commandArgument);
			
			if(matcher.find())
			{
				nick = matcher.group(1);
			}
		}
		
		if(nick == null)
		{
			response = "I'm... I can't help you.";
		}
		else
		{
			User user = new User(nick).getIfExists();
			
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
	}
}
