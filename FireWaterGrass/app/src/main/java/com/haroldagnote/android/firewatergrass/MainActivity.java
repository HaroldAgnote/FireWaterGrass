package com.haroldagnote.android.firewatergrass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainActivity extends Activity
{

	private static final String TAG = "MainActivity";
	private static final String KEY_COMPUTER = "computer";

	private Computer psychic;

	private Button noviceButton;
	private Button veteranButton;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		noviceButton = ( Button ) findViewById( R.id.novice_button );
		noviceButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				psychic = new Computer();
				Intent i = GameActivity.newIntent( MainActivity.this, psychic );
				startActivity( i );
			}
		} );

		veteranButton = ( Button ) findViewById( R.id.veteran_button );
		veteranButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				psychic = loadComputer();
				if ( psychic != null )
				{
					Intent i = GameActivity.newIntent( MainActivity.this, psychic );
					startActivity( i );
				}
			}
		} );
	}

	private Computer loadComputer()
	{
		Computer computer = null;
		try
		{
			FileInputStream file = this.openFileInput( "computer.dat" );
			ObjectInputStream objectStream = new ObjectInputStream( file );
			computer = ( Computer ) objectStream.readObject();
			objectStream.close();
			file.close();
		}
		catch ( IOException io )
		{
			AlertDialog alertBox = new AlertDialog.Builder(this)
					.setMessage("Error - Failed to load computer state!\n" + io)
					.setNeutralButton( "Ok", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick( DialogInterface arg0, int arg1 )
						{

						}
					}).show();
		}
		catch ( ClassNotFoundException cnf )
		{
			AlertDialog alertBox = new AlertDialog.Builder(this)
					.setMessage("Error - Failed to load computer state!\n" + cnf)
					.setNeutralButton( "Ok", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick( DialogInterface arg0, int arg1 )
						{

						}
					}).show();
		}
		return computer;
	}
}
