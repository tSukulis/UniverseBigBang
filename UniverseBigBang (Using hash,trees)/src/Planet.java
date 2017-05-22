

public class Planet {
	private int pid;
	private int distance;
	private Planet lc;
	private Planet rc;
	private Planet parent;
	private static Planet Root;
	
	
	public Planet(int pid,int distance)
	{
		this.pid=pid;
		this.distance=distance;
		this.lc=null;
		this.rc=null;
		this.parent=null;
	}
	
	public static Planet getRoot() {
		return Root;
	}
	public static void setRoot(Planet root) {
		Root = root;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public Planet getLc() {
		return lc;
	}
	public void setLc(Planet lc) {
		this.lc = lc;
	}
	public Planet getRc() {
		return rc;
	}
	public void setRc(Planet rc) {
		this.rc = rc;
	}
	public Planet getParent() {
		return parent;
	}
	public void setParent(Planet parent) {
		this.parent = parent;
	}
	
	
	
	public Planet insertPlanet(Planet root, Planet newnode,Planet sentinel)
	{
		Planet current=root,parent=null;
		if(root==null || root.getPid()==0)
		{
			newnode.setLc(sentinel);
			newnode.setRc(sentinel);
			sentinel.setParent(newnode);
			return newnode;
		}
		else
		{
			parent=current;
			boolean isLc=false,isRc=false;
			
			while(current!=sentinel && current!=null)
			{
				if(current.getDistance()>newnode.getDistance())
				{
					parent=current;
					current=current.getLc();
					isLc=true;
					isRc=false;
				}
				else if(current.getDistance()<newnode.getDistance())
				{
					parent=current;
					current=current.getRc();
					isLc=false;
					isRc=true;
				}
			}
			if(isLc)
			{
				current=newnode;
				current.setParent(parent);
				current.getParent().setLc(current);
			}
			else
			{
				current=newnode;
				current.setParent(parent);
				current.getParent().setRc(current);
			}
			
			if(current.getLc()==null)
				current.setLc(sentinel);
				
			if(current.getRc()==null)
				current.setRc(sentinel);
			
			sentinel.setParent(current);
			return root;
		}	
			
	}
	
	
	
	
	
	
	
	
	
	
	
}
