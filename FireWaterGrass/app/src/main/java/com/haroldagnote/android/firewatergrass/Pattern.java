package com.haroldagnote.android.firewatergrass;

import java.io.Serializable;

/**
 * "Class Description"
 *
 * @author Harold Agnote
 */
public class Pattern implements Serializable
{
	private String elementPattern;
	
	public Pattern( String pattern )
	{
		elementPattern = pattern;
	}
	
	public String getPattern()
	{
		return elementPattern;
	}
	
	@Override
	public boolean equals( Object o )
	{
		if ( this == o )
		{
			return true;
		}
		if ( !( o instanceof Pattern ) )
		{
			return false;
		}
		
		Pattern pattern = ( Pattern ) o;
		
		return this.elementPattern.equals( pattern.elementPattern );
		
	}
	
	@Override
	public int hashCode()
	{
		return elementPattern.hashCode();
	}
	
	@Override
	public String toString()
	{
		return elementPattern;
	}
}