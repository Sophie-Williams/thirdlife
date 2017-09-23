package thirdlife.db;

public abstract class Model implements IModel
{	
	public Boolean exists()
	{
		return getIfExists() != null;
	}
	
	public Model getIfExists()
	{
		return null;
	}
	
	public void create()
	{
		//
	}
	
	public void update()
	{
		//
	}
	
	public void delete()
	{
		//
	}
}
