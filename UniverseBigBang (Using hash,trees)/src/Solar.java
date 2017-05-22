
public class Solar {
	private int sid;
	private Planet planets;
	private Planet psentinel;
	private Solar next;
	
	public Solar(int sid)
	{
		this.sid=sid;
		this.planets=null;
		this.psentinel=new Planet(-1,0);
		this.next=null;	
	}
	
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public Planet getPlanets() {
		return planets;
	}
	public void setPlanets(Planet planets) {
		this.planets = planets;
	}
	public Planet getPsentinel() {
		return psentinel;
	}
	public void setPsentinel(Planet psentinel) {
		this.psentinel = psentinel;
	}
	public void setPidPsentinel(int pid)
	{
		this.getPsentinel().setPid(pid);
	}
	public Solar getNext() {
		return next;
	}
	public void setNext(Solar next) {
		this.next = next;
	}
	
    
	
	
	
	public void deletePlanetTree(Planet root)
	{
		if(root==null)
			return;
		deletePlanetTree(root.getLc());
		deletePlanetTree(root.getRc());
		
		
	}
	

	public void splitPlanets(int dis)
	{
		Planet root=this.getPlanets();
		
		root=this.getPlanets();
		if(root==null)
			return;
		Planet current=this.getPlanets();
		Planet prevR=null,prevL=null;
		
		while (current!=null && current!=this.getPsentinel() && dis!=current.getDistance())
		{
			if(dis<current.getDistance())
			{
				if(prevR==null)
				{
					Main.root2=current;
				}
				else
				{
					prevR.setDistance(current.getDistance());
				}
				prevR=current;
				current=current.getLc();
				prevR.setLc(null);
			}
			else
			{
				if(prevL==null)
					Main.root1=current;
				else
					prevL.setRc(current);
				prevL=current;
				current=current.getRc();
				prevL.setRc(null);
		
			}
		}
		if(current==null)
			return;
		if(prevR==null)
			Main.root2=current.getRc();
		else
			prevR.setLc(current.getRc());
		if(prevL==null)
			Main.root1=current.getLc();
		else
			prevL.setRc(current.getLc());
		
	}
		
		
	
	
}
