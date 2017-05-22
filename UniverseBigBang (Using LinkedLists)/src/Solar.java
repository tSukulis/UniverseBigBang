
public class Solar {
	private int sid;
	private Planet planets;
	private Solar next;
	
	
	public Solar(int Sid)
	{
		this.sid=Sid;
		this.planets=new Planet(-1,0);
		this.next=null;	
	}
	
	public void SetData(int sid, Planet planets,Solar next)
	{
		this.sid=sid;
		this.planets=new Planet(-1,0);
		this.next=null;
	}
	
	
	
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public Solar getNext() {
		return next;
	}
	public void setNext(Solar next) {
		this.next = next;
	}
	public Planet getPlanets() {
		return planets;
	}
	public void setPlanets(Planet planets) {
		this.planets = planets;
	}
	
	public Solar insertSolar(Solar New,Solar head,Solar ssential)
	{
		if (head.getSid()==0)
		{
			New.setNext(ssential);
			ssential.setNext(New);
			return New;
		}
		New.setNext(head);
		return New;	
	}
	
	public Solar lookUp_Solar(Solar head,int Sid,Solar Ssential)
	{
		
		for(Solar temp=head;temp!=null;temp=temp.getNext())
		{
			if(temp.getSid()==Sid && temp!=Ssential)
			{
				return temp;
			}
		}
		return null;
	}
	
	
	public Solar deleteSolar(Solar head,Solar Node,Galaxy galaxy)
	{
		if(head==Node)
		{
			if(head.getNext()!=null)
			{
			head=head.getNext();
				return head;
			}
			return null;
		}
		Solar prev=head,temp;
		for(temp=head.getNext();temp!=galaxy.getSsential();temp=temp.getNext())
		{
			if(temp==Node)
				break;
			prev=prev.getNext();
		}
		prev.setNext(temp.getNext());
		return head;
	}
	
	public void deletePlanet()
	{
		Planet prev=this.getPlanets(),temp;
		for(Planet current=prev.getNext();current!=null;current=current.getNext())
		{
			temp=prev;
			prev=null;
			prev=temp.getNext();
		}
	}

}
