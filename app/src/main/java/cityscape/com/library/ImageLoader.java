package cityscape.com.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Chirag on 29-03-2015.
 */
public class ImageLoader extends AsyncTask<Object,Void,Bitmap> {

    private ImageView imv;
    private String path;
    private Bitmap bitmap;

    public ImageLoader(ImageView imv) {
        this.imv = imv;
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0].toString()).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    protected void onPostExecute(Bitmap image) {
        if(image != null){
            imv.setImageBitmap(image);
        }
    }

   /* private void scaleImage()
    {
        // Get the ImageView and its bitmap
        Drawable drawing = imv.getDrawable();
        if (drawing == null) {
            return; // Checking for null & return, as suggested in comments
        }
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //int bounding = dpToPx(250);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        imv.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imv.getLayoutParams();
        params.width = width;
        params.height = height;
        imv.setLayoutParams(params);

        Log.i("Test", "done");
    }

    //private int dpToPx(int dp)
    //{
        //float density = getApplicationContext().getResources().getDisplayMetrics().density;
      //  return Math.round((float)dp * density);
    //} */
}
