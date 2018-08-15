package com.app.court.ui.views;

import com.andreabaccega.widget.FormEditText;

import android.content.Context;
import android.util.AttributeSet;

public class AnyEditTextView extends FormEditText {
	
	public AnyEditTextView( Context context ) {
		super( context );
		
	}
	
	public AnyEditTextView( Context context, AttributeSet attrs ) {
		super( context, attrs );
		
		if ( !this.isInEditMode() ) {
			Util.setTypeface( attrs, this );
		}
	}
	
	public AnyEditTextView( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs, defStyle );
		
		if ( !this.isInEditMode() ) {
			Util.setTypeface( attrs, this );
		}
		
	}
	
}
