package thirdlife.db;

public interface IModel
{	
	public Boolean exists();
	public Model getIfExists();
	public void create();
	public void update();
	public void delete();
}
