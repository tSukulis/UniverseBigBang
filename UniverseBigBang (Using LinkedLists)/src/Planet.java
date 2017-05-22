
public class Planet {
	private int pid;
	private int distance;
	private Planet prev;
	private Planet next;
	
	
	public Planet(int Pid,int Distance)
	{
		this.pid=Pid;
		this.distance=Distance;
		this.prev=null;
		this.next=null;
	}
	public Planet(int Pid)
	{
		this.pid=Pid;
		this.distance=0;
		this.prev=null;
		this.next=null;
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
	public Planet getPrev() {
		return prev;
	}
	public void setPrev(Planet prev) {
		this.prev = prev;
	}
	public Planet getNext() {
		return next;
	}
	public void setNext(Planet next) {
		this.next = next;
	}
	
	public void SetData(Planet p)
	{
		this.setDistance(p.getDistance());
		this.setPid(p.getPid());
	}
	
	
	public Planet  insertPlanet (Planet newNode,Planet head)
	{
		if(head.getPid()==-1)
		{
			head=newNode;
			return head;
		}
		else if(newNode.getDistance()<head.getDistance())
		{
			newNode.setNext(head);
			head.setPrev(newNode);
			head.setDistance(head.getDistance()-newNode.getDistance());
			return newNode;
		}
		else
		{
			Planet previous=null;
			int distance=0;
			for(Planet temp=head;temp!=null;temp=temp.getNext())
			{
				previous=temp;
				distance=distance+temp.getDistance();
				if(distance>newNode.getDistance())
				{
					if(temp.getPrev()!=null)
					{
						newNode.setNext(temp);
						temp.getPrev().setNext(newNode);
						newNode.setPrev(temp.getPrev());
						temp.setPrev(newNode);
						newNode.setDistance(newNode.getDistance()-distance+temp.getDistance());
						return head;
						
					}
				}
			}
			previous.setNext(newNode);
			newNode.setPrev(previous);
			newNode.setDistance(newNode.getDistance()-distance);
			return head;
		}
		
	}
	
	public Planet  insertOrphan (Planet newNode,Planet head)
	{
		if(head==null || head.getPid()==-1)
		{
			head=newNode;
			return head;
		}
		
		else if(newNode.getPid()<head.getPid())
		{
			newNode.setNext(head);
			head.setPrev(newNode);
			return newNode;
		}
		
		else
		{
			Planet previous=null;
			for(Planet temp=head;temp!=null;temp=temp.getNext())
			{
				previous=temp;
				
				if(temp.getPid()>newNode.getPid())
				{
					if(temp.getPrev()!=null)
					{
						newNode.setNext(temp);
						temp.getPrev().setNext(newNode);
						newNode.setPrev(temp.getPrev());
						temp.setPrev(newNode);
						return head;
					}
				}
			}
			previous.setNext(newNode);
			newNode.setPrev(previous);
			
			return head;
		}
	}
	
	public Planet deletePlanet(Planet head,Planet Node)
	{
		if(Node==head)
		{
			
			Planet newhead=head.getNext();
			newhead.setDistance(head.getDistance()+newhead.getDistance());
			newhead.setPrev(null);
			return newhead;
		}
		if(Node.getNext()==null)
		{
			Node.setPrev(null);
			Node.setNext(null);
			Node=null;
			return head;
		}
		Node.getPrev().setNext(Node.getNext());
		Node.getNext().setDistance(Node.getDistance()+Node.getNext().getDistance());
		Node.getNext().setPrev(Node.getPrev());
		Node=null;
		return head;
		
	}
	
	public Planet deleteOrphan(Planet head,Planet Node)
	{
		if(Node==head)
		{
			Planet newHead=head.getNext();
			newHead.setPrev(null);
			return newHead;
		}
		if(Node.getNext()==null)
		{
			Node.setPrev(null);
			Node.setNext(null);
			Node=null;
			return head;
		}
		Node.getPrev().setNext(Node.getNext());
		Node.getNext().setDistance(Node.getDistance()+Node.getNext().getDistance());
		Node.getNext().setPrev(Node.getPrev());
		Node=null;
		return head;
		
	}

}
