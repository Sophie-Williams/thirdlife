package thirdlife.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pircbotx.hooks.types.GenericMessageEvent;

import thirdlife.db.Endorsement;
import thirdlife.db.User;

public final class Endorse extends Command
{	
	public Endorse(Matcher commandMatcher)
	{
		super(commandMatcher);
	}
	
	public void execute(GenericMessageEvent event)
	{
		String by_nick = event.getUser().getNick();
		
		if(commandArgument.isEmpty())
		{
			response = "Endorse a user with '!tl endorse <user>'.";
			
			return;
		}
			
		Pattern pattern = Pattern.compile("^([^ ]+)");
		
		Matcher matcher = pattern.matcher(commandArgument);
		
		if(matcher.find())
		{
			String nick = matcher.group(1);
			
			User user = new User(nick).getIfExists();
			
			if(user == null)
			{
				response = "No registered users found with nick '" + nick + "'.";
			}
			else
			{
				Endorsement endorsement = new Endorsement(by_nick, nick);
				
				if(endorsement.wasToday())
				{
					if(by_nick.equals(nick))
					{
						response = "Let's not get carried away.";
					}
					else
					{
						response = nick + " is great and all, but you can only endorse them once a day.";
					}
					
					return;
				}
				
				endorsement.create();
				
				user.endorse();
				user.update();
				
				if(by_nick.equals(nick))
				{
					response = "Hey! You've endorsed yourself! That's allowed!";
				}
				else
				{
					response = "You've endorsed " + user.getNick() + "!";
				}
			}
		}
	}
}
