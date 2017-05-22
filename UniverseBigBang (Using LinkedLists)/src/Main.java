import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @file    Main.java
 * @author  Antonis Papaioannou <papaioan@csd.uoc.gr>
 *
 * @brief   Main function for the needs of cs-240a 2014
 */

public class Main {
    
    public static boolean DEBUG = true;
    public static Galaxy galaxies[]; 
    public static int tempindex=0;
    
    
    /**
     * @brief Initializes the system.
     *
     * @return true on success
     *         false on failure
     */
    public static boolean big_bang() {
    	galaxies=new Galaxy[1000];
        return true;
    }
    
    /**
    * @brief Create a new galaxy and add it to the universe
    *
    * @param gid The new galaxy's id
    *
    * @return true on success
    *         false on failure
    */
    public static boolean galaxy_creation(int gid) {
    	Galaxy galaxy=new Galaxy(gid);
        galaxies[Galaxy.getIndex()]=galaxy;
        galaxy.setIndex(Galaxy.getIndex()+1);
        tempindex=Galaxy.getIndex();
        return true;
    }

    /**
    * @brief Create a new solar system and add it to the galaxy with id gid
    *
    * @param sid The new solar system's star id
    * @param gid The galaxy's id to add the new solar system
    *
    * @return true on success
    *         false on failure
    */
    public static boolean star_birth(int sid, int gid) {
    	Solar new_Solar=new Solar(sid);
    	int galaxyId=Main.LookUpGid(gid);
    	Solar headSolar=galaxies[galaxyId].getSolars();
    	headSolar=galaxies[galaxyId].getSolars().insertSolar(new_Solar, galaxies[Main.LookUpGid(gid)].getSolars(),galaxies[galaxyId].getSsential());
    	
    	
    	if(headSolar!=null)
    	{
    		galaxies[galaxyId].setSolars(headSolar);
    		System.out.println("S "+sid+" "+gid);
    		System.out.print("Solars= ");
    		for(Solar temp=headSolar;temp!=galaxies[galaxyId].getSsential();temp=temp.getNext())
    		{
    			System.out.print(temp.getSid()+" ");
    		}
    		System.out.println();
    		return true;
    		
    	}
    	return false;
    	
           
        
    }

   /**
    * @brief Create a new planet in the solar system with id sid
    *
    * @param pid      The new planet's id
    * @param distance The new planet's distance from the solar system's star
    * @param sid      The solar system's id to add the new planet
    *
    * @return true on success
    *         false on failure
    */
    public static boolean planet_creation(int pid, int distance, int sid) {
    	
    	Planet newPlanet =new Planet(pid,distance);
    	
    	Solar temp=Main.LookUpSolarInUniverse(sid);
    	Planet headPlanet=temp.getPlanets();
    	
    	headPlanet=headPlanet.insertPlanet(newPlanet, headPlanet);
    	
		/*######PRINTS####*/
    	System.out.println("P "+ pid + " "+ distance+ " "+ sid);
    	if(headPlanet!=null)
    	{
    		temp.setPlanets(headPlanet);
    		for(Planet printtemp=headPlanet;printtemp!=null;printtemp=printtemp.getNext())
    		{
    			if(printtemp!=null)
    				System.out.print(printtemp.getPid()+" : "+printtemp.getDistance()+" : ");
    			if(printtemp.getPrev()!=null)
    				System.out.print(printtemp.getPrev().getPid()+" : ");
    			else
    				System.out.print("0 : ");
    			if(printtemp.getNext()!=null)
    				System.out.print(printtemp.getNext().getPid());
    			else
    				System.out.print("0");
    			System.out.println();
    		}
    		System.out.println();
    		return true;
    	}
    	return false;
    	
    	
    	
        
    }

    /**
    * @brief Delete a solar system and delete all planets in range
    * distance from the solar system's star-sun. The remaining planets
    * form a new orphan planets cluster in the galaxy.
    *
    * @param sid      The solar system's id
    * @param distance The range in which to delete the planets
    *
    * @return true on success
    *         false on failure
    */
    public static boolean star_death(int sid, int distance) {
    	int dis=0;
    	Planet temp;
    	Ocluster OC = null;
    	Galaxy tempGalaxy=Main.LookUpGalaxyViaSolar(sid);
    	Ocluster tempOcluster[]=tempGalaxy.getOclusters();
    	Solar headSolar=Main.LookUpSolarInUniverse(sid);
    	for(int i=0;i<tempGalaxy.getOclusters().length;i++)
    	{
    		
    		if(tempOcluster[i]==null)
    			tempOcluster[i]=new Ocluster();		
    	}
    	
    	for(temp=headSolar.getPlanets();temp!=null;temp=temp.getNext())
    	{
    		dis=dis+temp.getDistance();
    		
    		if(dis>distance)
    			break;
    	}
    	
    	for(int i=0;i<tempGalaxy.getOclusters().length;i++)
    	{
    		if(tempOcluster[i].getCid()==Integer.MAX_VALUE)
    		{
    			tempOcluster[i].setCid(sid);
    			OC=tempOcluster[i];
    			break;
    		}
    	}
    			Planet headOrphan=OC.getOrphans();
    			
    			for(Planet NodePlanet=temp;NodePlanet!=null;NodePlanet=NodePlanet.getNext())
    			{
    				Planet newnode=new Planet(NodePlanet.getPid());
    				
    				headOrphan=headOrphan.insertOrphan(newnode, headOrphan);
    				if(headOrphan!=null)
    					OC.setOrphans(headOrphan);	 
    			}
    			
    			Galaxy gsolar=Main.LookUpGalaxyViaSolar(sid);
    			Solar psolar=gsolar.getSolars();
    			psolar.deletePlanet();
    			psolar=psolar.deleteSolar(psolar, headSolar, tempGalaxy);
    			tempGalaxy.setSolars(psolar);
    			
    			/*######PRINTS####*/
    			System.out.println("D  " + sid+" "+ distance);
    			System.out.print("Solars = ");
    			for(Solar tmp=tempGalaxy.getSolars();tmp!=tempGalaxy.getSsential();tmp=tmp.getNext())
    			{
    				System.out.print(tmp.getSid()+" ");
    			}
    			System.out.println();
    			System.out.print("OrphansC = ");
    			Ocluster OcArray[]=tempGalaxy.getOclusters();
    			
    			for(int i=0;i<tempGalaxy.getOclusters().length;i++)
    			{
    				if(OcArray[i].getCid()==Integer.MAX_VALUE)
	    				break;
    				System.out.print(OcArray[i].getCid()+" ");
    				
    			}
    			System.out.println();
    			System.out.print("Orphans = ");
    			for(Planet OCtmp=OC.getOrphans();OCtmp!=null;OCtmp=OCtmp.getNext())
    			{
    				System.out.print(OCtmp.getPid()+" ");
    			}
    			System.out.println();
    			
    			
    	 return true;
    }

   /**
    * @brief Delete the orphan planet with id oid and the planet with id
    * pid
    *
    * @param oid The orphan planet's id
    * @param pid The planet's id
    *
    * @return true on success
    *         false on failure
    */
    public static boolean planet_orphan_crash(int oid, int pid) {
    	Planet planetToDelete=Main.LookUpPlanet(pid);
    	Solar solar=Main.LookUpSolarviaPlanet(pid);
    	Planet PlanetHead=solar.getPlanets();
    	System.out.println("solar:"+solar.getSid());
    	System.out.println("planetToDelete:"+planetToDelete.getPid());
    	PlanetHead=PlanetHead.deletePlanet(PlanetHead, planetToDelete);
    	System.out.println("planethead:"+PlanetHead.getPid());
    	if(PlanetHead!=null)
    		solar.setPlanets(PlanetHead);
    	
    	Planet OrphanTodelete=Main.LookUpOrphanInUniverse(oid);
    	Ocluster ocluster=Main.LookUpOclusterInUniverseViaOrphan(oid);
    	Planet OrphanHead=ocluster.getOrphans();
    	
    	
    	
    	
    	
    	OrphanHead=OrphanHead.deleteOrphan(OrphanHead, OrphanTodelete);
    	
    	if(OrphanHead!=null)
    		ocluster.setOrphans(OrphanHead);
        return true;
    }

   /**
    * @brief Split an orphan planets cluster in two
    *
    * @param cid1 The, to split, orphan planets cluster's id
    * @param cid2 The orphan planets cluster's id for the first new cluster
    * @param cid3 The orphan planets cluster's id for the second new cluster
    *
    * @return true on success
    *         false on failure
    */
    public static boolean orphans_cluster_crash(int cid1, int cid2, int cid3) {
        return true;
    }

   /**
    * @brief Merge two galaxies
    *
    * @param gid1 The first galaxy's id
    * @param gid2 The second galaxy's id
    *
    * @return true on success
    *         false on failure
    */
    public static boolean galaxy_merger(int gid1, int gid2) {
    	System.out.println("GALAXY1: "+galaxies[0].getGid());
    	System.out.println("GALAXY2: "+galaxies[1].getGid());
    	
    	int pos=Main.positionInGalaxies(gid2);
    	System.out.println("POS:"+pos);
    	Galaxy galaxy1=Main.LookUpGalaxyInUniverse(gid1);
    	Galaxy galaxy2=Main.LookUpGalaxyInUniverse(gid2);
    	System.out.println("M "+gid1+ " "+gid2);
    	
    	System.out.print("Solars1 = ");
    	for(Solar printSolar=galaxy1.getSolars();printSolar!=galaxy1.getSsential();printSolar=printSolar.getNext())
    	{
    		if(printSolar!=null)
    			System.out.print(printSolar.getSid()+", ");
    	}
    	System.out.println();
    	System.out.print("OrphansC1 = ");
    	Ocluster OclusterArray[]=galaxy1.getOclusters();
    	for(int i=0;i<galaxy1.getOclusters().length;i++)
    	{
    		if(OclusterArray[i]!=null)
    		System.out.print(OclusterArray[i].getCid()+", ");
    	}
    	
    	System.out.println();
    	System.out.print("Solars2 = ");
    	for(Solar printSolar=galaxy2.getSolars();printSolar!=galaxy2.getSsential();printSolar=printSolar.getNext())
    	{
    		if(printSolar!=null)
    			System.out.print(printSolar.getSid()+", ");
    	}
    	System.out.println();
    	System.out.print("OrphansC2 = ");
    	Ocluster OclusterArray2[]=galaxy2.getOclusters();
    	for(int i=0;i<galaxy2.getOclusters().length;i++)
    	{
    		if(OclusterArray2[i]!=null)
    		System.out.print(OclusterArray2[i].getCid()+", ");
    	}
    	System.out.println();
    	
    	if(galaxy2.getSolars()!=null)
    	{
    		Solar tail1=galaxy1.getSsential().getNext();
    		tail1.setNext(galaxy2.getSolars());
    		galaxy1.setSsential(galaxy2.getSsential());

    	}
    	/*delete*/
    	for(Solar deletesolar=galaxy2.getSolars();deletesolar!=galaxy2.getSsential();deletesolar=deletesolar.getNext())
    	{
    		for(Planet deleteplanet=deletesolar.getPlanets().getNext();deleteplanet!=null;deleteplanet=deleteplanet.getNext())
    		{
    			Planet prev=deleteplanet.getPrev();
    			prev=null;
    			
    		}
    		
    	}
    	galaxy2.setSolars(null);
    	galaxy2.setOclusters(null);
    	galaxy2.setSsential(null);
    	galaxy2=null;
    	
    	if((galaxies[pos+1])!=null)
    	{
    		galaxies[pos]=galaxies[Galaxy.getIndex()-1];
    		galaxies[Galaxy.getIndex()-1]=null;
    		
    		galaxies[pos].setIndex(Galaxy.getIndex()-1);
    		Main.tempindex=Galaxy.getIndex();
    		
    	}
    	System.out.print("Solars3 = ");
    	for(Solar printSolar=galaxy1.getSolars();printSolar!=galaxy1.getSsential();printSolar=printSolar.getNext())
    	{
    		if(printSolar!=null)
    			System.out.print(printSolar.getSid()+", ");
    	}
    	System.out.println();
    	System.out.print("OrphansC3 = ");
    	Ocluster OclusterArray3[]=galaxy1.getOclusters();
    	for(int i=0;i<galaxy1.getOclusters().length;i++)
    	{
    		if(OclusterArray[i]!=null)
    		System.out.print(OclusterArray3[i].getCid()+", ");
    	}
    	System.out.println();
    	System.out.println("DONE");
    	System.out.println();
      return true;
    }

   /**
    * @brief Find a planet in the universe
    *
    * @param pid The planet's id
    *
    * @return true on success
    *         false on failure
    */
    public static boolean lookup_planet(int pid) {
    	Solar tempsolar;
    	for(int i=0;i<Galaxy.getIndex();i++)
    	{
    		for(tempsolar=galaxies[i].getSolars();tempsolar!=galaxies[i].getSsential();tempsolar=tempsolar.getNext())
    		{
    			for(Planet tempplanet=tempsolar.getPlanets();tempplanet!=null;tempplanet=tempplanet.getNext())
    			{
    				if(tempplanet.getPid()==pid)
    				{
    					System.out.println("L "+pid);
    					System.out.println(galaxies[i].getGid()+" : "+tempsolar.getSid()+" : "+" : "+ tempplanet.getDistance()+" : "+" : "+tempplanet.getPrev().getPid()+" : "+tempplanet.getNext().getPid());
    					System.out.println("DONE");
    					System.out.println();
    					return true;
    				}
    				
    			}
    		}
    	}
    	System.out.println("Planet: "+pid+" NotFound");
        return false;
    }

    /**
    * @brief Find all planets in range distance from the planet with id pid
    *
    * @param pid      The planet's id
    * @param distance The search range
    *
    * @return true on success
    *         false on failure
    */
    public static boolean find_planets(int pid, int distance) {
    	int counter=0;
    	Planet planet=Main.LookUpPlanet(pid);
    	System.out.println("PLANET=:"+planet.getPid());
    	if (planet!=null)
    	{
    		System.out.println("F "+ pid+ " "+distance);
    		System.out.println("Previous Planets");
        	for(Planet prevplanet=planet.getPrev();prevplanet!=null;prevplanet=prevplanet.getPrev())
        	{
        		counter=counter+prevplanet.getDistance();
        		if(counter>distance)
        			break;
        		System.out.println(prevplanet.getPid()+" "+prevplanet.getDistance()+" : "+prevplanet.getPrev().getPid()+" : "+prevplanet.getNext().getPid());
        			
        	}
        	counter=0;
        	System.out.println("Next Planets");
        	for(Planet nextplanet=planet.getNext();nextplanet!=null;nextplanet=nextplanet.getNext())
        	{
        		counter=counter+nextplanet.getDistance();
        		if(counter>distance)
        			break;
        		System.out.println(nextplanet.getPid()+" "+nextplanet.getDistance()+" : "+nextplanet.getPrev().getPid()+" : "+nextplanet.getNext().getPid());
        			
        	}
        	System.out.println("DONE");
            return true;
    	}
    	return false;
    	
    }

    /**
    * @brief Print a solar system
    *
    * @param sid The solar system's id
    *
    * @return true on success
    *         false on failure
    */
    public static boolean print_solar(int sid) {
    	Solar solar=Main.LookUpSolarInUniverse(sid);
    	System.out.println("  H "+ sid);
    	System.out.println("  Planets = ");
    		for(Planet printtemp=solar.getPlanets();printtemp!=null;printtemp=printtemp.getNext())
        	{
        		if(printtemp!=null && printtemp.getPid()!=-1)
    				System.out.print("	"+printtemp.getPid()+" : "+printtemp.getDistance()+" : ");
    			if(printtemp.getPrev()!=null )
    				System.out.print(printtemp.getPrev().getPid()+" : ");
    			else
    				System.out.print("0 : ");
    			if(printtemp.getNext()!=null)
    				System.out.print(printtemp.getNext().getPid());
    			else
    				System.out.print("0");
    			System.out.println();
        	
    		
    	}
    	return true;
    	
        
    }

    /**
    * @brief Print an orphan planets cluster
    *
    * @param cid The orphan planets cluster's id
    *
    * @return true on success
    *         false on failure
    */
    public static boolean print_ocluster(int cid) {
    	Ocluster temp=Main.LookUpOclusterInUniverse(cid);
    	System.out.println("  I "+cid);
    	System.out.println("  Orphans =");
    	if (temp!=null)
    	{
    		for(Planet orphanTemp=temp.getOrphans();orphanTemp!=null;orphanTemp=orphanTemp.getNext())
    		{
    			if(orphanTemp!=null && orphanTemp.getPid()!=-1)
    				System.out.print("   "+orphanTemp.getPid()+" : ");
    			if(orphanTemp.getPrev()!=null)
    				System.out.print(orphanTemp.getPrev().getPid()+" : ");
    			else
    				System.out.print("0 : ");
    			if(orphanTemp.getNext()!=null)
    				System.out.print(orphanTemp.getNext().getPid());
    			else
    				System.out.print("0");
    			System.out.println();
    		}
    		System.out.println("DONE");
        	return true;
    	}
    	return false;
		
    }

   /**
    * @brief Print a galaxy
    *
    * @param gid The galaxy's id
    *
    * @return true on success
    *         false on failure
    */
    public static boolean print_galaxy(int gid) {
    	Galaxy galaxy=Main.LookUpGalaxyInUniverse(gid);
    	if(galaxy!=null)
    	{
    		System.out.println("J "+gid);
        	System.out.println("Solars = ");

        	if(galaxy.getSolars().getSid()!=0)
        	{
        		for(Solar printSolar=galaxy.getSolars();printSolar!=galaxy.getSsential();printSolar=printSolar.getNext())
            	{
            		if(printSolar!=null)
            			Main.print_solar(printSolar.getSid());
            	}
        	}
        	
        	System.out.println("Orphan planets clusters = ");
        	Ocluster OclusterArray[]=galaxy.getOclusters();
        	for(int i=0;i<galaxy.getOclusters().length;i++)
        	{
        		if(OclusterArray[i]!=null)
        		{
        			if(OclusterArray[i].getCid()==Integer.MAX_VALUE)
        	    		break;
            		Main.print_ocluster(OclusterArray[i].getCid());
        		}
        		
        	}
        	
        	return true;
    	}
    	return false;
  
   }

   /**
    * @brief Print the universe
    *
    * @return true on success
    *         false on failure
    */
    public static boolean print_universe() {
    	System.out.println("U");
    	for(int i=0;i<Galaxy.getIndex();i++)
 	   	{
    		Main.print_galaxy(Main.galaxies[i].getGid());
 	   	}
        return true;
    }

   /**
    * @brief Finalize the system, empty and free all the data structures
    *
    * @return true on success
    *         false on failure
    */
    public static boolean end_of_the_world() {
        return true;
    }
    
    public static int LookUpGid(int gid)
	{
		for(int i=0;i<tempindex;i++)
		{	
			if(Main.galaxies[i].getGid()==gid)
				return i;
		}
		 return -1;
    	
	}
    
    public static Solar LookUpSolarInUniverse(int sid)
    {
    	for(int i=0;i<Galaxy.getIndex();i++)
    	{
    		Main.galaxies[i].getSsential().setSid(sid);
    		for(Solar tempSolar=Main.galaxies[i].getSolars();tempSolar!=galaxies[i].getSsential();tempSolar=tempSolar.getNext())
    		{
    			if(tempSolar.getSid()==sid && tempSolar!=Main.galaxies[i].getSsential())
    				return tempSolar;
    		}	
    		
    	}
    	
    	return null;
    }
    
    public static Galaxy LookUpGalaxyViaSolar(int sid)
    {
    	for(int i=0;i<Main.tempindex;i++)
    	{
    		
    		Main.galaxies[i].getSsential().setSid(sid);
    		for(Solar tempSolar=Main.galaxies[i].getSolars();tempSolar!=null;tempSolar=tempSolar.getNext())
    		{
    			if(tempSolar.getSid()==sid && tempSolar!=Main.galaxies[i].getSsential())
    				return Main.galaxies[i];
    		}	
    		
    	}
    	
    	return null;	
    }
	
    public static Ocluster LookUpOclusterInUniverse(int cid)
    {
    	
    	for(int i=0;i<Main.tempindex;i++)
    	{
    		Ocluster array[]=Main.galaxies[i].getOclusters();
    		
    		for(int j=0;j<array.length;j++)
    		{
    			if(array[j].getCid()!=Integer.MAX_VALUE)
    				
    			{
    				if(array[j].getCid()==cid)
    					return array[j];
    			}
    			else break;
    		}
    	}
    	return null;
    }
    
    public static Ocluster LookUpOclusterInUniverseViaOrphan(int oid)
    {
    	
    	for(int i=0;i<Galaxy.getIndex();i++)
    	{
    		Ocluster array[]=Main.galaxies[i].getOclusters();
    		for(int j=0;j<array.length;j++)
    		{
    			if(array[j].getCid()!=Integer.MAX_VALUE)
    			{
    				for(Planet tempOrphan=array[j].getOrphans();tempOrphan!=null;tempOrphan=tempOrphan.getNext())
    				{
    					
    					if(tempOrphan.getPid()==oid) 
    					{
    						
    						return array[j];
    					}
    					if(tempOrphan.getPid()>oid) 
    					{
    						break;
    					}
    				}
    			}
    			else break;
    		}
    	}
    	return null;
    }
    
    public static Planet LookUpOrphanInUniverse(int pid)
    {
    	for(int i=0;i<Galaxy.getIndex();i++)
    	{
    		Ocluster array[]=Main.galaxies[i].getOclusters();
    		for(int j=0;j<array.length;j++)
    		{
    			if(array[j].getCid()!=Integer.MAX_VALUE)
    			{
    				
    				for(Planet tempOrphan=array[j].getOrphans();tempOrphan!=null;tempOrphan=tempOrphan.getNext())
    				{
    					
    					if(tempOrphan.getPid()==pid) 
    					{
    						System.out.println("tempOrphan.getPid():"+tempOrphan.getPid()+"=="+pid);
    						return tempOrphan;
    					}
    					if(tempOrphan.getPid()>pid) 
    					{
    						break;
    					}
    					
    					
    				}
    			}
    			else break;
    		}
    	}
    	return null;
    }
    
    public static Galaxy LookUpGalaxyInUniverse(int gid)
    {
    	for(int i=0;i<Main.tempindex;i++)
    	{
    		if(Main.galaxies[i].getGid()==gid)
    			return Main.galaxies[i];
    	}
    	return null;
    }
    
    public static int positionInGalaxies(int gid)
    {
    	for(int i=0;i<Main.tempindex;i++)
    	{
    		if(Main.galaxies[i].getGid()==gid)
    			return i;
    	}
    	return -1;
    }
    
    public static Planet LookUpPlanet(int pid)
    {
    	for(int i=0;i<Main.tempindex;i++)
    	{
    		for(Solar tempsolar=galaxies[i].getSolars();tempsolar!=galaxies[i].getSsential();tempsolar=tempsolar.getNext())
    		{
    			for(Planet tempplanet=tempsolar.getPlanets();tempplanet!=null;tempplanet=tempplanet.getNext())
    			{
    				if(tempplanet.getPid()==pid)
    				{
    					return tempplanet;
    				}
    				
    			}
    		}
    	}
        return null;
    }
    
    public static Solar LookUpSolarviaPlanet(int pid)
    {
    	for(int i=0;i<Main.tempindex;i++)
    	{
    		for(Solar tempsolar=galaxies[i].getSolars();tempsolar!=galaxies[i].getSsential();tempsolar=tempsolar.getNext())
    		{
    			for(Planet tempplanet=tempsolar.getPlanets();tempplanet!=null;tempplanet=tempplanet.getNext())
    			{
    				if(tempplanet.getPid()==pid)
    				{
    					return tempsolar;
    				}
    			}
    		}
    	}
        return null;
    }

////////////////////////////////////////////////////////////////////////////////
    public static void DPRINT(String s) {
        if (DEBUG) { System.out.println(s); }
    } 
   

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader inputFile;
        String line;
        String [] params;
        

        System.out.println();
	/* Check command buff arguments */       
        if (args.length != 1) {
            System.err.println("Usage: <executable-name> <input_file>");
            System.exit(0);
        }

	/* Open input file */        
        inputFile = new BufferedReader(new FileReader(args[0]));

	/* Read input file buff-by-buff and handle the events */
        while ( (line = inputFile.readLine()) != null ) {

            System.out.println("Event: " + line);
            params = line.split(" ");
            char eventID = line.charAt(0);

            switch(eventID) {

		/* Comment */
		case '#':
			break;

		/* Big Bang
		 * B */
		case 'B':
		{
			if ( big_bang() ) {
							
							System.out.println("B Done");
							
                            //DPRINT("B succeeded\n");
							System.out.println();
			} else {
                            System.out.println("B failed");
			}

			break;
		}

		/* Create a new Galaxy
		 * G <gid> */
		case 'G':
		{
					
                    int gid = Integer.parseInt(params[1]);
                    assert(gid > 0);
                        //DPRINT(eventID + " " + gid);
                        

			if ( galaxy_creation(gid) ) {
				System.out.println("G "+gid);
				System.out.print("Galaxies ="); 
					for(int i=0;i<Main.tempindex;i++)
					{
						System.out.print(Main.galaxies[i].getGid()+" ");
					}
					System.out.println();
					System.out.println();
                      		
                            
                            //DPRINT(eventID + " " + gid + " succeeded");
                            
			} else {
                            System.out.println(eventID + " " + gid + " failed");
			}

			break;
		}

		/* Create a new Star
		 * S <sid> <gid> */
		case 'S':
		{
                    int sid = Integer.parseInt(params[1]);
                    int gid = Integer.parseInt(params[2]);
                    assert(sid > 0);
                    assert(gid > 0);
                    //DPRINT(eventID + " " + sid + " " + gid);
                    
                   
                    

                    if ( star_birth(sid, gid) ) {
                    		//DPRINT(eventID + " " + sid + " " + gid + " succeeded");
                    	System.out.println();
                    		
                        
                    } else {
                        System.out.println(eventID + " " + sid + " " + gid + " failed");
                    }

                    break;
		}

		/* Create a new Planet
		 * P <pid> <distance> <sid> */
		case 'P':
		{
                    int pid = Integer.parseInt(params[1]);
                    int distance = Integer.parseInt(params[2]);
                    int sid = Integer.parseInt(params[3]);
                    assert(pid > 0);
                    assert(sid > 0);
                    //DPRINT(eventID + " " + pid + " " + distance + " " + sid);

                    if ( planet_creation(pid, distance, sid) ) {
                    	     	
                        //DPRINT(eventID + " " + pid + " " + distance + " " + sid + " succeeded");
                    	System.out.println();
                    } else {
                        System.out.println(eventID + " " + pid + " " + distance + " " + sid + " failed");
                    }

                    break;
		}

		/* Delete a solar system
		 * D <sid> <distance> */
		case 'D':
		{
					
                    int sid = Integer.parseInt(params[1]);
                    int distance = Integer.parseInt(params[2]);
                    assert(sid > 0);
                    //DPRINT(eventID + " " + sid + " " + distance);

                    if ( star_death(sid, distance) ) {
                        //DPRINT(eventID + " " + sid + " " + distance + " succeeded");
                    	System.out.println();
                    } else {
                        System.out.println(eventID + " " + sid + " " + distance + " failed");
                    }

                    break;
		}

		/* Trigger an orphan planet crash with a planet
		 * O <oid> <pid> */
		case 'O':
		{
                    int oid = Integer.parseInt(params[1]);
                    int pid = Integer.parseInt(params[2]);
                    assert(oid > 0);
                    assert(pid > 0);
                    DPRINT(eventID + " " + oid + " " + pid);

                    if ( planet_orphan_crash(oid, pid) ) {
                    	
                    	
                        DPRINT(eventID + " " + oid + " " + pid + " succeed");
                    } else {
                        System.out.println(eventID + " " + oid + " " + pid + " failed");
                    }
                    
                    break;
		}

		/* Trigger an orphan planet cluster split
		 * C <cid1> <cid2> <cid3> */
		case 'C':
		{
                    int [] cid = new int[3];
                    cid[0] = Integer.parseInt(params[1]);
                    cid[1] = Integer.parseInt(params[2]);
                    cid[2] = Integer.parseInt(params[3]);
                    assert(cid[0] > 0);
                    assert(cid[1] > 0);
                    assert(cid[2] > 0);
                    DPRINT(eventID + " " + cid[0] + " " + cid[1] + " " + cid[2]);

                    if ( orphans_cluster_crash(cid[0], cid[1], cid[2]) ) {
                        DPRINT(eventID + " " + cid[0] + " " + cid[1] + " " + cid[2] + " succeeded");
                    } else {
                        System.out.println(eventID + " " + cid[0] + " " + cid[1] + " " + cid[2] + " failed");
                    }

                    break;
		}

		/* Trigger the merge of two galaxies
		 * M <gid1> <gid2> */
		case 'M':
		{
                    int gid1 = Integer.parseInt(params[1]);
                    int gid2 = Integer.parseInt(params[2]);
                    assert(gid1 > 0);
                    assert(gid2 > 0);
                    //DPRINT(eventID + " " + gid1 + " " + gid2);

                    if ( galaxy_merger(gid1, gid2) ) {
                        //DPRINT(eventID + " " + gid1 + " " + gid2 + " succeed");
                    } else {
                        System.out.println(eventID + " " + gid1 + " " + gid2 + " failed");
                    }

                    break;
		}

		/* Lookup a planet
		 * L <pid> */
		case 'L':
		{
                    int pid = Integer.parseInt(params[1]);
                    assert(pid > 0);
                    DPRINT(eventID + " " + pid);

                    if ( lookup_planet(pid) ) {
                        DPRINT(eventID + " " + pid + " succeeded");
                    } else {
                        System.out.println(eventID + " " + pid + " failed");
                    }

                    break;
		}

		/* Find planets in range
		 * F <pid> <distance> */
		case 'F':
		{
                    int pid = Integer.parseInt(params[1]);
                    int distance = Integer.parseInt(params[2]);
                    assert(pid > 0);
                    //DPRINT(eventID + " " + pid + " " + distance);
                    

                    if ( find_planets(pid, distance) ) {
                    	System.out.println();
                    	
                        //DPRINT(eventID + " " + pid + " " + distance + " succeeded");
                    } else {
                        System.out.println(eventID + " " + pid + " " + distance + " failed");
                    }

                    break;
		}

		/* Print a solar system
		 * H <sid> */
		case 'H':
		{
                    int sid = Integer.parseInt(params[1]);
                    assert(sid > 0);
                    //DPRINT(eventID + " " + sid);

                    if ( print_solar(sid) ) {
                        //DPRINT(eventID + " " + sid + " succeeded");
                    } else {
                        System.out.println(eventID + " " + sid + " failed");
                    }
                    
                    break;
		}

		/* Print a orphan planets cluster
		 * I <cid> */
		case 'I':
		{
                    int cid = Integer.parseInt(params[1]);
                    assert(cid > 0);
                    DPRINT(eventID + " " + cid);

                    if ( print_ocluster(cid) ) {
                        DPRINT(eventID + " " + cid + " succeeded");
                    } else {
                        System.out.println(eventID + " " + cid + " failed");
                    }

                    break;
		}

		/* Print a galaxy
		 * J <gid> */
		case 'J':
		{
                    int gid = Integer.parseInt(params[1]);
                    assert(gid > 0);
                    DPRINT(eventID + " " + gid);

                    if ( print_galaxy(gid) ) {
                    	System.out.println();
                        //DPRINT(eventID + " " + gid + " succeeded");
                    } else {
                        System.out.println(eventID + " " + gid + " failed");
                    }

                    break;
		}

		/* Print universe
		 * U */
		case 'U':
			
			
		{			
			       if ( print_universe() ) {
			    	   
			             
			DPRINT("U succeeded\n");
			System.out.println();
                    } else {
                        System.out.println("U failed");
                    }

                    break;
		}

		/* End of the World (Optional)
		 * E */
		case 'E':
		{
                    if ( end_of_the_world() ) {
			DPRINT("E succeeded\n");
                    } else {
                        System.out.println("E failed");
                    }

                    break;
		}

		/* Empty line */
		case '\n':
			break;

		/* Ignore everything else */
		default:
                    DPRINT("Ignoring " + line);

                    break;
		}
	}
        
       

    }
           
}
