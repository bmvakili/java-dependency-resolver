/**
 * 
 */
package org.jinouts.dependency.resolver;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;


/**
 * @author asraf
 *
 */
@Deprecated
public class ClassDependencyResolver
{
	
	public static final String SOURCE_DIR = "D:\\andPro\\SoapUISources\\soapui-4.0.1-src\\soapui-4.0.1\\src\\java";
	public static final String DEST_DIR = "D:\\andPro\\SOAPUIRND\\src-soap";
	
	//public static final String STARTING_CLASS_FILE = SOURCE_DIR + "\\com\\eviware\\soapui\\impl\\wsdl\\WsdlProject.java";
	public static final String STARTING_CLASS_FILE = SOURCE_DIR + "\\com\\eviware\\soapui\\impl\\WsdlInterfaceFactory.java";
	
	public static final int DEPTH_LEVEL = 1;
	//\\com\\eviware\\soapui\\impl\\WsdlInterfaceFactory.java
	/**
	 * @param args
	 */
	public static void main ( String[] args )
	{
		// ignore the java package say-- java.lang etc.
		// ignore the inner class
		
		// Read a file to start with
		File startingFile =  new File ( STARTING_CLASS_FILE );
		
		if ( !startingFile.exists ( ) )
		{
			System.out.println ("FIle not found now exiting !!!!!");
			return;
		}
		
		try
		{
			resolveDependency( startingFile );
		}
		catch( Exception ex)
		{
			ex.printStackTrace ( );
		}
		
	}
	@Deprecated
	static void resolveDependency ( File startingFile ) throws Exception
	{
		int counter = 0;
		
		Set<JavaFile> workingJavaFileSet = new HashSet<JavaFile> ( );
		Set<JavaFile> processedJavaFileSet = new HashSet<JavaFile> ( );
		
		JavaFile sJavaFile = JavaFile.getJavaFile ( SOURCE_DIR, "com.eviware.soapui.impl.WsdlInterfaceFactory" );
		String destDirPath = DEST_DIR + File.separator + sJavaFile.getPackageDir ( );
		// now copy file
		FileUtils.copyFileToDirectory ( sJavaFile, new File ( destDirPath ) );
		
		//
		List<JavaFile> importJavaFileList = DependencyResolverUtil.getImports ( startingFile );
		workingJavaFileSet.addAll ( importJavaFileList );
		
		while ( workingJavaFileSet.size ( ) > 0 && counter < DEPTH_LEVEL )
		{
			Set<JavaFile> tempJavaFileSet = new HashSet<JavaFile> ( );
			tempJavaFileSet.addAll ( workingJavaFileSet );
			
			for ( JavaFile javaFile: tempJavaFileSet )
			{
				if ( processedJavaFileSet.contains ( javaFile ) )
				{
					workingJavaFileSet.remove ( javaFile );									
				}
				else
				{
					destDirPath = DEST_DIR + File.separator + javaFile.getPackageDir ( );
					// now copy file
					FileUtils.copyFileToDirectory ( javaFile, new File ( destDirPath ) );
					
					processedJavaFileSet.add ( javaFile );
					workingJavaFileSet.remove ( javaFile );
					
					List<JavaFile> newImportJavaFileList = DependencyResolverUtil.getImports ( javaFile.getAbsoluteFile ( ) );
					System.out.println (javaFile.getName ( ) + " has imports: " + newImportJavaFileList.size ( ) );
					
					if ( newImportJavaFileList.size ( ) < 50 )
					{
						workingJavaFileSet.addAll ( newImportJavaFileList );
						System.out.println ("Total Set Size now " + workingJavaFileSet.size ( ) );
					}
				}				
				
				
			}
			System.out.println ("While counter " + counter );
			counter++;
		}
		
	}
	
	

}
