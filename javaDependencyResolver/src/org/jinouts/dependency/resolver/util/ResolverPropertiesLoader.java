/**
 * 
 */
package org.jinouts.dependency.resolver.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author asraf
 * asraf344@gmail.com
 */
public class ResolverPropertiesLoader
{
	public static void loadProperties () 
	{
		Properties properties = new Properties ( );
		try
		{
			properties.load( new FileInputStream ( "conf/resolver.properties" ));
			
			// now get the properties
			String sourceDir = (String)properties.get ( "source.dir" );
			String destSourceDir = (String)properties.get ( "dest.src.dir" );
			String destLibDir = (String)properties.get ( "dest.lib.dir" );
			String maxIteration = (String)properties.get ( "max.iteration" );
			
			
			System.out.println ("Properties: "+sourceDir +" "+destSourceDir+" "+destLibDir+" "+maxIteration);
			
			ResolverProperties.SOURCE_DIR = sourceDir;
			ResolverProperties.DEST_SRC_DIR = destSourceDir;
			ResolverProperties.DEST_LIB_DIR = destLibDir;
			ResolverProperties.DEPTH_LEVEL = Integer.parseInt ( maxIteration );
			
		}
		catch ( FileNotFoundException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
