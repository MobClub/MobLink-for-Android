package com.mob.moblink.demo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;

public class QRcodeUtils {

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	public static Bitmap encodeAsBitmap(String contents, int img_width, int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		QRCodeWriter rqCode = new QRCodeWriter();
		HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		BitMatrix matrix = rqCode.encode(contents, BarcodeFormat.QR_CODE, img_width, img_height, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				} else {
					pixels[y * width + x] = WHITE;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	public static Bitmap addLogo(Bitmap src, Bitmap logo) {
		if (src == null) {
			return null;
		}

		if (logo == null) {
			return src;
		}

		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();

		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}

		if (logoWidth == 0 || logoHeight == 0) {
			return src;
		}

		//logo大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
			canvas.drawBitmap(src, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
		}
		return bitmap;
	}

	public static Bitmap getCircleAvatar(Context context, Bitmap avatar) {
		Bitmap bitmap = Bitmap.createBitmap(avatar.getWidth(), avatar.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setColor(Color.BLACK);
		canvas.drawCircle(avatar.getWidth() / 2, avatar.getHeight() / 2,
				avatar.getWidth() / 2 < avatar.getHeight() / 2 ? avatar.getWidth() / 2 : avatar.getHeight() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(avatar, 0, 0, paint);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);

		float strokeWidth =
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
						context.getResources().getDisplayMetrics());
		paint.setStrokeWidth(strokeWidth);
		canvas.drawCircle(avatar.getWidth() / 2, avatar.getHeight() / 2,
				avatar.getWidth() / 2 < avatar.getHeight() / 2 ? avatar.getWidth() / 2 : avatar.getHeight() / 2
						- strokeWidth / 2, paint);
		return bitmap;
	}

}
