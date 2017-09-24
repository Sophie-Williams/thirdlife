package thirdlife.command;

import java.util.regex.Matcher;

public abstract class Command implements ICommand
{
	protected String response;
    protected String commandMessage;
    protected String trigger;
    protected String command;
    protected String commandArgument;
    
    public Command(Matcher commandMatcher)
    {
    	commandMessage = commandMatcher.group(0);
    	trigger = commandMatcher.group(1);
    	command = commandMatcher.group(2);
    	commandArgument = commandMatcher.group(3);
    }
	
	public String getResponse()
	{
		return response;
	}
}
