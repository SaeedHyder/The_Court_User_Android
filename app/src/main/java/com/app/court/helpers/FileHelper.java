package com.app.court.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;



public class FileHelper {
	
	private Context ctx;
	

	public FileHelper( Context ctx ) {
		this.ctx = ctx;
		
	}
	
	/**
	 * This image file can be used for a temporary location to store image file
	 * on an external location which can be used for tasks such as displaying an
	 * internal use of app.
	 * 
	 * Also deletes existing image file.
	 * 
	 * Dont Use it for external purposes.
	 * 
	 * @return
	 */
	public File getTempImageFile() {
		File file = new File( getAvailableCache(), "tempImage.jpg" );
		if ( file.exists() )
			file.delete();
		return file;
		
	}
	
	public File getTempTxtFile(String fileName) {
		File file = new File( getAvailableCache(), fileName + ".txt" );
		if ( file.exists() )
			file.delete();
		return file;
		
	}
	
	public File getHttpCacheFile() {
		File file = new File( getAvailableCache(), "httpcache" );
		return file;
		
	}
	
	public File getAvailableCache() {
		if ( OSHelper.isExerternalStorageAvailable() ) {
			if ( ctx.getExternalCacheDir() != null )
				ctx.getExternalCacheDir().mkdirs();
			return ctx.getExternalCacheDir();
		} else {
			ctx.getCacheDir().mkdirs();
			return ctx.getCacheDir();
		}
	}
	
	public void writeToFile( File file  , String content) {
		
		try {
			
			FileOutputStream fos = new FileOutputStream( file );
			String string = content ;
			fos.write( string.getBytes() );
			fos.close();
			
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
}
