/*
Connor Kalina 1550 Project 3 virtual machine page replacement algorithm simulation
11.07.2019
*/
import java.util.*;
import java.io.*;

public class vmsim {
	public static int memAccesses,pageFaults,writes;//counters to print at the end of simulation
    public static BufferedReader buff = null;
    public static ArrayList<String> type;
    public static ArrayList<String> address;
    public static ArrayList<String> cycles;
    public static LinkedList<page> frames;
    public static ArrayList<page> allPages;
    
	public static void main(String args[]) throws IOException,FileNotFoundException//accepts String array of arguments
	{
		int nFrames = 0;//initialize arguments
		String alg = "no alg";
		int refresh = 0;
		String traceFile = "null";
		
		for(int i = 0; i < args.length; i++) //parse and save arguments
		{
			if(args[i].equals("-n"))
			{//following argument will be nFrames
				nFrames = Integer.parseInt(args[i+1]);
			}
			else if(args[i].equals("-a"))
			{//following argument will be algorithm type
				alg = args[i+1];
			}
			else if(args[i].equals("-r"))
			{//following argument will be refresh rate
				refresh = Integer.parseInt(args[i+1]);
			}
			else if(i == args.length - 1)
			{//last argument, trace file
				traceFile = args[i];
			}
		}
		
		String [] readLine = null;
        type = new ArrayList<>();
        address = new ArrayList<>();
        cycles = new ArrayList<>();

        buff = new BufferedReader(new FileReader(traceFile));
        while(buff.ready())
        {
            readLine = buff.readLine().split(" ");
            type.add(readLine[0]);
            address.add(readLine[1]);
            cycles.add(readLine[2]);
            memAccesses++;
        }
        allPages = new ArrayList<page>();
        frames = new LinkedList<page>();
        for(int i = 0; i < nFrames; i++)
        {//populate with null frames
        	page X = new page();
        	X.pageNum =  "-9999";
        	frames.add(X);
        }

		if(alg.equals("fifo"))
		{
			fifo(nFrames);
		}
		else if(alg.equals("opt")) 
		{
			opt(nFrames);
		}
		else if(alg.equals("aging")) 
		{
			lru(nFrames,refresh);
		}
		//final result print outs
		 System.out.println("Algorithm: " + alg.toUpperCase());
		 System.out.println("Number of frames: " + nFrames);
	     System.out.println("Total memory accesses: " + memAccesses);
	     System.out.println("Total page faults: " + pageFaults);
	     System.out.println("Total writes to disk: " + writes);
	}

	public static void fifo(int nFrames) 
	{
		for(int i = 0; i < memAccesses; i++) 
		{
			boolean memcheck = false;
			//create page based on info, 
			String pageNumber = address.get(i).substring(2,7);
			//check if pageNumber exists...
			page p;
			boolean exists = false;
			int idk = -1;
			for(int z = 0; z < allPages.size(); z++)
			{
				if(allPages.get(z).pageNum.equals(pageNumber))
				{
					//page exists (on disk or in memory) already
					exists = true;
					idk = z;
				}
			}
			if(exists) 
			{
				p = allPages.get(idk);//retrieve page, dont create new one
			}
			else
			{
				p = new page(pageNumber);//page didn't exist before, create new
				allPages.add(p);
			}
			if(type.get(i).charAt(0) == 's') 
			{
				p.dirty = true;
			}
			//check if p address is already in page frames
			for(int x = 0; x < nFrames; x++)//p is not in page frames
			{
				if (frames.get(x).pageNum.equals((p.pageNum)))
				{
					//page already in memory
					memcheck = true;
				}
			}
			if(memcheck == false)//not in memory 
			{
				pageFaults++;//increment pageFaults
				if(frames.peek().dirty) 
				{
					writes++;
					frames.peek().dirty = false;
				}
				frames.pop();//queue operations(FIFO)
				frames.add(p);
			}
		}
	}
	public static void opt(int nFrames) 
	{
		for(int i = 0; i < memAccesses; i++) 
		{
			boolean memcheck = false;
			//create page based on info 
			String pageNumber = address.get(i).substring(2,7);
			//check if page with that page number exists...
			page p;
			boolean exists = false;
			int idk = -1;
			for(int z = 0; z < allPages.size(); z++)
			{
				if(allPages.get(z).pageNum.equals(pageNumber))
				{
					//it exists already
					exists = true;
					idk = z;
				}
			}
			if(exists) 
			{
				p = allPages.get(idk);
			}
			else
			{
				p = new page(pageNumber);
				allPages.add(p);
			}
			if(type.get(i).charAt(0) == 's') 
			{
				p.dirty = true;
			}
			//check if p page number is loaded in page frames
			for(int x = 0; x < nFrames; x++)//p is not in page frames
			{
				if (frames.get(x).pageNum.equals((p.pageNum)))
				{
					memcheck = true;
				}
			}	
			if(memcheck == false)//not in memory 
			{
				pageFaults++;
			//  opt eviction policy here
			//  load next instruction's page and make a copy of page frames to manipulate
			//	if frame has a page with -9999, skip this and just evict that..
				boolean emptySlot = false;
				for(int x = 0; x < nFrames; x++)
				{
					if (frames.get(x).pageNum.equals("-9999"))
					{//there was an empty frame
						frames.remove(x);
						frames.add(p);
						emptySlot = true;
						break;
					}
				}
				@SuppressWarnings("unchecked")
				LinkedList<page> loadedFrames = (LinkedList<page>) frames.clone();
				if(!emptySlot) 	
				{
					int b = i+1;
					while(loadedFrames.size() > 1 && b <= address.size()-1)
					{
						String nextPageNum = address.get(b).substring(2,7);
						for(int q = 0; q < loadedFrames.size(); q++)
						{
							if(loadedFrames.get(q).pageNum.equals(nextPageNum))
							{//found page match in instruction b
								loadedFrames.remove(q);//remove this from contention of being removed
							}
						}
						b++; 
					}
				//last remaining element of loadedFrames is the page that needs to be evicted
				//match it to element of frames and remove
				}
				int index = frames.indexOf(loadedFrames.get(0));
				if(frames.get(index).dirty == true)
				{
					writes++;
					frames.get(index).dirty = false;
				}
				@SuppressWarnings("unused")
				boolean test = frames.remove(loadedFrames.get(0));
				frames.add(p);
			}
		}
	}
	public static void lru(int nFrames, int refresh) 
	{
		for(int i = 0; i < memAccesses; i++) 
		{
			//could update every page in memory with new age after every instruction
			int addedTime = Integer.parseInt(cycles.get(i));
			for(int h = 0; h < frames.size(); h++)
			{//add age to every page
				frames.get(h).addAge(addedTime);
			}
			//below determines if in memory already
			boolean memcheck = false;
			//create page based on info, 
			String pageNumber = address.get(i).substring(2,7);
			//check if page with that page number exists...
			page p;
			boolean exists = false;
			int idk = -1;
			for(int z = 0; z < allPages.size(); z++)
			{
				if(allPages.get(z).pageNum.equals(pageNumber))
				{
					//it exists already
					exists = true;
					idk = z;
				}
			}
			if(exists) 
			{
				p = allPages.get(idk);
				p.resetAge();
			}
			else
			{
				p = new page(pageNumber);
				allPages.add(p);
			}
			if(type.get(i).charAt(0) == 's') 
			{
				p.dirty = true;
			}
			//check if p page number is loaded in page frames
			for(int x = 0; x < nFrames; x++)//p is not in page frames
			{
				if (frames.get(x).pageNum.equals((p.pageNum)))
				{
					//page already in memory	
					memcheck = true;
					frames.get(x).resetAge();
				}
			}
			if(memcheck == false)//not in memory 
			{
				pageFaults++;
			//  lru aging eviction policy here
				boolean emptySlot = false;
				for(int x = 0; x < nFrames; x++)
				{
					if (frames.get(x).pageNum.equals("-9999"))
					{
						//page already in memory	
						frames.remove(x);
						frames.add(p);
						emptySlot = true;
						break;
					}
				}
				if(!emptySlot)//replacement algorithm
				{//replace most aged process in page frames
					page agedFrame = new page();
					for(int y = 0; y < frames.size(); y++) 
					{//find most aged process
						int ageofaged = agedFrame.age/refresh;
						int ageofnew = frames.get(y).age/refresh;
						if(agedFrame.age == 0 || ageofnew > ageofaged)
						{
							agedFrame = frames.get(y);
						}
						else if(ageofnew == ageofaged)
						{
							//tie-breaker
							if(agedFrame.dirty == true && frames.get(y).dirty == true)
							{
								//double tie-breaker
								if(Integer.decode( "0x"+ agedFrame.pageNum) > Integer.decode("0x" + frames.get(y).pageNum))
								{//replace lower page #
									agedFrame = frames.get(y);
								}
								//else
									//agedFrame = frames.get(y);
							}
							else if(agedFrame.dirty == false && frames.get(y).dirty == false)
							{
								//double tie-breaker
								if(Integer.decode( "0x"+ agedFrame.pageNum) > Integer.decode("0x" + frames.get(y).pageNum))
								{
									
									agedFrame = frames.get(y);
								}
								//else
									//agedFrame = frames.get(y);
							}
							else if(agedFrame.dirty == true && frames.get(y).dirty == false)
							{//replace not dirty page
								agedFrame = frames.get(y);
							}
							
						}
					}
					int index = frames.indexOf(agedFrame);
					if(frames.get(index).dirty == true)
					{
						writes++;
						frames.get(index).dirty = false;
					}
					frames.remove(index);
					frames.add(p);
				}
			}
		}
	}
}
