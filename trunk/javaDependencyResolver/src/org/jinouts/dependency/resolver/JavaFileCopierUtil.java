/**
 * 
 */
package org.jinouts.dependency.resolver;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;


/**
 * @author asraf
 *
 */
public class JavaFileCopierUtil
{
	
	public static final String SOURCE_DIR = "D:\\andPro\\SoapUISources\\soapui-4.0.1-src\\soapui-4.0.1\\src\\java";
	public static final String DEST_DIR = "D:\\andPro\\SOAPUIRND\\src-soap";
	
	//public static final String STARTING_CLASS_FILE = SOURCE_DIR + "\\com\\eviware\\soapui\\impl\\wsdl\\WsdlProject.java";
	public static final String STARTING_CLASS_FILE = SOURCE_DIR + "\\com\\eviware\\soapui\\impl\\WsdlInterfaceFactory.java";
	
	public static final int DEPTH_LEVEL = 1;
	//\\com\\eviware\\soapui\\impl\\WsdlInterfaceFactory.java
	static String javaClass =  "com.eviware.soapui.support.types.StringList"; //
	/**
	 * @param args
	 */
	public static void main ( String[] args )
	{
				
		try
		{
			//
			
			copyFile( javaClass );
		}
		catch( Exception ex)
		{
			ex.printStackTrace ( );
		}
		
	}
	
	static void copyFile ( String javaClass ) throws Exception
	{
				
		JavaFile sJavaFile = JavaFile.getJavaFile ( SOURCE_DIR, javaClass );
		String destDirPath = DEST_DIR + File.separator + sJavaFile.getPackageDir ( );
		System.out.println (javaClass + " Successfully Copied ");
		// now copy file
		FileUtils.copyFileToDirectory ( sJavaFile, new File ( destDirPath ) );
		
		
	}
	
	

}
