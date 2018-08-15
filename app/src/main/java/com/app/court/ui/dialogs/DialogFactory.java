package com.app.court.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;

import com.app.court.activities.DockActivity;
import com.app.court.fragments.MapControllerDialog;
import com.app.court.fragments.MapControllerFragment;

public class DialogFactory {
	
	public static Dialog createProgressDialog( Activity activity, String title,
			String loadingMessage ) {
		ProgressDialog prDialog = new ProgressDialog( activity );
		prDialog.setTitle( title );
		prDialog.setMessage( loadingMessage );
		return prDialog;
	}
	
	public static Dialog createQuitDialog( Activity activity,
			DialogInterface.OnClickListener dialogPositive, int messageId ) {
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setIcon( android.R.drawable.ic_dialog_alert )
				.setTitle( android.R.string.dialog_alert_title )
				.setMessage( messageId )
				.setCancelable( true )
				.setPositiveButton( android.R.string.yes, dialogPositive )
				.setNegativeButton( android.R.string.no,
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick( DialogInterface dialog,
									int which ) {
								dialog.dismiss();
								
							}
						} );
		return builder.create();
		
	}
	
	public static Dialog createSimpleDialog( Activity activity,
			DialogInterface.OnClickListener dialogPositive, int messageId,
			int titleId ) {
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setIcon( android.R.drawable.ic_dialog_alert )
				.setTitle( android.R.string.dialog_alert_title )
				.setMessage( messageId )
				.setCancelable( true )
				.setPositiveButton( android.R.string.yes, dialogPositive )
				.setNegativeButton( android.R.string.no,
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick( DialogInterface dialog,
									int which ) {
								dialog.dismiss();
								
							}
						} );
		return builder.create();
		
	}
	
	public static Dialog createMessageDialog( Activity activity,
			DialogInterface.OnClickListener dialogPositive,
			CharSequence message, String titleId ) {
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setIcon( android.R.drawable.ic_dialog_alert )
				.setTitle( titleId )
				.setMessage( message ).setCancelable( true )
				.setPositiveButton( android.R.string.ok, dialogPositive );
		return builder.create();
		
	}
	
	public static Dialog createMessageDialog2( Activity activity,
			DialogInterface.OnClickListener dialogPositive,
			CharSequence message, String titleId ) {
		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setIcon( android.R.drawable.ic_dialog_alert )
				.setTitle( android.R.string.dialog_alert_title )
				.setMessage( message )
				.setCancelable( true )
				.setPositiveButton( "Yes", dialogPositive )
				.setNegativeButton( "No",
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick( DialogInterface dialog,
									int which ) {
								dialog.dismiss();
								
							}
						} );
		return builder.create();
		
	}
	
	public static Dialog createInputDialog( Activity activity,
			DialogInterface.OnClickListener dialogPositive,
			DialogInterface.OnClickListener dialogNegative, String title,
			String message ) {
		
		AlertDialog.Builder alert = new AlertDialog.Builder( activity );
		alert.setIcon( android.R.drawable.ic_dialog_alert );
		alert.setTitle( title );
		alert.setMessage( message );
		final EditText input = new EditText( activity );
		alert.setView( input );
		
		alert.setPositiveButton( "Yes", dialogPositive );
		
		alert.setNegativeButton( "No", dialogNegative );
		
		return alert.create();
		
	}

	public static void showMapControllerDialog(DockActivity activity, MapControllerFragment fragment) {

		FragmentManager fm = activity.getSupportFragmentManager();

		MapControllerDialog dialogFragment = new MapControllerDialog();
		dialogFragment.setFragment(fragment);
		dialogFragment.show(fm, "MapControllerFragment");

	}
	
	
}
