/**
 * 
 */
package org.jinouts.dependency.resolver.util;

import java.io.File;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * @author asraf asraf344@gmail.com
 * 
 */
public class JarUtils
{
	// http://stackoverflow.com/questions/3497664/how-to-find-the-jar-file-containing-a-class-definition
	public static boolean isClassInJar ( File jarFile, String classFileFullPath )
	{
		try
		{
			System.out.println ( "Searching: " + jarFile.getPath ( ) );
			JarFile jar = new JarFile ( jarFile );
			ZipEntry e = jar.getEntry ( classFileFullPath );
			if ( e == null )
			{
				e = jar.getJarEntry ( classFileFullPath );
			}

			if ( e != null )
			{
				// foundIn.add(f.getPath());
				System.out.println ( "Class Found in : " + jarFile.getName ( ) );
				return true;
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace ( );
		}
		
		return false;

	}
}
