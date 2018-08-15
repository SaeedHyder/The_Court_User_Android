package com.app.court.helpers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.app.court.R;

public class AnimationHelper {
	
	
	public static Animation getScaleDownToHalfAnimation(Context context){
		return AnimationUtils.loadAnimation(context, R.anim.scale_down_half);
	}
	
	public static Animation getScaleUpAnimaation(Context context){
		return AnimationUtils.loadAnimation(context, R.anim.scale_up);
	}
	
	
	
	public static void animateRise( final ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f );
		animation.setDuration( 500 );
		set.addAnimation( animation );
		
		animation.setAnimationListener( new AnimationListener() {
			
			@Override
			public void onAnimationStart( Animation animation ) {
			}
			
			@Override
			public void onAnimationRepeat( Animation animation ) {
			}
			
			@Override
			public void onAnimationEnd( Animation animation ) {
				mLayout.setVisibility( View.INVISIBLE );
			}
		} );
		
		mLayout.startAnimation( set );
		
	}
	
	public static void animateFall( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		animation.setDuration( 500 );
		set.addAnimation( animation );
		
		mLayout.startAnimation( set );
		
	}
	
	public static void animateLayoutSlideDown( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		animation.setDuration( 150 );
		set.addAnimation( animation );
		
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f );
		mLayout.setLayoutAnimation( controller );
		
	}
	
	public static void animateLayoutSlideToRight( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f );
		mLayout.setLayoutAnimation( controller );
		
	}
	
	public static void animateLayoutSlideFromRight( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f );
		mLayout.setLayoutAnimation( controller );
		
	}
	
	public static void animateLayoutSlideToLeft( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f );
		mLayout.setLayoutAnimation( controller );
		
	}
	
	public static void animateFromRight( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		animation.setDuration( 500 );
		set.addAnimation( animation );
		
		mLayout.startAnimation( set );
		
	}
	
	public static void animateToRight( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		animation.setDuration( 500 );
		set.addAnimation( animation );
		
		mLayout.startAnimation( set );
		
	}
	
	
	
	public static void animateScaleUp( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		
		//scale animation
		Animation animation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		animation.setDuration( 500 );
		set.addAnimation( animation );
		
		//start animation
		mLayout.startAnimation( set );
		
	}
	
	
	
	/**
	 * 
	 * 
	 **/
	
	// for the previous movement
	public static Animation inFromRightAnimation( long animtime ) {
		
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f );
		inFromRight.setDuration( animtime );
		inFromRight.setInterpolator( new AccelerateInterpolator() );
		return inFromRight;
	}
	
	public static Animation inFromBottomAnimation( long animtime ) {
		
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f );
		inFromRight.setDuration( animtime );
		inFromRight.setInterpolator( new AccelerateInterpolator() );
		return inFromRight;
	}
	
	public static Animation inFromTopAnimation( long animtime ) {
		
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f );
		inFromRight.setDuration( animtime );
		inFromRight.setInterpolator( new AccelerateInterpolator() );
		return inFromRight;
	}
	
	public static Animation outToLeftAnimation( long animtime ) {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f );
		outtoLeft.setDuration( animtime );
		outtoLeft.setInterpolator( new AccelerateInterpolator() );
		return outtoLeft;
	}
	
	// for the next movement
	public static Animation inFromLeftAnimation( long animtime ) {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f );
		inFromLeft.setDuration( animtime );
		inFromLeft.setInterpolator( new AccelerateInterpolator() );
		return inFromLeft;
	}
	
	public static Animation outToRightAnimation( long animtime ) {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f );
		outtoRight.setDuration( animtime );
		outtoRight.setInterpolator( new AccelerateInterpolator() );
		return outtoRight;
	}
	
	public static Animation outToTopAnimation( long animtime ) {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f );
		outtoRight.setDuration( animtime );
		outtoRight.setInterpolator( new AccelerateInterpolator() );
		return outtoRight;
	}
	
	public static Animation outToBottomAnimation( long animtime ) {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f );
		outtoRight.setDuration( animtime );
		outtoRight.setInterpolator( new AccelerateInterpolator() );
		return outtoRight;
	}
}