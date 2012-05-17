/**
 * 
 */
package org.jinouts.dependency.resolver.util;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;

/**
 * @author asraf asraf344@gmail.com
 * 
 * http://stackoverflow.com/questions/3734825/find-out-which-classes-of-a-given-api-are-used
 */
public class ClassCollector extends Remapper
{

	private final Set<Class<?>> classNames;
	private final String prefix;

	public ClassCollector(final Set<Class<?>> classNames, final String prefix)
	{
		this.classNames = classNames;
		this.prefix = prefix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String mapDesc ( final String desc )
	{
		if ( desc.startsWith ( "L" ) )
		{
			this.addType ( desc.substring ( 1, desc.length ( ) - 1 ) );
		}
		return super.mapDesc ( desc );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] mapTypes ( final String[] types )
	{
		for ( final String type : types )
		{
			this.addType ( type );
		}
		return super.mapTypes ( types );
	}

	private void addType ( final String type )
	{
		final String className = type.replace ( '/', '.' );
		
		if ( this.prefix != null && className.startsWith ( this.prefix ) )
		{
			if (  className.startsWith ( this.prefix ) )
			{
				try
				{
					this.classNames.add ( Class.forName ( className ) );
				}
				catch ( final ClassNotFoundException e )
				{
					//throw new IllegalStateException ( e );
					e.toString ( );
				}
			}
			
		}
		else
		{
			try
			{
				if ( !className.contains ( "jxbrowser" ))
				{
					//System.out.println ("adding for " +className);
					this.classNames.add ( Class.forName ( className ) );
				}	
				
			}
			
			catch ( Exception e )
			{
				//throw new IllegalStateException ( e );
				e.toString ( );
			}
		}
	}

	@Override
	public String mapType ( final String type )
	{
		this.addType ( type );
		return type;
	}

	/**
	 * 
	 * @param name  class  name
	 * @param prefix common prefix for all classes
								// that will be retrieved
	 * @return
	 * @throws IOException
	 */
	public static Set<Class<?>> getClassesUsedBy ( final String name,  final String prefix 	) throws IOException
	{
		final ClassReader reader = new ClassReader ( name );
		final Set<Class<?>> classes = new TreeSet<Class<?>> ( new Comparator<Class<?>> ( )
		{

			@Override
			public int compare ( final Class<?> o1, final Class<?> o2 )
			{
				return o1.getName ( ).compareTo ( o2.getName ( ) );
			}
		} );
		
		final Remapper remapper = new ClassCollector ( classes, prefix );
		final ClassVisitor inner = new EmptyVisitor ( );
		final RemappingClassAdapter visitor = new RemappingClassAdapter ( inner, remapper );
		try
		{
			reader.accept ( visitor, ClassReader.EXPAND_FRAMES );
		}
		catch (Exception ex)
		{
			ex.toString ( );
		}
		
		
		return classes;
	}

}