package es.caballero.frog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.Random;

/**
 * Created by rcaballeron on 08.02.2015.
 */
public class WimmelView extends View {
    public static final int NUMBER_DISTRACT_IMAGES = 9;
    private Random rnd;
    private long randomSeed = 1;
    private int imageCount;
    private static final int[] images = {R.drawable.distract1, R.drawable.distract2,
            R.drawable.distract3, R.drawable.distract4, R.drawable.distract5,
            R.drawable.distract6, R.drawable.distract7, R.drawable.distract8,
            R.drawable.distract9};
    private Paint paint = new Paint();

    public WimmelView(Context context) {
        super(context);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rnd = new Random(randomSeed);
        for (int image: images) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image);

            for (int i = 0; i < imageCount / images.length; i++) {
                float left = (float) (rnd.nextFloat() * (getWidth() - bitmap.getWidth()));
                float top = (float) (rnd.nextFloat()) * (getHeight() - bitmap.getHeight());
                canvas.drawBitmap(bitmap, left, top, paint);
            }
        }
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
        randomSeed = System.currentTimeMillis();
        invalidate();
    }


}
