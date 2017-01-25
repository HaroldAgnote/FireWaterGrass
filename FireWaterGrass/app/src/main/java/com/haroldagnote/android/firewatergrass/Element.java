package com.haroldagnote.android.firewatergrass;

import java.io.Serializable;

/**
 * "File Description"
 *
 * @author Harold Agnote
 */
public class Element implements Comparable < Element >, Serializable
{
	private static final String[] elementStrings = { "Fire", "Water", "Grass", "Null" };
	
	private int mElement;
	private String mElementString;
	
	public Element( int element )
	{
		mElement = element;
		mElementString = elementStrings[ mElement ];
	}
	
	public int getElement()
	{
		return mElement;
	}
	
	public String getElementString()
	{
		return mElementString;
	}
	
	@Override
	public int compareTo( Element o )
	{
		int compare = this.mElement - o.mElement;
		if ( this.equals( o ) )
		{
			return 0;
		}
		else if ( compare == -1 || compare == 2 )
		{
			return 2;
		}
		else if ( compare == -2 || compare == 1 )
		{
			return -2;
		}
		return 0;
	}
	
	@Override
	public boolean equals( Object o )
	{
		if ( this == o )
		{
			return true;
		}
		if ( !( o instanceof Element ) )
		{
			return false;
		}
		Element e = ( Element ) o;
		
		return this.mElement == e.mElement;
	}
	
	@Override
	public int hashCode()
	{
		return mElementString.hashCode();
	}
	
	@Override
	public String toString()
	{
		return mElementString;
	}
}
