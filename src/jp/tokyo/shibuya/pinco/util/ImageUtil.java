package jp.tokyo.shibuya.pinco.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;

public class ImageUtil {
	
	/**
	 * ビットマップデータをバイト配列に変換
	 * 
	 * @param bmp
	 *            ビットマップ
	 * @return byte[] ビットマップをバイト配列にした値
	 */
	public static byte[] Bmp2Byte(Bitmap bmp) {

		if (bmp == null || "".equals(bmp)) {
			return null;
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// compress(フォーマット, クォリティ(0〜100), ストリーム)
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		return byteArray;
	}

	/**
	 * BitmapをUriに変換
	 * 
	 * @param bitmap
	 * @param conReslv
	 * @return
	 */
	public static Uri bitmapURiChange(Bitmap bitmap, Context con) {
		ContentResolver cr = con.getContentResolver();
		Uri resizeUri = null;
		final int IMGQUALITY = 50;
		try {
			ContentValues values = new ContentValues();
			values.put(MediaColumns.MIME_TYPE, "image/jpeg");
			values.put(ImageColumns.DATE_TAKEN, System.currentTimeMillis());
			resizeUri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			BufferedOutputStream bs = new BufferedOutputStream(cr.openOutputStream(resizeUri));
			try {
				bitmap.compress(Bitmap.CompressFormat.JPEG, IMGQUALITY, bs);
				bs.flush();
			} finally {
				bs.close();
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return resizeUri;
	}
	
	/**
	 * BitmapをUriに変換
	 * @param bitmap
	 * @param conReslv
	 * @return
	 */
	public static Uri bitmapURiChange(Bitmap bitmap, ContentResolver conReslv){
		Uri resizeUri = null;
		final int IMGQUALITY = 50;
		try {
			ContentValues values = new ContentValues();
			values.put(MediaColumns.MIME_TYPE, "image/jpeg");
			values.put(ImageColumns.DATE_TAKEN, System.currentTimeMillis()); 
			resizeUri = conReslv.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			BufferedOutputStream bs =
					new BufferedOutputStream(conReslv.openOutputStream(resizeUri));
			try {
				bitmap.compress(Bitmap.CompressFormat.JPEG, IMGQUALITY, bs);
				bs.flush();
			} finally {
				bs.close();
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return resizeUri;
	}
	
	/**
	 * URIをファイルパスで出力
	 * @param uri
	 * @return
	 */
	public static String uri2FilePath(Uri uri, Context con) {
		
		String fileName = "";
		
		Cursor c = con.getContentResolver().query(uri, null, null, null, null);
		c.moveToFirst();
		fileName = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
		return fileName;
	}
	
	/**
	 * ファイルパスから拡張子を返す
	 * @param filePath
	 * @return format 画像ファイルの拡張子
	 */
	public static String checkExtension (String filePath) {
		
		String exte = "";
		
		String str = filePath.substring(filePath.length()-3);
			
		if ("jpg".equals(str) || "JPG".equals(str)) {
			exte = "jpg";
		} else if ("gif".equals(str) || "GIF".equals(str)) {
			exte = "gif";
		} else if ("png".equals(str) || "PNG".equals(str)) {
			exte = "png";
		} else if ("bmp".equals(str) || "bmp".equals(str)) {
			exte = "bmp";
		}
		
		return exte;
	}

	/**
	 * 画像角丸加工処理
	 * 
	 * @param bitmap
	 * @param i
	 * @return
	 */
	public static Bitmap roundCornerPic(Bitmap bitmap, int i) {

		if (bitmap == null) {
			return null;
		}

		Bitmap newImage = null;
		Bitmap reBitmap = null;
		// リサイズ後サイズ
		int MAXWIDTH = 0;
		int MAXHEIGHT = 0;
		// 角丸直径計算用
		final float ROUND = 10f;

		// 画像サイズ取得
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		if (i > 0) {
			// 正方形切り抜きする
			int cutSize = Math.min(width, height);
			reBitmap = Bitmap.createBitmap(cutSize, cutSize, Bitmap.Config.ARGB_8888);
			Canvas canvas1 = new Canvas(reBitmap);
			canvas1.drawBitmap(bitmap, 0, 0, null);
			width = cutSize;
			height = cutSize;
			MAXWIDTH = i;
			MAXHEIGHT = i;
		} else {
			// 正方形切り抜きしない
			reBitmap = bitmap;
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			MAXWIDTH = width;
			MAXHEIGHT = height;
		}
		// 角丸加工
		// 切り取り領域となるbitmap生成
		Bitmap clipArea = Bitmap.createBitmap(MAXWIDTH, MAXHEIGHT, Bitmap.Config.ARGB_8888);

		// 角丸矩形を描写
		Canvas c = new Canvas(clipArea);
		c.drawRoundRect(new RectF(0, 0, MAXWIDTH, MAXHEIGHT), MAXWIDTH / ROUND, MAXWIDTH / ROUND, new Paint(
				Paint.ANTI_ALIAS_FLAG));

		// 角丸画像となるbitmap生成
		newImage = Bitmap.createBitmap(MAXWIDTH, MAXHEIGHT, Bitmap.Config.ARGB_8888);

		// 切り取り領域を描写
		Canvas canvas2 = new Canvas(newImage);
		Paint paint = new Paint();
		canvas2.drawBitmap(clipArea, 0, 0, paint);

		// 切り取り領域内にオリジナルの画像を描写
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas2.drawBitmap(reBitmap, new Rect(0, 0, width, height), new Rect(0, 0, MAXWIDTH, MAXHEIGHT), paint);

		return newImage;
	}

}
