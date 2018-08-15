package com.app.court.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class CustomButton extends ImageButton {
	
	public CustomButton( Context context ) {
		super( context );
	}
	
	public CustomButton( Context context, AttributeSet attrs ) {
		super( context, attrs );
	}
	
	public CustomButton( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs, defStyle );
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setBackgroundDrawable( Drawable d ) {
		// Replace the original background drawable (e.g. image) with a
		// LayerDrawable that
		// contains the original drawable.
		CustomButtonBackgroundDrawable layer = new CustomButtonBackgroundDrawable( d );
		super.setBackgroundDrawable( layer );
	}
	
	/**
	 * The stateful LayerDrawable used by this button.
	 */
	protected class CustomButtonBackgroundDrawable extends LayerDrawable {
		
		// The color filter to apply when the button is pressed
		protected ColorFilter _pressedFilter = new LightingColorFilter( Color.LTGRAY, 1 );
		
		// Alpha value when the button is disabled
		protected int _disabledAlpha = 100;
		
		// Alpha at default state
		protected int _defaultAlpha = 255;
		
		public CustomButtonBackgroundDrawable( Drawable d ) {
			super( new Drawable[] { d } );
		}
		
		@Override
		protected boolean onStateChange( int[] states ) {
			boolean enabled = false;
			boolean pressed = false;
			
			for ( int state : states ) {
				if ( state == android.R.attr.state_enabled )
					enabled = true;
				else if ( state == android.R.attr.state_pressed )
					pressed = true;
			}
			
			mutate();
			if ( enabled && pressed ) {
				setColorFilter( _pressedFilter );
			} else if ( !enabled ) {
				setColorFilter( null );
				setAlpha( _disabledAlpha );
			} else {
				setColorFilter( null );
				setAlpha( _defaultAlpha );
			}
			
			invalidateSelf();
			
			return super.onStateChange( states );
		}
		
		@Override
		public boolean isStateful() {
			return true;
		}
	}
	
}