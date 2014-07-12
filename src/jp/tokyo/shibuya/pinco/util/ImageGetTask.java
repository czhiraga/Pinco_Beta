package jp.tokyo.shibuya.pinco.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageGetTask extends AsyncTask<String, Void, Bitmap> {
	private ImageView image;
	private ProgressBar progress;
	private String tag;
	private int size = 0;
	private boolean roundFlg = false;
	private final String ROUND = "round";
	private boolean tagExist = true;

	public ImageGetTask(ImageView image, ProgressBar progress) {
		// 対象の項目を保持しておく
		this.image = image;
		this.progress = progress;
		try {
			this.tag = image.getTag().toString();
		} catch (NullPointerException e) {
			tagExist = false;
		}
	}

	public ImageGetTask() {

	}

	/**
	 * 
	 * @param _image
	 * @param _progress
	 * @param roundFlg
	 *            角を丸くするか否か
	 */
	public ImageGetTask(ImageView image, ProgressBar progress, boolean roundFlg) {
		// 対象の項目を保持しておく
		this.image = image;
		this.progress = progress;
		this.tag = image.getTag().toString();
		this.roundFlg = roundFlg;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// ここでHttp経由で画像を取得します。取得後Bitmapで返します。
		Bitmap image = null;
		synchronized (this) {
			try {

				if (roundFlg) {
					// 角丸画像がキャッシュにあればキャッシュから取得
					image = ImageCache.getImage(params[0] + ROUND);
					if (image != null) {
						return image;
					} else {
					}
				}

				// キャッシュより画像データを取得
				image = ImageCache.getImage(params[0]);
				if (image == null) {
					// キャッシュにデータが存在しない場合はwebより画像データを取得
					URL imageUrl = new URL(params[0]);
					InputStream imageIs;
					imageIs = imageUrl.openStream();

					// BitmapFactory.Options o = new Options();
					// BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(o,
					// false);
					// image = BitmapFactory.decodeStream(imageIs, null, o);
					image = BitmapFactory.decodeStream(imageIs);

					// 取得した画像データをキャッシュに保持
					ImageCache.setImage(params[0], image);
				}

				if (roundFlg) {
					Bitmap newbitmap = null;
					newbitmap = ImageUtil.roundCornerPic(image, size);
					image = newbitmap;
					// 角丸画像をキャッシュに保存
					ImageCache.setImage(params[0] + ROUND, image);
				}

				return image;

			} catch (MalformedURLException e) {
				return null;
			} catch (IOException e) {
				return null;
			} catch (IllegalArgumentException e) {
				return null;
			} catch (SecurityException e) {
				return null;

			}
		}

	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (tagExist) {
			// Tagが同じものか確認して、同じであれば画像を設定する
			// （Tagの設定をしないと別の行に画像が表示されてしまう）
			if (tag.equals(image.getTag())) {
				if (result != null) {
					// 画像の設定
					image.setImageBitmap(result);
				} else {
					// エラーの場合はNOIMAGE画像を表示
					// mImgUser.setImageResource(R.drawable.no_image);
				}
				// プログレスバーを隠し、取得した画像を表示
				progress.setVisibility(View.GONE);
				image.setVisibility(View.VISIBLE);
			}
		} else {
			if (result != null) {
				// 画像の設定
				image.setImageBitmap(result);
			} else {
				// エラーの場合はNOIMAGE画像を表示
				// mImgUser.setImageResource(R.drawable.no_image);
			}
			// プログレスバーを隠し、取得した画像を表示
			progress.setVisibility(View.GONE);
			image.setVisibility(View.VISIBLE);
		}
	}

	/** imageをClearする **/
	public void imageClear() {
		image.setImageDrawable(null);
	}

}