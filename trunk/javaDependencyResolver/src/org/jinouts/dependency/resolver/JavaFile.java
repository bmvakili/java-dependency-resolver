/**
 * 
 */
package org.jinouts.dependency.resolver;

import java.io.File;

/**
 * @author asraf
 *
 */
public class JavaFile extends File
{
	private String fullClassName;
	private String dirName;
	private String fullFilePath;
	private String packageDir;
	
	private  JavaFile( String fullFilePath )
	{
		
		super ( fullFilePath );
		this.fullFilePath = fullFilePath;
		// TODO Auto-generated constructor stub
	}
	
	public static JavaFile getJavaFile ( String SourceDir, String fullClassName )
	{
		//System.out.println ("Getting java file for class" + fullClassName );
		//TODO: correct the package dir
		String packageDir = fullClassName.replace ( ".", separator );
		String fullFilePath = SourceDir + separator + packageDir + ".java";
		packageDir = packageDir.substring ( 0, packageDir.lastIndexOf ( separator )  );
		
		
		String dir = SourceDir + separator + packageDir;
		
		//System.out.println ( fullFilePath );
		
		JavaFile javaFile = new JavaFile ( fullFilePath );
		javaFile.setFullClassName ( fullClassName );
		javaFile.setDirName ( dir );
		javaFile.setPackageDir ( packageDir );
		
		return javaFile;
	}
	
	
	
	public String getFullClassName ( )
	{
		return fullClassName;
	}
	public void setFullClassName ( String fullClassName )
	{
		this.fullClassName = fullClassName;
	}
	public String getDirName ( )
	{
		return dirName;
	}
	public void setDirName ( String dirName )
	{
		this.dirName = dirName;
	}
	public String getFullFilePath ( )
	{
		return fullFilePath;
	}
	public void setFullFilePath ( String fullFilePath )
	{
		this.fullFilePath = fullFilePath;
	}

	public String getPackageDir ( )
	{
		return packageDir;
	}

	public void setPackageDir ( String packageDir )
	{
		this.packageDir = packageDir;
	}
	

	@Override
	public boolean equals ( Object obj )
	{
		JavaFile file = (JavaFile) obj;
		// TODO Auto-generated method stub
		return (this.fullClassName.equals ( file.getFullClassName ( ) ) );
	}
	
}
