/**
 * 
 */
package org.jinouts.dependency.resolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.jinouts.dependency.resolver.util.ClassCollector;
import org.jinouts.dependency.resolver.util.ResolverProperties;


/**
 * @author asraf
 *
 */
/**
 * @author asraf
 * asraf344@gmail.com
 */
/**
 * @author asraf
 * asraf344@gmail.com
 */
public class DependencyResolverUtil
{
	
	
	@Deprecated
	public static List<JavaFile>  getImports ( File javaSourceFile ) throws Exception
	{
		// read the imports
		LineIterator lineIterator = FileUtils.lineIterator ( javaSourceFile );
		List<JavaFile> javaFileList = new ArrayList<JavaFile> ( );
		
		System.out.println ("Getting imports for "+javaSourceFile.getAbsolutePath ( ) );
		String line = lineIterator.nextLine ( );
		while ( (line != null) &&  ( !line.contains ( "class" ) ) )
		{
			if ( line.contains ( "import " ) )
			{
				String fullClassName = getFullClassNameFromImport ( line  );
				JavaFile javaFile = JavaFile.getJavaFile ( ClassDependencyResolver.SOURCE_DIR, fullClassName );
				
				if ( javaFile.exists ( ) )
				{
					javaFileList.add ( javaFile );
				}
				
			}
			
			if (  lineIterator.hasNext ( ))
			{
				line = lineIterator.nextLine ( );
			}
			else
			{
				line = null;
			}
						
		}
		
		return javaFileList;
	}
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	public static Set<JavaFile>  getUsedJavaFileList ( String className ) 
	{
		Set<JavaFile> javaFileSet = new HashSet<JavaFile> ( );
		
		// get the class name list
		List<String> classNameList = getUsedClasses ( className  );
		
		// now iterate the class name list
		for ( String fullClassName : classNameList )
		{
			try
			{
				JavaFile javaFile = JavaFile.getJavaFile ( ResolverProperties.SOURCE_DIR, fullClassName );
				javaFileSet.add ( javaFile );
			}
			catch ( Exception ex )
			{
				ex.toString ( );
			}
			
		}
		
		// return the list
		return javaFileSet;	
	}
	
	public static List<String> getUsedClasses ( String name  )
	{
		List<String> classNameList = new ArrayList<String> ( );
		
		try
		{
			System.out.println ( "Used classes for  "+name );
			final Collection<Class<?>> classes = ClassCollector.getClassesUsedBy ( name, null );
			
			for ( final Class<?> cls : classes )
			{
				String className = cls.getName ( ) ;
				//System.out.println ( " - " +  cls.getName ( ) );
				
				// if its not java own class
				if ( !className.startsWith ( "java" ))
				{
					// if its not an array or inner class
					if ( !className.startsWith ( "[L" ) && !className.contains ( "$" ) )
					{
						//JavaFile javaFile = JavaFile.getJavaFile ( ASMClassDependencyResolver.DEST_SRC_DIR, className );
						classNameList.add ( className );
						
					}
					// if its some array
					else if( className.startsWith ( "[L" ) )
					{
						classNameList.add ( className.substring ( 2, className.length ( )-1 ) );
					}
				}
				
			}
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return classNameList;
	}
	
	public static String getImportableClassName ( String className)
	{
		if ( !className.startsWith ( "java" ))
		{
			// if its not an array or inner class
			if ( !className.startsWith ( "[L" ) && !className.contains ( "$" ) )
			{
				//JavaFile javaFile = JavaFile.getJavaFile ( ASMClassDependencyResolver.DEST_SRC_DIR, className );
				return className;
				
			}
			// if its some array
			else if( className.startsWith ( "[L" ) )
			{
				return   className.substring ( 2, className.length ( )-1 );
			}
		}
		
		return null;
		
	}
	
	public static String getFullClassNameFromImport ( String importString )
	{
		return importString.substring ( 7, importString.length ( )-1).trim ( );
	}

}
