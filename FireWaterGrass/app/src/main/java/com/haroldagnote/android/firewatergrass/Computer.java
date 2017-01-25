package com.haroldagnote.android.firewatergrass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * "Class Description"
 *
 * @author Harold Agnote
 */
public class Computer implements Serializable
{
	private ArrayList < String > taunts;
	private ArrayList < String > winSpeeches;
	private ArrayList < String > loseSpeeches;
	private HashMap < Pattern, Integer > patterns;
	private Element[] currentPattern;
	private int currentIndex;
	private boolean readyToPredict;
	private boolean seeThoughts;
	private String thoughts;
	
	
	public Computer()
	{
		patterns = new HashMap < Pattern, Integer >();
		seeThoughts = false;
		currentIndex = 0;
		currentPattern = new Element[ 5 ];
		readyToPredict = false;
		thoughts = "";
		for ( int i = 0; i < currentPattern.length; i++ )
		{
			currentPattern[ i ] = null;
		}
		initializeSpeeches();
	}
	
	public void resetThoughts()
	{
		thoughts = "";
	}
	
	public String getThoughts()
	{
		if ( seeThoughts )
		{
			return thoughts;
		}
		else
		{
			return "";
		}
	}
	
	public boolean switchSeeThoughts()
	{
		seeThoughts = !seeThoughts;
		return seeThoughts;
	}
	
	public boolean isSeeThoughts()
	{
		return seeThoughts;
	}
	
	public boolean isReadyToPredict()
	{
		return readyToPredict;
	}
	
	public Element chooseElement()
	{
		Element myElement;
		resetThoughts();
		if ( readyToPredict )
		{
			Element predictedElement = predictElement();
			myElement = chooseBestElement( predictedElement );
		}
		else
		{
			myElement = chooseRandomElement();
		}
		return myElement;
	}
	
	public Element predictElement()
	{
		Element element;
		
		element = predictElement( currentIndex );
		
		return element;
	}
	
	public Element predictElement( int patternLength )
	{
		Element element = null;
		int max = 0;
		Pattern[] possible = new Pattern[ 3 ];
		Element[] lastThrows = new Element[ patternLength ];
		String lastThrowString = "";
		for ( int i = 0; i < patternLength; i++ )
		{
			lastThrows[ i ] = currentPattern[ currentPattern.length - patternLength - 1 + i ];
			lastThrowString += lastThrows[ i ];
		}
		for ( int i = 0; i < possible.length; i++ )
		{
			possible[ i ] = new Pattern( lastThrowString + new Element( i ) );
		}
		if ( seeThoughts )
		{
			thoughts += "Your last ( " + patternLength + " ) choices were: \n" + lastThrowString + "\n";
			thoughts += "So, the possible outcomes of these patterns are: \n";
			for ( int i = 0; i < possible.length; i++ )
			{
				thoughts += possible[ i ] + "\n";
			}
		}
		for ( int i = 0; i < possible.length; i++ )
		{
			if ( patterns.containsKey( possible[ i ] ) )
			{
				thoughts += "You've played " + possible[ i ] + " " + patterns.get( possible[ i ] ) + " times. \n";
				if ( patterns.get( possible[ i ] ) > max )
				{
					max = patterns.get( possible[ i ] );
					element = new Element( i );
				}
			}
			else
			{
				thoughts += "There is no record of you using the pattern " + possible[ i ] + "\n";
			}
		}
		if ( max == 0 )
		{
			if ( patternLength == 1 )
			{
				element = chooseRandomElement();
				thoughts += "I'm not sure what you will pick so I will guess that you will throw " + element + "\n";
			}
			else
			{
				element = predictElement( patternLength - 1 );
			}
		}
		else
		{
			thoughts += "Based on these calculations, I will predict that you will throw " + element + "\n";
		}
		return element;
	}
	
	public Element chooseBestElement( Element e )
	{
		int index = 0;
		Element choose = new Element( index );
		while ( choose.compareTo( e ) >= 0 )
		{
			index++;
			choose = new Element( index );
		}
		return choose;
	}
	
	public Element chooseRandomElement()
	{
		Element element = new Element( ( int ) ( Math.random() * 3 ) );
		
		return element;
	}
	
	public void rememberElement( Element e )
	{
		//Debug Purposes
		resetThoughts();
		thoughts += "I am remembering that you played " + e + "\n";
		currentPattern[ currentIndex ] = e;
		currentIndex++;
		String patternLine = "";
		if ( currentIndex > 1 )
		{
			patternLine = "";
			for ( int i = 0; i < currentPattern.length - 3; i++ )
			{
				patternLine += currentPattern[ currentIndex - 2 + i ];
			}
			rememberPattern( patternLine );
		}
		if ( currentIndex > 2 )
		{
			patternLine = "";
			for ( int i = 0; i < currentPattern.length - 2; i++ )
			{
				patternLine += currentPattern[ currentIndex - 3 + i ];
			}
			rememberPattern( patternLine );
		}
		if ( currentIndex > 3 )
		{
			patternLine = "";
			for ( int i = 0; i < currentPattern.length - 1; i++ )
			{
				patternLine += currentPattern[ currentIndex - 4 + i ];
			}
			rememberPattern( patternLine );
		}
		if ( currentIndex > 4 )
		{
			patternLine = "";
			for ( int i = 0; i < currentPattern.length; i++ )
			{
				patternLine += currentPattern[ i ];
				if ( i < currentPattern.length - 1 )
				{
					if ( currentPattern[ i + 1 ] != null )
					{
						currentPattern[ i ] = currentPattern[ i + 1 ];
					}
				}
			}
			rememberPattern( patternLine );
			currentIndex--;
		}
	}
	
	public void rememberPattern( String patternText )
	{
		Pattern pattern = new Pattern( patternText );
		//Debug Purposes
		thoughts += "I am remembering a pattern played: " + pattern + "\n";
		if ( currentIndex > 4 )
		{
			readyToPredict = true;
		}
		
		if ( patterns.containsKey( pattern ) )
		{
			patterns.put( pattern, patterns.get( pattern ) + 1 );
			thoughts += "I have this pattern \n";
		}
		else
		{
			patterns.put( pattern, 1 );
			thoughts += "I don't have this pattern \n";
		}
		thoughts += "You've used this pattern " + patterns.get( pattern ) + "\n";
	}
	
	public void initializeSpeeches()
	{
		taunts = initializeSpeech( "psychicTauntSpeeches.txt" );
		winSpeeches = initializeSpeech( "psychicWinSpeeches.txt" );
		loseSpeeches = initializeSpeech( "psychicLoseSpeeches.txt" );
	}
	
	public ArrayList < String > initializeSpeech( String file )
	{
		ArrayList < String > speeches = new ArrayList < String >();
		try
		{
			Scanner read = new Scanner( new File( file ) );
			while ( read.hasNext() )
			{
				String line = read.nextLine();
				speeches.add( line );
			}
			read.close();
		}
		catch ( FileNotFoundException fnf )
		{
			System.out.println( "Error - File Not Found" );
		}
		return speeches;
	}
	
	public String taunt()
	{
		int random = ( int ) ( Math.random() * taunts.size() );
		return taunts.get( random );
	}
	
	public String win()
	{
		int random = ( int ) ( Math.random() * winSpeeches.size() );
		return winSpeeches.get( random );
	}
	
	public String lose()
	{
		int random = ( int ) ( Math.random() * loseSpeeches.size() );
		return loseSpeeches.get( random );
	}
	
}
