
public class Galaxy {
	private static int  MAX_ORPHAN_CLUSTERS=100;
	private int gid;
	private Solar solars;
	private Ocluster Oclusters[];
	private Solar ssential;
	private static int index=0;
	
	
	
	public Galaxy(int gId)
	{
		this.gid=gId;
		this.solars=new Solar(0);
		this.Oclusters=new Ocluster[MAX_ORPHAN_CLUSTERS];
		this.ssential=new Solar(-1);
	}
	
	public Galaxy()
	{
		
	}
	
	public static int getMAX_ORPHAN_CLUSTERS() {
		return MAX_ORPHAN_CLUSTERS;
	}
	public static void setMAX_ORPHAN_CLUSTERS(int mAX_ORPHAN_CLUSTERS) {
		MAX_ORPHAN_CLUSTERS = mAX_ORPHAN_CLUSTERS;
	}
	public static int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		Galaxy.index = index;
	}
	public Solar getSsential() {
		return ssential;
	}
	public void setSsential(Solar ssential) {
		this.ssential = ssential;
	}
	
	
	public Ocluster[] getOclusters() {
		return Oclusters;
	}
	public void setOclusters(Ocluster oclusters[]) {
		Oclusters = oclusters;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public Solar getSolars() {
		return this.solars;
	}
	public void setSolars(Solar solars) {
		this.solars = solars;
	}

	
	
	

}
