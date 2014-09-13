/* Copyright (C) 2008 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the License. */
package com.example.shareapp.tools;


import java.io.IOException;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;


/**
 * Various utilities shared amongst the Launcher's classes.
 */
final public class Utilities
{
	private static int icon_size = 50;
	private static int icon_bg_num = 1;
	private static int sIconWidth = -1;
	private static int sIconHeight = -1;
	public static int sIconTextureWidth = -1;
	public static int sIconTextureHeight = -1;
	private static final Rect sOldBounds = new Rect();
	private static final Canvas sCanvas = new Canvas();
	private static final Paint sPaint = new Paint();
	private static Bitmap[] iconBgs;
	private static Bitmap mask;
	static
	{
		sCanvas.setDrawFilter( new PaintFlagsDrawFilter( Paint.DITHER_FLAG , Paint.FILTER_BITMAP_FLAG ) );
	}
	
	private static Bitmap getIconBg()
	{
		if( iconBgs == null )
			return null;
		if( iconBgs.length == 0 )
			return null;
		return iconBgs[(int)( Math.random() * iconBgs.length )];
	}
	
	private static Bitmap getBitmap(
			Context context ,
			String filename )
	{
		try
		{
			return BitmapFactory.decodeStream( context.getAssets().open( filename ) );
		}
		catch( IOException e )
		{
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	/**
	 * Returns a bitmap suitable for the all apps view.  The bitmap will be a power
	 * of two sized ARGB_8888 bitmap that can be used as a gl texture.
	 */
	public static Bitmap createIconBitmap(
			Drawable icon ,
			Context context )
	{
		synchronized( sCanvas )
		{ // we share the statics :-(
			if( sIconWidth == -1 )
			{
				initStatics( context );
			}
			int width = sIconWidth;
			int height = sIconHeight;
			if( icon instanceof PaintDrawable )
			{
				PaintDrawable painter = (PaintDrawable)icon;
				painter.setIntrinsicWidth( width );
				painter.setIntrinsicHeight( height );
			}
			else if( icon instanceof BitmapDrawable )
			{
				// Ensure the bitmap has a density.
				BitmapDrawable bitmapDrawable = (BitmapDrawable)icon;
				Bitmap bitmap = bitmapDrawable.getBitmap();
				if( bitmap.getDensity() == Bitmap.DENSITY_NONE )
				{
					bitmapDrawable.setTargetDensity( context.getResources().getDisplayMetrics() );
				}
			}
			int sourceWidth = icon.getIntrinsicWidth();
			int sourceHeight = icon.getIntrinsicHeight();
			if( sourceWidth > 0 && sourceHeight > 0 )
			{
				// There are intrinsic sizes.
				//				if( width < sourceWidth || height < sourceHeight )
				//				{
				//					// It's too big, scale it down.
				//					final float ratio = (float)sourceWidth / sourceHeight;
				//					if( sourceWidth > sourceHeight )
				//					{
				//						height = (int)( width / ratio );
				//					}
				//					else if( sourceHeight >= sourceWidth )
				//					{
				//						width = (int)( height * ratio );
				//					}
				//					width = (int)( width * 0.9 );
				//					height = (int)( height * 0.9 );
				//				}
				//				else if( sourceWidth < width && sourceHeight < height )
				//				{
				//					// It's small, use the size they gave us.
				//					final float ratio = (float)width / height;
				//					if( width > height )
				//					{
				//						height = (int)( sourceWidth / ratio );
				//					}
				//					else if( height > width )
				//					{
				//						width = (int)( sourceHeight * ratio );
				//					}
				//					
				//				}
				width = (int)( width  );
				height = (int)( height  );
			}
			// no intrinsic size --> use default size
			int textureWidth = sIconTextureWidth;
			int textureHeight = sIconTextureHeight;
			final Bitmap bitmap = Bitmap.createBitmap( textureWidth , textureHeight , Bitmap.Config.ARGB_8888 );
			final Canvas canvas = sCanvas;
			canvas.setBitmap( bitmap );
			final int left = ( textureWidth - width ) / 2;
			final int top = ( textureHeight - height ) / 2;
			sOldBounds.set( icon.getBounds() );
			icon.setBounds( left , top , left + width , top + height );
			Bitmap iconBg = getIconBg();
			if( iconBg != null )
			{
//				canvas.drawBitmap( iconBg , 0 , 0 , null );
			}
			int saveLayer = -1;
			if( mask != null )
			{
				saveLayer = canvas.saveLayer(
						0 ,
						0 ,
						textureWidth ,
						textureHeight ,
						null ,
						Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG );
			}
			icon.draw( canvas );
			icon.setBounds( sOldBounds );
			if( mask != null && saveLayer != -1 )
			{
				sPaint.setColor( Color.WHITE );
				sPaint.setAntiAlias( true );
				sPaint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.MULTIPLY ) );
				canvas.drawBitmap( mask , 0 , 0 , sPaint );
				sPaint.setXfermode( null );
				canvas.restoreToCount( saveLayer );
			}
			return bitmap;
		}
	}
	
	/**
	 * Returns a Bitmap representing the thumbnail of the specified Bitmap.
	 * The size of the thumbnail is defined by the dimension
	 * android.R.dimen.launcher_application_icon_size.
	 *
	 * @param bitmap The bitmap to get a thumbnail of.
	 * @param context The application's context.
	 *
	 * @return A thumbnail for the specified bitmap or the bitmap itself if the
	 *         thumbnail could not be created.
	 */
	public static Bitmap resampleIconBitmap(
			Bitmap bitmap ,
			Context context )
	{
		synchronized( sCanvas )
		{ // we share the statics :-(
			if( sIconWidth == -1 )
			{
				initStatics( context );
			}
			if( bitmap.getWidth() == sIconTextureWidth && bitmap.getHeight() == sIconTextureHeight )
			{
				return bitmap;
			}
			else
			{
				return createIconBitmap( new BitmapDrawable( bitmap ) , context );
			}
		}
	}
	
	public static void initStatics(
			Context context )
	{
		sIconWidth = sIconHeight = (int)( DensityUtil.dp2px(context, icon_size) - 2 );
		sIconTextureWidth = sIconTextureHeight = DensityUtil.dp2px(context, icon_size) ;
		Bitmap oriMask = getBitmap( context , "theme/iconbg/mask.png" );
		if( oriMask != null )
		{
			mask = Bitmap.createScaledBitmap( oriMask , DensityUtil.dp2px(context, icon_size) , DensityUtil.dp2px(context, icon_size) , true );
			//mask = oriMask;
			if( oriMask != mask )
				oriMask.recycle();
		}
	}
}
