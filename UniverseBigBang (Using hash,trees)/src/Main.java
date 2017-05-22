
//package cs240_project_a;

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

	static int ht_upper_g; /**< The upper limit of the chains'
	                        * size for the rehash BONUS */
	static int ht_lower_g; /**< The lower limit of the chains'
	                        * size for the rehash BONUS */
	static int phashtable_size_g; /**< The size of the orphan planets
	                               * hashtable, parsed from the command
	                               * line (>0) */
	static int max_id_g; /**< The maximum planet ID */
	public static Galaxy Galaxies[]; 
	public static Planet tmpRootOrphan;
	public static Planet gLbPlanet;
	public static Planet gLbOrphan,prevOrphan,nextOrphan;
	public static Planet root1;
	public static Planet root2;
	

	/*static account_t ahashtable_p[];*/ /**< The accounts hashtable (pinakas
	                                      * katakermatismou logariasmwn). This
	                                      * is an array of lists (chains) */

	// This is a very conservative progress on the hashtable. Our purpose
	// is to force many rehashes to check the stability of the code.
	static int primes_g[] = { 5, 7, 11, 13, 17, 19, 23, 29, 31, 37,
	                          41, 43, 47, 53, 59, 61, 67, 71, 73, 79,
	                          83, 89, 97, 101, 103, 107, 109, 113, 127, 131,
	                          137, 139, 149, 151, 157, 163, 167, 173, 179, 181,
	                          191, 193, 197, 199, 211, 223, 227, 229, 233, 239,
	                          241, 251, 257, 263, 269, 271, 277, 281, 283, 293,
	                          307, 311, 313, 317, 331, 337, 347, 349, 353, 359,
	                          367, 373, 379, 383, 389, 397, 401, 409, 419, 421,
	                          431, 433, 439, 443, 449, 457, 461, 463, 467, 479,
	                          487, 491, 499, 503, 509, 521, 523, 541, 547, 557,
	                          563, 569, 571, 577, 587, 593, 599, 601, 607, 613,
	                          617, 619, 631, 641, 643, 647, 653, 659, 661, 673,
	                          677, 683, 691, 701, 709, 719, 727, 733, 739, 743,
	                          751, 757, 761, 769, 773, 787, 797, 809, 811, 821,
	                          823, 827, 829, 839, 853, 857, 859, 863, 877, 881,
	                          883, 887, 907, 911, 919, 929, 937, 941, 947, 953};

	public static boolean DEBUG = true;

    /**
     * @brief Initializes the system.
     *
     * @return true on success
     *         false on failure
     */
    public static boolean big_bang() {
    	Galaxies=new Galaxy[1000];
    	for(int i=0;i<1000;i++)
    		Galaxies[i]=new Galaxy();
    	System.out.println("B DONE");
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
    	Galaxies[Galaxy.getIndex()].setGid(gid);
    	Galaxies[Galaxy.getIndex()].init(gid);
    	Galaxy.setIndex(Galaxy.getIndex()+1);
    	System.out.println("G "+gid);
    	System.out.println("Galaxies = ");
    	for(int i=0;i<Galaxy.getIndex();i++)
    	{
    		System.out.print(Galaxies[i].getGid()+",  ");
    	}
    	System.out.println("Galaxy.getIndex:"+Galaxy.getIndex());
    	System.out.println();
    	System.out.println("Done");
    	System.out.println();
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
    	if(galaxyId>=0)
    	{
    		Galaxies[galaxyId].insertSolar(new_Solar);
    		System.out.println("S "+sid+" "+gid);
    		System.out.print("Solars= ");
    		for(Solar temp=Galaxies[galaxyId].getSolars();temp!=Galaxies[galaxyId].getSsentinel();temp=temp.getNext())
    		{
    			System.out.print(temp.getSid()+", ");
    		}
    		System.out.println();
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
    	Planet newnode=new Planet(pid,distance);
    	Solar solarCurrent=LookUpSolarInUniverse(sid);
    	
    	Planet PlanetRoot=solarCurrent.getPlanets();
    	if (PlanetRoot==null)
    	{
    		PlanetRoot=Main.initPlanet(PlanetRoot);
    	}
    	PlanetRoot=PlanetRoot.insertPlanet(PlanetRoot, newnode, solarCurrent.getPsentinel());
    		if(PlanetRoot!=null)
    		{
    	    	Solar temp=LookUpSolarInUniverse(sid);
    	    	temp.setPlanets(PlanetRoot);
    			System.out.println("P "+pid+" "+distance+sid);
    			Main.inOrder_print_Solar(PlanetRoot, temp);
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
    	Solar solarCurrent=Main.LookUpSolarInUniverse(sid);
    	Planet PlanetRoot=solarCurrent.getPlanets();
    	int GalaxyId=Main.galaxyPoitionSolar(sid);
   	
    	if(PlanetRoot!=null)
    	{
    	       Main.postOrderAndDelete(solarCurrent.getPlanets(), solarCurrent.getPsentinel(), distance,Galaxies[GalaxyId]);
    	       Galaxies[GalaxyId].deleteSolar(solarCurrent);
    	       Main.printstar_death(Galaxies[GalaxyId], sid, distance);
    	       return true; 
    	}
	    return false;  
        
    }
    
    
    /*print of death_star Event*/
    public static void printstar_death(Galaxy galaxy,int sid, int distance)
    {
    	System.out.println("D "+sid+" "+distance);
    	System.out.print("Solars = ");
    	for(Solar current=galaxy.getSolars();current!=galaxy.getSsentinel();current=current.getNext())
    	{
        	System.out.print(current.getSid()+" ");
    	}
    	System.out.println();
    	System.out.print("Orphans = ");
    	Main.inOrder_print_Orphan(galaxy.getOrphans());
    	
    }
    
    /*prints the orphan tree */
    public static void inOrder_print_Orphan(Planet root)
    {
    	if(root == null ) 
    		return;
    	inOrder_print_Orphan( root.getLc());
    	System.out.print("	"+root.getPid()+", " );
    	inOrder_print_Orphan( root.getRc());  	
    }
    
    
    /*makes a postorder traversal to planet root
     * if the root->distance is greater than distance calls the UnsortedTreeInsert
     * to insert the node to Orphantree and then deletes the node
     * else just delete the node
     */
    public static void postOrderAndDelete(Planet root,Planet sentinel,int distance,Galaxy galaxy)
    {
    	if(root == null || root == sentinel) 
    	{
 	       return ;
    	}
        
    	postOrderAndDelete( root.getLc(),sentinel ,distance,galaxy);
    	postOrderAndDelete( root.getRc(),sentinel,distance,galaxy);
        if(root.getDistance()>distance)
        {
        	Planet newnode=new Planet(root.getPid(),0);
        	boolean flag=galaxy.UnsortedTreeInsert(newnode);
        	root=null;
        	 
        }
        else root=null;
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
        return true;
    }

   /**
    * @brief Binary star creation
    *
    * @param sid1 The, to split, solar system
    * @param distance
    * @param sid2 The id of the first new solar system
    * @param sid3 The id of the second new solar system
    *
    * @return true on success
    *         false on failure
    */
    public static boolean binary_star_creation(int sid1, int distance, int sid2, int sid3) {
    	
		Galaxy currentGalaxy=Main.LookUpGalaxyViaSolar(sid1);
		if(currentGalaxy!=null)
		{
			Solar currentSolar=Main.LookUpSolarInUniverse(sid1);
	    	currentSolar.splitPlanets(distance);
	    	Solar solar2=new Solar(sid2);
	    	Solar solar3=new Solar(sid3);
	    	solar2.setPlanets(Main.root1);
	    	solar2.setPlanets(Main.root2);
	    	currentGalaxy.insertSolar(solar2);
	    	currentGalaxy.insertSolar(solar3);
	    	System.out.println("C "+sid1+" "+distance+" "+sid2+" "+sid3);
	    	System.out.println("sid2 Planets = ");
	    	Main.print_solar(sid2);
	    	System.out.println();
	    	System.out.println("sid3 Planets = ");
	    	System.out.println("DONE");
	    	System.out.println();
	    	return true;
		}
		return false;
    	
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
    	System.out.println("L "+pid);
	    int p_prev = 0,p_next=0,i;
	    Solar currentSolar = null;
	    Main.gLbPlanet=null;
    	for(i=0;i<Galaxy.getIndex();i++)
    	{
    		for(currentSolar=Galaxies[i].getSolars();currentSolar!=Galaxies[i].getSsentinel();currentSolar=currentSolar.getNext())
    		{
    			currentSolar.setPidPsentinel(pid);
    			Main.inOrderLookUpPlanet(currentSolar.getPlanets(),currentSolar.getPsentinel(),pid);
    			if(Main.gLbPlanet!=null)
    				break;
    		}
    		
    	}
    			if (Main.gLbPlanet!=null)
    			{
    				Planet FoundedPlanet=Main.gLbPlanet;			
    				if(FoundedPlanet.getParent()==null)
    				{
    					if(FoundedPlanet.getLc()==null && FoundedPlanet.getRc()==null)
    					{
    						p_prev=0;
    						p_next=0;
    					}
    					else if(FoundedPlanet.getLc()!=null && FoundedPlanet.getRc()!=null)
    					{
    						p_prev=FoundedPlanet.getLc().getPid();
    						p_next=FoundedPlanet.getRc().getPid();
    					}
    					else if(FoundedPlanet.getLc()==null && FoundedPlanet.getRc()!=null)
    					{
    						p_prev=0;
    						p_next=FoundedPlanet.getRc().getPid();
    					}
    					else if(FoundedPlanet.getLc()!=null && FoundedPlanet.getRc()==null)
    					{
    						p_prev=FoundedPlanet.getLc().getPid();
    						p_next=0;
    					}
    				}
    				else if(FoundedPlanet.getParent().getLc()==FoundedPlanet)
    				{
    					if(FoundedPlanet.getLc()!=null)
    					{
    						p_prev=FoundedPlanet.getLc().getPid();
    						p_next=FoundedPlanet.getParent().getPid();
    					}
    					else
    					{
    						p_prev=0;
    						p_next=FoundedPlanet.getParent().getPid();
    					}
    				}
    				else if(FoundedPlanet.getParent().getRc()==FoundedPlanet)
    				{
    					if(FoundedPlanet.getRc()!=null)
    					{
    						p_prev=FoundedPlanet.getParent().getPid();
    						p_next=FoundedPlanet.getRc().getPid();
    					}
    					else
    					{
    						p_prev=FoundedPlanet.getParent().getPid();
    						p_next=0;
    					}
    				}
    				
    				System.out.println("	"+Galaxies[i].getGid()+" : "+currentSolar.getSid()+" : "+FoundedPlanet.getDistance()+", "+p_prev+" "+p_next);
    				System.out.println("Planet with pid:"+pid +"  Founded"+"	");
   				 	return true;
   				}
    		
    	
		System.out.println("Not_Founded");
        return false;
    	
    }
     
     public static void inOrderLookUpPlanet(Planet root,Planet Psentinel,int gid)
     {
     	if(root==null || root==Psentinel)
     		return;
     	inOrderLookUpPlanet(root.getLc(),Psentinel,gid);
     	if(root.getPid()==gid)
     	{
     		System.out.println("Found");
     		Main.gLbPlanet=root;
     		return;
     	}
     	inOrderLookUpPlanet(root.getRc(),Psentinel,gid);

     	
     }
     
     
 

    /**
    * @brief Find an orphan in the universe
    *
    * @param oid The orphan planet's id
    *
    * @return true on success
    *         false on failure
    */
    public static boolean lookup_orphan(int oid) {
    	System.out.println("K "+oid);
	    int p_prev = 0,p_next=0,i;
	    Main.gLbOrphan=null;
    	for(i=0;i<Galaxy.getIndex();i++)
    	{
    		Main.prevOrphan=Galaxies[i].getOrphans();
    		Main.nextOrphan=Galaxies[i].getOrphans();
    		Planet inorderSuccessor=Main.inorderSuccessor(Galaxies[i].getOrphans(), oid);
    		//Main.inorderLookUpOrphan(Galaxies[i].getOrphans(), oid);
    		if(Main.gLbOrphan!=null)
    		{
    			
    			System.out.println("Founed:"+Main.gLbOrphan.getPid());
    			System.out.print("	"+Galaxies[i].getGid()+"  "+inorderSuccessor.getPid());
    			System.out.println();
    			return true;
    		}
    		
    	}
    	
        return false;
    }
    
  
    public static Planet findRightSuccessor(Planet root)
    {
        while (root.getLc()!=null)
            root=root.getLc();
        return root;
    }
    
    
    public static Planet  inorderSuccessor(Planet root, int key, Planet parent)
    {
        if (root==null)
            return null;
        if (root.getPid() == key)
        {
        	Main.gLbOrphan=root;
            if (root.getRc()!=null)
                return findRightSuccessor(root.getRc());
            else
                return parent;
        }
        Planet left=inorderSuccessor(root.getLc(),key,root);
        if (left!=null)
            return left;
        return inorderSuccessor(root.getRc(),key,parent);
    }
    
    
    public static Planet inorderSuccessor(Planet root, int key)
    {
        return inorderSuccessor(root,key,null);
    }
    
    
    
    
    public static void inorderLookUpOrphan(Planet root,int oid)
    {
    	
    	if(root==null )
     		return;
    	inorderLookUpOrphan(root.getLc(),oid);
     	if(root.getPid()==oid)
     	{
     		Main.gLbOrphan=root;
     		return;
     		
     	}
     	inorderLookUpOrphan(root.getRc(),oid);
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
    	Solar solarCurrent=Main.LookUpSolarInUniverse(sid);
    	System.out.println("H "+sid);
    	System.out.println("Planets = ");
    	Main.inOrder_print_Solar(solarCurrent.getPlanets(), solarCurrent);
    	System.out.println("DONE");
    	System.out.println();
        return true;
    }
    
    public static void inOrder_print_Solar(Planet root,Solar solarHead)
    {
    	if(root == null || root==solarHead.getPsentinel()) 
    		return;
    	inOrder_print_Solar( root.getLc(),solarHead);
    	System.out.println("	"+root.getPid()+" : "+root.getDistance());
    	inOrder_print_Solar( root.getRc(),solarHead);  	
    }

    /**
    * @brief Print orphan planets
    *
    * @param gid The id of galaxy
    *
    * @return true on success
    *         false on failure
    */
    public static boolean print_orphans(int gid) {
    	System.out.println("I "+gid);
    	System.out.println("Orphans = ");
    	int Gpos=Main.LookUpGid(gid);
    	if(Gpos!=-1)
    	{
    		Main.inOrder_print_Orphan(Galaxies[Gpos].getOrphans());
    		System.out.println();
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
    	System.out.println("J "+gid);
    	int Gpos=Main.LookUpGid(gid);
    	if(Gpos>-1 && Gpos<Galaxy.getIndex())
    	{
    		for(Solar currentSolar=Galaxies[Gpos].getSolars();currentSolar!=Galaxies[Gpos].getSsentinel();currentSolar=currentSolar.getNext())
    	    	Main.print_solar(currentSolar.getSid());
    	    Main.print_orphans(Galaxies[Gpos].getGid());
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
    	for(int Gpos=0;Gpos<Galaxy.getIndex();Gpos++)
    	{
    		Main.print_galaxy(Galaxies[Gpos].getGid());
    	}
    	
        return true;/*No need for false*/
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
    
    //########################################################################################################
    
    /**
     * 
     * @param gid
     * @return the position in GalaxiesArray of the gid Galaxy -1 otherwise
     */
    public static int LookUpGid(int gid)
	{
		for(int i=0;i<Galaxy.getIndex();i++)
		{	
			if(Main.Galaxies[i].getGid()==gid)
				return i;
		}
		 return -1;
    	
	}
    
    /**
     * 
     * @param sid
     * @return the head of the SolarList with Sid
     * 			or null if there is no Solar System with that Sid
     */
    public static Solar LookUpSolarInUniverse(int sid)
    {
    	for(int i=0;i<Galaxy.getIndex();i++)
    	{
    		Main.Galaxies[i].getSsentinel().setSid(sid);
    		for(Solar tempSolar=Main.Galaxies[i].getSolars();tempSolar!=Galaxies[i].getSsentinel();tempSolar=tempSolar.getNext())
    		{
    			if(tempSolar.getSid()==sid && tempSolar!=Main.Galaxies[i].getSsentinel())
    			{
    				return tempSolar;
    				
    			}
    		}	
    		
    	}
    	
    	return null;
    }
    
    /**
     * 
     * @param sid
     * @return the Galaxy that contains the Solar system with the given Sid
     */
    public static Galaxy LookUpGalaxyViaSolar(int sid)
    {
    	for(int i=0;i<Galaxy.getIndex();i++)
    	{
    		
    		Main.Galaxies[i].getSsentinel().setSid(sid);
    		for(Solar tempSolar=Main.Galaxies[i].getSolars();tempSolar!=null && tempSolar!=Galaxies[i].getSsentinel();tempSolar=tempSolar.getNext())
    		{
    			if(tempSolar.getSid()==sid && tempSolar!=Main.Galaxies[i].getSsentinel())
    				return Main.Galaxies[i];
    		}	
    		
    	}
    	
    	return null;	
    }
    
    /**
     * 
     * @param gid
     * @return the position in Galaxies Array of the Galaxy with the Given Gid
     */
    public static int positionInGalaxies(int gid)
    {
    	for(int i=0;i<Galaxy.getIndex();i++)
    	{
    		if(Main.Galaxies[i].getGid()==gid)
    			return i;
    	}
    	return -1;
    }
    
    /**
     * 
     * @param root
     * @return initializes the root of a PlanetTree and returns it
     * 		   otherwise returns null
     */
    public static Planet initPlanet(Planet root)
	{
    	if(root==null)
    	{
    		root=new Planet(0,0);
    		return root;
    	}
		return null;
	}
    
    /**
     * 
     * @param sid
     * @return the position in Galaxies Array of the Galaxy that contains the Solar System with sid
     */
    public static int galaxyPoitionSolar(int sid)
    {
    	for(int i=0;i<Galaxy.getIndex();i++)
    	{
    		boolean flag=LookUpSolar(sid,Galaxies[i]);
    		if (flag==true)
    			return i;
    		
    	}
    	return Integer.MAX_VALUE;
    }
    
    /**
     * 
     * @param sid of a Solar
     * @param galaxy A galaxy of the Galaxies Array
     * @return true if Found false otherwise
     */
    public static boolean LookUpSolar(int sid,Galaxy galaxy)
    {
    	galaxy.getSsentinel().setSid(sid);
    	for(Solar current=galaxy.getSolars();current!=null && current!=galaxy.getSsentinel();current=current.getNext())
    	{
    		if(current.getSid()==sid)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * makes an inOrder traversal to planetTree and prints its nodes
     * @param root 
     * @param sentinel
     */
    public static void inOrderPrintPlanet (Planet root,Planet sentinel)
    {
     
      if(root == null || root == sentinel) 
    	  return;
      
      inOrderPrintPlanet( root.getLc() ,sentinel);
      if(root!=null)
    	  System.out.print("Planet:"+root.getPid()+"  :"+root.getDistance());
      		System.out.println();
      	inOrderPrintPlanet( root.getRc(),sentinel); 
    }
    

    
    

////////////////////////////////////////////////////////////////////////////////
    public static void DPRINT(String s) {
        if (DEBUG) { System.out.println(s); }
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader inputFile;
        String line;
        String [] params;

        /* Check command buff arguments */
        if (args.length != 1 /*|| args.length != 2*/) {
            System.err.println("Usage: <executable-name> <input_file>");
            System.err.println("   or  <executable-name> <input_file> <hashtable_size>");
            System.exit(0);
        }

        /*phashtable_size_g = Integer.parseInt(args[0]);
        if( phashtable_size_g < 1){
	        System.out.println("Error: The hashtable_size must be larger than 0");
	        System.exit(0);
        }*/

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

            /* HT_LOWER */
            case '1': {
                ht_lower_g = Integer.parseInt(params[1]);
                //System.out.println("ht_lower_g = " + ht_lower_g);
                break;
            }

            /* HT_UPPER */
            case '2': {
                ht_upper_g = Integer.parseInt(params[1]);
                //System.out.println("ht_upper_g = " + ht_upper_g);
                break;
            }

            /* MAX ID */
            case '3': {
                max_id_g = Integer.parseInt(params[1]);
                //System.out.println("max_id_g = " + max_id_g);
                if ( /*init_flag*/ 5== 0 ) {
                    System.err.println("Wrong testfile format!");
                    System.exit(-1);
                }
                break;
            }

            /* Big Bang
             * B */
            case 'B':
            {
                if ( big_bang() ) {
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
                	System.out.println();
                	System.out.println();
                    //DPRINT(eventID + " " + sid + " " + distance + " succeeded");
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

            /* Trigger an Binary Star Creation
             * C <sid1> <distance> <sid2> <sid3> */
            case 'C':
            {
                int [] sid = new int[3];
                sid[0] = Integer.parseInt(params[1]);
                int distance = Integer.parseInt(params[2]);
                sid[1] = Integer.parseInt(params[3]);
                sid[2] = Integer.parseInt(params[4]);
                assert(sid[0] > 0);
                assert(sid[1] > 0);
                assert(sid[2] > 0);
                //DPRINT(eventID + " " + sid[0] + " " + sid[1] + " " + sid[2]);

                if ( binary_star_creation(sid[0], distance, sid[1], sid[2]) ) {
                    //DPRINT(eventID + " " + sid[0] + " " + distance + " " +
                           //sid[1] + " " + sid[2] + " succeeded");
                } else {
                    System.out.println(eventID + " " + sid[0] + " " +
                                       distance + " " + sid[1] + " " + sid[2] + " failed");
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

                /* Lookup orphan planet
                 * K <oid>  */
                case 'K':
                {
                    int oid = Integer.parseInt(params[1]);
                    assert(oid > 0);

                    DPRINT(eventID + " " + oid);

                    if ( lookup_orphan(oid) ) {
                        DPRINT(eventID + " " + oid + " succeeded");
                    } else {
                        System.out.println(eventID + " " + oid + " failed");
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

		/* Print a orphan planets
		 * I <gid> */
		case 'I':
		{
                    int gid = Integer.parseInt(params[1]);
                    assert(gid > 0);
                    DPRINT(eventID + " " + gid);

                    if ( print_orphans(gid) ) {
                        DPRINT(eventID + " " + gid + " succeeded");
                    } else {
                        System.out.println(eventID + " " + gid + " failed");
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
                        DPRINT(eventID + " " + gid + " succeeded");
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
                    	
                    	/*for(int i=0;i<Galaxy.getIndex();i++)
                    	{
                    		System.out.println();
                    		System.out.println("Galaxy:"+Galaxies[i].getGid());
                    		if(Galaxies[i].getSolars()!=null && Galaxies[i].getSolars().getNext()!=null)
                    		{	
                    			for(Solar SolarCurrent=Galaxies[i].getSolars();SolarCurrent!=Galaxies[i].getSsentinel() ;SolarCurrent=SolarCurrent.getNext())
                        		{
                        			System.out.println("	Solar:"+SolarCurrent.getSid());
                        			if(SolarCurrent.getPlanets()!=null)
                        			{
                        				Main.inOrderPrintPlanet(SolarCurrent.getPlanets(), SolarCurrent.getPsentinel());
                            			System.out.println();
                        			}
                        		}
                    			System.out.println("	Orphans:");

                        		Main.inOrder_print_Orphan(Galaxies[i].getOrphans());
                    		}
                    		
                    	}*/
                    	
                    	
			//DPRINT("U succeeded\n");
			//System.out.println();
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














