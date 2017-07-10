package org.apache.nutch.enconversion.unl.ta;
import org.apache.nutch.unl.UNL;
import java.lang.*;
import java.io.*;
import java.io.FileOutputStream;
import java.util.*;
public class integrate implements Serializable
{
	public static FinalLLImpl[] ll_new = new FinalLLImpl[25000];
	public static FinalLLImpl[] ll = new FinalLLImpl[25000];
	public static int count=0;
	public static FinalLLImpl[] integrateObj()
	{
		File f=new File("./crawl-unl");
		File filelist[]=f.listFiles();
		System.out.println("size is "+filelist.length);
		for(int j=1;j<filelist.length;j++)
		{
			try
			{
				String name="unlgraph"+j+".txt";
				FileInputStream fis=new FileInputStream("./crawl-unl/"+name);
			 	ObjectInputStream ois1=new ObjectInputStream(fis);					
		 		ll_new=(FinalLLImpl[])ois1.readObject(); 
				System.out.println("Reading unlgraph1 completed........."+name);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			/*try
			{
				FileInputStream fis1=new FileInputStream("./crawl-unl/unlgraph2.txt");
				 ObjectInputStream ois1=new ObjectInputStream(fis1);					
			 	ll=(FinalLLImpl[])ois1.readObject(); 
				System.out.println("Reading unlgraph2 completed.........");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}*/
			for(int i=0;i<ll_new.length;i++)
			{
				if(ll_new[i]!=null)
				{
					System.out.println("Object added into "+count+" position");
					ll[count]=ll_new[i];
					count++;
				}
			
			}
		}
		System.out.println("Writing Started......"+ll.length);
		/*try
		{
			 FileOutputStream fostream=new FileOutputStream("./crawl-unl/unlgraph.txt");
         		ObjectOutput oostream=new ObjectOutputStream(fostream);
			//ll=getmultilist();
         		oostream.writeObject(ll_new);
			System.out.println("Writing into binary file completed.....");
        		// oostream.flush(); 
         		oostream.close();
			fostream.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}*/
		return ll;
	}
}
