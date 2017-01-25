package com.haroldagnote.android.firewatergrass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class GameActivity extends Activity
{
	private static final String TAG = "GameActivity";
	private static final String COMPUTER_INSTANCE = "com.haroldagnote.android.firewatergrass.computer_instance";

	private Computer psychic;
	private Element player;

	private int wins;
	private int losses;

	private TextView playerScore;
	private TextView psychicScore;

	private Button go;

	private ImageButton fireButton;
	private ImageButton waterButton;
	private ImageButton grassButton;

	private ImageView playerHand;
	private ImageView psychicHand;

	public static Intent newIntent( Context packageContext, Computer computer )
	{
		Intent i = new Intent( packageContext, GameActivity.class );
		i.putExtra( COMPUTER_INSTANCE, computer );
		return i;
	}

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_game );

		psychic = ( Computer ) getIntent().getSerializableExtra( COMPUTER_INSTANCE );

		wins = 0;
		losses = 0;
		playerScore = ( TextView ) findViewById( R.id.player_score );
		psychicScore = ( TextView ) findViewById( R.id.cpu_score );

		go = ( Button ) findViewById( R.id.confirm_button );
		go.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				if ( player != null )
				{
					showPsychicHand();
					game( player );
				}
			}
		} );

		fireButton = ( ImageButton ) findViewById( R.id.fire_button );
		fireButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				player = new Element( 0 );
				updateHand( playerHand, player );
				hidePsychicHand();
			}
		} );
		waterButton = ( ImageButton ) findViewById( R.id.water_button );
		waterButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				player = new Element( 1 );
				updateHand( playerHand, player );
				hidePsychicHand();
			}
		} );
		grassButton = ( ImageButton ) findViewById( R.id.grass_button );
		grassButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				player = new Element( 2 );
				updateHand( playerHand, player );
				hidePsychicHand();
			}
		} );

		playerHand = ( ImageView ) findViewById( R.id.player_hand );
		psychicHand = ( ImageView ) findViewById( R.id.psychic_hand );
	}

	public boolean onKeyDown( int keyCode, KeyEvent event )
	{
		if ( keyCode == KeyEvent.KEYCODE_BACK )
		{
			exitByBackKey();

			//moveTaskToBack(false);

			return true;
		}
		return super.onKeyDown( keyCode, event );
	}

	protected void exitByBackKey()
	{

		AlertDialog alertbox = new AlertDialog.Builder( this )
				.setMessage( "Do you want to save the Computer State?" )
				.setPositiveButton( "Yes", new DialogInterface.OnClickListener()
				{

					public void onClick( DialogInterface arg0, int arg1 )
					{
						GameActivity.this.saveComputer();
						finish();
						//close();


					}
				} )
				.setNegativeButton( "No", new DialogInterface.OnClickListener()
				{

					// do something when the button is clicked
					public void onClick( DialogInterface arg0, int arg1 )
					{
						finish();
					}
				} )
				.show();

	}

	@Override
	public void onStart()
	{
		super.onStart();
		Log.d( TAG, "onStart() called" );
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Log.d( TAG, "onPause() called" );
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Log.d( TAG, "onResume() called" );
	}

	@Override
	public void onStop()
	{
		super.onStop();
		Log.d( TAG, "onStop() called" );
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.d( TAG, "onDestroy() called" );
	}

	private void updateHand( ImageView imageView, Element element )
	{
		switch ( element.getElementString() )
		{
			case "Fire":
				imageView.setImageResource( R.drawable.fireimage );
				break;
			case "Water":
				imageView.setImageResource( R.drawable.waterimage );
				break;
			case "Grass":
				imageView.setImageResource( R.drawable.grassimage );
				break;
		}
	}

	private void game( Element playerChoice )
	{
		Element computerChoice = psychic.chooseElement();
		updateHand( psychicHand, computerChoice );
		int game = playerChoice.compareTo( computerChoice );
		psychic.rememberElement( playerChoice );
		if ( game > 0 )
		{
			losses++;
		}
		else if ( game < 0 )
		{
			wins++;
		}
		updateScoreboard();
	}

	private void updateScoreboard()
	{
		playerScore.setText( Integer.toString( wins ) );
		psychicScore.setText( Integer.toString( losses ) );
	}

	private void hidePsychicHand()
	{
		psychicHand.setVisibility( View.INVISIBLE );
	}

	private void showPsychicHand()
	{
		psychicHand.setVisibility( View.VISIBLE );
	}

	private void saveComputer()
	{
		try
		{
			FileOutputStream file = this.openFileOutput( "computer.dat", Context.MODE_PRIVATE );
			ObjectOutputStream objectStream = new ObjectOutputStream( file );
			objectStream.writeObject( psychic );
			objectStream.close();
			file.close();
		}
		catch ( IOException io )
		{
			System.out.println( "Error - Failed to Process File" );
			System.out.println( io );
		}
	}
}
