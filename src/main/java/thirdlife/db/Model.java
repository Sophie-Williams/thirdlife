package thirdlife.db;

public abstract class Model implements IModel
{	
	public Boolean exists()
	{
		return getIfExists() != null;
	}
}
