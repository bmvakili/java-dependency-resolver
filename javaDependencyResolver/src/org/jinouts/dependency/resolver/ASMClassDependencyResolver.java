/**
 * 
 */
package org.jinouts.dependency.resolver;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.jinouts.dependency.resolver.util.JarUtils;
import org.jinouts.dependency.resolver.util.ResolverProperties;
import org.jinouts.dependency.resolver.util.ResolverPropertiesLoader;


/**
 * @author asraf
 * Resolve dependency for a particular class. It searches for its dependency classes in the
 * particular project, and copy them to this project and again searches dependency for these
 * classes and so on. This process will be continued until it find all the dependency classes.
 */
public class ASMClassDependencyResolver
{	
	static final String  SOURCE_DIR_NOT_EXIST = "The source directory you mentioned doesn't exist, please correct it and run again !!";
	static final String  DIR_NOT_CREATED = "Couldn't make destination source/lib dir, please review the properties File again !!";
	
	/**
	 * @param args
	 * 
	 */
	public static void main ( String[] args )
	{
		try
		{	
			// Load the properties
			ResolverPropertiesLoader.loadProperties ( );
			// Initialize the source/dest dir and check the validation
			initialize ( );
			
			// now resolve the depency for the class--
			// say here we wanna find dependencies for the class- "JarUtils.class"
			resolveDependency( JarUtils.class.getName ( )  );
			
		}
		catch( Exception ex)
		{
			ex.printStackTrace ( );
		}
		
		System.exit ( 0 );
	}
	
	static void initialize ()
	{
		// intantiate the dir from the property file
		File destSrcDir = new File ( ResolverProperties.DEST_SRC_DIR );
		File destLibDir = new File ( ResolverProperties.DEST_LIB_DIR );
		File sourceDir = new File ( ResolverProperties.SOURCE_DIR );
		
		if ( !sourceDir.exists ( ) )
		{
			// if source dir does not exist show message
			showInformationMessage ( SOURCE_DIR_NOT_EXIST );
		}
		
		boolean destLibCreated = true;
		boolean destSrcCreated = true;
		if ( !destSrcDir.exists ( ) )
		{
			destSrcCreated = destSrcDir.mkdir ( );
		}
		
		if ( !destLibDir.exists ( ) )
		{
			destLibCreated = destLibDir.mkdir ( );
		}
		
		// if dest direcotories not exist show message
		if ( !(destSrcCreated && destSrcCreated) )
		{
			showInformationMessage ( DIR_NOT_CREATED );
		}
	}
	
	// show messages
	static void showInformationMessage ( String message )
	{
		JOptionPane.showMessageDialog ( null, message, "Invalid Directory", JOptionPane.OK_OPTION );
		System.exit ( 0 );
	}
	
	static void resolveDependency ( String startingClassName ) throws Exception
	{
		int counter = 0;
		
		// working file sets are the classes whose we need to find dependencies
		Set<JavaFile> workingJavaFileSet = new HashSet<JavaFile> ( );
		// processed classes are the files -- those are copied and their dependencies 
		// have been found -- they are kept in the cache just not to repeat the process again
		Set<JavaFile> processedJavaFileSet = new HashSet<JavaFile> ( );
		
		// unprocessed file set -- those are not found in the source,
		// may be found in the jar files
		Set<JavaFile> unProcessedJavaFileSet = new HashSet<JavaFile> ( );
		
		// get the java file for the source
		JavaFile sJavaFile = JavaFile.getJavaFile ( ResolverProperties.SOURCE_DIR, startingClassName );
		String destDirPath = ResolverProperties.DEST_SRC_DIR + File.separator + sJavaFile.getPackageDir ( );
		
		// now copy file
		FileUtils.copyFileToDirectory ( sJavaFile, new File ( destDirPath ) );
		
		// get the depencies of this class
		workingJavaFileSet = DependencyResolverUtil.getUsedJavaFileList ( startingClassName );
		
		// iterate it
		while ( workingJavaFileSet.size ( ) > 0 && counter < ResolverProperties.DEPTH_LEVEL )
		{
			Set<JavaFile> tempJavaFileSet = new HashSet<JavaFile> ( );
			tempJavaFileSet.addAll ( workingJavaFileSet );
			
			for ( JavaFile javaFile: tempJavaFileSet )
			{
				if ( !javaFile.exists ( ) )
				{
					// it doesn't exist keep it in the unprocessed cache to process it later.
					unProcessedJavaFileSet.add ( javaFile );
					workingJavaFileSet.remove ( javaFile );
				}
				else if ( processedJavaFileSet.contains ( javaFile ) )
				{
					// its already processed so just remove it
					workingJavaFileSet.remove ( javaFile );									
				}
				else
				{
					// needs to process it
					destDirPath = ResolverProperties.DEST_SRC_DIR + File.separator + javaFile.getPackageDir ( );
					// now copy file
					FileUtils.copyFileToDirectory ( javaFile, new File ( destDirPath ) );
					
					processedJavaFileSet.add ( javaFile );
					workingJavaFileSet.remove ( javaFile );
					
					// get the dependencies of this file
					Set<JavaFile> newImportJavaFileSet = DependencyResolverUtil.getUsedJavaFileList ( javaFile.getFullClassName ( ) );
					newImportJavaFileSet.removeAll ( processedJavaFileSet );
					newImportJavaFileSet.removeAll ( unProcessedJavaFileSet );
					
					
					if ( newImportJavaFileSet.size ( ) < 50 )
					{
						workingJavaFileSet.addAll ( newImportJavaFileSet );
						System.out.println ("Total Set Size now " + workingJavaFileSet.size ( ) );
					}
				}				
				
				
			}
			
			System.out.println ("While counter " + counter );
			System.out.println ("Total Set Size now " + workingJavaFileSet.size ( ) );
			System.out.println ("Processed Set Size: " + processedJavaFileSet.size ( ) );
			System.out.println ("Unprocessed Set Size now " + unProcessedJavaFileSet.size ( ) );
			counter++;
			
			// free resources
			tempJavaFileSet.clear ( );
			tempJavaFileSet = null;
		}
		
		// now processing is over for the sources, now try to fetch the dependency jars
		processForJarClasses ( unProcessedJavaFileSet );
		
	}
	
	/**
	 * Get the dependency jars
	 * @param unProcessedJavaFileSet
	 */
	static void processForJarClasses ( Set<JavaFile> unProcessedJavaFileSet )
	{
		//http://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
		File destLibDir = new File ( ResolverProperties.DEST_LIB_DIR );
		HashSet<String> jarHashSet = new HashSet<String>();
		for ( JavaFile javaFile: unProcessedJavaFileSet )
		{
			String className = javaFile.getFullClassName ( );
			String validClassName = DependencyResolverUtil.getImportableClassName ( className );
			System.out.println ("->Getting jar for "+ className  );
			try
			{
				if ( validClassName != null )
				{
					Class<?> cl = Class.forName ( validClassName );
					
					String path = cl.getProtectionDomain().getCodeSource().getLocation().getPath();
					String decodedPath = URLDecoder.decode(path, "UTF-8");
					//System.out.println ("--> "+decodedPath+" path "+path );
					//System.out.println ("jar:  "+decodedPath );
					File jarFile = new File ( decodedPath );
					
					String jarFileName = jarFile.getName ( );
					
					if ( !jarHashSet.contains ( jarFileName ) )
					{
						jarHashSet.add ( jarFile.getName ( ) );
						System.out.println ("Now Copying jar: " + jarFileName );
						FileUtils.copyFileToDirectory ( jarFile, destLibDir  );
					}
				}

				
			}
			catch( Exception ex )
			{
				ex.printStackTrace ( );
			}
			
		}
	}
	
	

}
