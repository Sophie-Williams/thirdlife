package thirdlife.command;

import java.util.regex.Matcher;

import org.pircbotx.hooks.types.GenericMessageEvent;

import thirdlife.db.User;

public final class Register extends Command
{
	public Register(Matcher commandMatcher)
	{
		super(commandMatcher);
	}
	
	public void execute(GenericMessageEvent event)
	{
		//TODO: Validate nick.
		
		User user = new User(event.getUser().getNick());
		
		if(user.exists())
		{
			response = "You have already registered.";
		}
		else
		{
			user.create();
			
			response = "Welcome to Third Life! You are now registered.";
		}
	}
}
