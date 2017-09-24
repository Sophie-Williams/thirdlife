package thirdlife.command;

import org.pircbotx.hooks.types.GenericMessageEvent;

public interface ICommand
{
	public void execute(GenericMessageEvent event);
	public String getResponse();
}
