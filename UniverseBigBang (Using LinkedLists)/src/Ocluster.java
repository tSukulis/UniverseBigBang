
public class Ocluster {
	private int cid;
	private Planet orphans;
	
	
	public Ocluster(int cid)
	{
		this.cid=cid;
	}
	public Ocluster()
	{
		this.cid=Integer.MAX_VALUE;
		this.orphans=new Planet(-1,0);
	}
	
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public Planet getOrphans() {
		return orphans;
	}
	public void setOrphans(Planet orphans) {
		this.orphans = orphans;
	}
	
	
	

}
