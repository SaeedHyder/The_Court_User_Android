package com.app.court.helpers;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.AnyTextView;

public class InputHelper {
	
	public static boolean checkWhiteSpace( AnyEditTextView edtext,
			Activity activity ) {
		
		String test = edtext.getText().toString();
		
		if ( test.startsWith( " " ) ) {
			Funcs.showShortToast(
					"Field should not contain white space in start", activity );
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean checkWhiteSpace( TextView edtext, Activity activity ) {
		
		String test = edtext.getText().toString();
		
		if ( test.startsWith( " " ) ) {
			Funcs.showShortToast(
					"Field should not contain white space in start", activity );
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean matchPasswords( AnyEditTextView edtPassword,
			AnyEditTextView edtConfirmPassword, Activity activity ) {
		if ( edtPassword.getText().toString()
				.equals( edtConfirmPassword.getText().toString() ) ) {
			return true;
		} else {
			UIHelper.showLongToastInCenter( activity,
					"Password and Confirm Password do not match" );
			return false;
		}
	}
	
	public static void showDatePickerDialogWithFutureCheck(
			final Activity activity, final AnyEditTextView editText ) {
		
		// 0 for From date - 1 for To Date
		
		// Get Cuttent Date Instance
		final Calendar c = Calendar.getInstance();
		final Time currentTime = new Time();
		
		int mYear = c.get( Calendar.YEAR );
		int mMonth = c.get( Calendar.MONTH );
		int mDay = c.get( Calendar.DAY_OF_MONTH );
		
		currentTime.set( mDay - 1, mMonth, mYear );
		
		DatePickerDialog dateDlg = new DatePickerDialog( activity,
				new DatePickerDialog.OnDateSetListener() {
					
					public void onDateSet( DatePicker view, int year,
							int monthOfYear, int dayOfMonth ) {
						
						Time chosenDate = new Time();
						
						chosenDate.set( dayOfMonth, monthOfYear, year );
						
						long dtDob = chosenDate.toMillis( true );
						
						CharSequence strDate = DateFormat.format( "yyyy-MM-dd",
								dtDob );
						
						Log.i( chosenDate + "", currentTime + "" );
						
						if ( chosenDate.after( currentTime ) ) {
							UIHelper.showLongToastInCenter( activity,
									"Invalid Date Selected" );
							
						}
						
						else {
							
							editText.setText( strDate );
						}
					}
					
				}, mYear, mMonth, mDay );
		
		dateDlg.show();
	}
	
	public static void showDatePickerDialogWithFutureCheck(
			final Activity activity, final AnyTextView textView ) {
		
		// 0 for From date - 1 for To Date
		
		// Get Cuttent Date Instance
		final Calendar c = Calendar.getInstance();
		final Time currentTime = new Time();
		
		int mYear = c.get( Calendar.YEAR );
		int mMonth = c.get( Calendar.MONTH );
		int mDay = c.get( Calendar.DAY_OF_MONTH );
		
		currentTime.set( mDay, mMonth, mYear );
		
		DatePickerDialog dateDlg = new DatePickerDialog( activity,
				new DatePickerDialog.OnDateSetListener() {
					
					public void onDateSet( DatePicker view, int year,
							int monthOfYear, int dayOfMonth ) {
						
						Time chosenDate = new Time();
						
						chosenDate.set( dayOfMonth, monthOfYear, year );
						
						long dtDob = chosenDate.toMillis( true );
						
						CharSequence strDate = DateFormat.format( "yyyy-MM-dd",
								dtDob );
						
						if ( chosenDate.after( currentTime ) ) {
							UIHelper.showLongToastInCenter( activity,
									"Cannot select future date" );
						} else if ( chosenDate.equals( currentTime ) ) {
							UIHelper.showLongToastInCenter( activity,
									"Cannot select current date" );
						} else {
							
							textView.setText( strDate );
						}
					}
					
				}, mYear, mMonth, mDay );
		
		dateDlg.show();
	}
	
	public static void showDatePickerDialogWithPastCheck(
			final Activity activity, final AnyEditTextView editText ) {
		
		// 0 for From date - 1 for To Date
		
		// Get Cuttent Date Instance
		final Calendar c = Calendar.getInstance();
		final Time currentTime = new Time();
		
		int mYear = c.get( Calendar.YEAR );
		int mMonth = c.get( Calendar.MONTH );
		int mDay = c.get( Calendar.DAY_OF_MONTH );
		
		currentTime.set( mDay, mMonth, mYear );
		
		DatePickerDialog dateDlg = new DatePickerDialog( activity,
				new DatePickerDialog.OnDateSetListener() {
					
					public void onDateSet( DatePicker view, int year,
							int monthOfYear, int dayOfMonth ) {
						
						Time chosenDate = new Time();
						
						chosenDate.set( dayOfMonth, monthOfYear, year );
						
						long dtDob = chosenDate.toMillis( true );
						
						CharSequence strDate = DateFormat.format( "yyyy-MM-dd",
								dtDob );
						
						if ( chosenDate.before( currentTime ) ) {
							UIHelper.showLongToastInCenter( activity,
									"Cannot select past date" );
						} else if ( chosenDate.equals( currentTime ) ) {
							UIHelper.showLongToastInCenter( activity,
									"Cannot select current date" );
						} else {
							
							editText.setText( strDate );
						}
					}
					
				}, mYear, mMonth, mDay );
		
		dateDlg.show();
	}
	
	public static void showDatePickerDialogWithPastCheck(
			final Activity activity, final AnyTextView textView ) {
		
		// 0 for From date - 1 for To Date
		
		// Get Cuttent Date Instance
		final Calendar c = Calendar.getInstance();
		final Time currentTime = new Time();
		
		int mYear = c.get( Calendar.YEAR );
		int mMonth = c.get( Calendar.MONTH );
		int mDay = c.get( Calendar.DAY_OF_MONTH );
		
		currentTime.set( mDay, mMonth, mYear );
		
		DatePickerDialog dateDlg = new DatePickerDialog( activity,
				new DatePickerDialog.OnDateSetListener() {
					
					public void onDateSet( DatePicker view, int year,
							int monthOfYear, int dayOfMonth ) {
						
						Time chosenDate = new Time();
						
						chosenDate.set( dayOfMonth, monthOfYear, year );
						
						long dtDob = chosenDate.toMillis( true );
						
						CharSequence strDate = DateFormat.format( "yyyy-MM-dd",
								dtDob );
						
						if ( chosenDate.before( currentTime ) ) {
							UIHelper.showLongToastInCenter( activity,
									"Cannot select past date" );
						} else if ( chosenDate.equals( currentTime ) ) {
							UIHelper.showLongToastInCenter( activity,
									"Cannot select current date" );
						} else {
							
							textView.setText( strDate );
						}
					}
					
				}, mYear, mMonth, mDay );
		
		dateDlg.show();
	}
	
}
