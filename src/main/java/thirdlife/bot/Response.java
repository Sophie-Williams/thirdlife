package thirdlife.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class Response
{
	public static Map<String, String[]> responses = new HashMap<String, String[]>(){
		private static final long serialVersionUID = 1L;
		{
			put("confirmation", new String[]{
				"All set.",
				"Okay.",
				"Done!"
			});
			put("endorsed_too_often", new String[]{
				"Give it a rest already.",
				"%to_nick% is great an all, but once a day is enough."
			});
		}
	};
	
	public static String get(String type, Map<String, String> values)
	{
		String[] choices = responses.get(type);
		
		Integer choice = (new Random()).nextInt(choices.length);
		
		String response = choices[choice];
		
		for(Map.Entry<String, String> value : values.entrySet())
		{
			response = response.replaceAll("%" + value.getKey() + "%", value.getValue());
		}
		
		return response;
	}
}
