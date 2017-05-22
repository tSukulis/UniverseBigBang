import java.util.LinkedList;
import java.util.Queue;


public class Galaxy {
	private int gid;
	private Solar solars;
	private Planet orphans;
	private Solar ssentinel;
	private static int index=0;
	
	
	public Galaxy()
	{
		this.gid=Integer.MAX_VALUE;
		this.solars=null;
		this.orphans=null;
		this.ssentinel=new Solar(-1);
	}
	
	
	
	
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public Solar getSolars() {
		return solars;
	}
	public void setSolars(Solar solar) {
		this.solars = solar;
	}
	public Planet getOrphans() {
		return orphans;
	}
	public void setOrphans(Planet orphans) {
		this.orphans = orphans;
	}
	public Solar getSsentinel() {
		return ssentinel;
	}
	public void setSsentinel(Solar ssentinel) {
		this.ssentinel = ssentinel;
	}

	public static int getIndex() {
		return index;
	}

	public static void setIndex(int index) {
		Galaxy.index = index;
	}
	
	public void setSidSsentinel(int sid) {
		this.ssentinel.setSid(sid) ;
	}
	
	public  void init(int gid)
	{
		this.gid=gid;
		this.solars=new Solar(0);
		this.orphans=null;
		this.ssentinel=new Solar(-1);	
	}
	
	public void initOrphan(Planet orphanRoot)
	{
		this.orphans=orphanRoot;
	}
	

	public void insertSolar(Solar NewSolar)
	{
		Solar head=this.getSolars();
		if (head==null || head.getSid()==0)
		{
			this.setSolars(NewSolar);
			NewSolar.setNext(this.ssentinel);
			this.ssentinel.setNext(NewSolar);
			return ;//New;
		}
		NewSolar.setNext(this.getSolars());
		this.setSolars(NewSolar);
		return ;//New;	
	}
	
	public void deleteSolar(Solar Node)
	{
		Solar head = this.getSolars();
		if(head==Node)
		{
			if(head.getNext()!=null)
			{
				this.setSolars(head.getNext());
				head=null;
				return ;
			}
			return ;
		}
		Solar prev=head,temp;
		for(temp=head.getNext();temp!=this.getSsentinel();temp=temp.getNext())
		{
			if(temp==Node)
				break;
			prev=prev.getNext();
		}
		prev.setNext(temp.getNext());
		return ;
	}
	
	
	
	public boolean UnsortedTreeInsert(Planet newnode)
	{
		int g=0;
		if(this.getOrphans()==null ||this.getOrphans().getPid()==-1)
		{
			this.setOrphans(newnode);
			return true;
		}
		Planet root=this.getOrphans();
		Queue<Planet> q = new LinkedList<Planet>();
		q.add(root);
		int level=0;
		while(true)
		{
			int nodeCount=q.size();
			if(nodeCount==0)
				return false;
			level++;
			if(level>g)
				g++;
			while(nodeCount>0)
			{
				Planet temp=q.remove();
				if(level==g && temp.getLc()==null)
				{
					temp.setLc(newnode);
					return true;
				}
				if(level==g && temp.getRc()==null)
				{
					temp.setRc(newnode);
					return true;
				}
				if(temp.getLc()!=null)
				{
					q.add(temp.getLc());
				}
				if(temp.getRc()!=null)
				{
					q.add(temp.getRc());
				}
				nodeCount--;
			}
		}
	}
}
