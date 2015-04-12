package cityscape.com.library;

import android.graphics.Bitmap;

/**
 * Created by Krith on 01-04-2015.
 */
public class FBUserDetails {

    private String fbUserName;
    private String userId;
    private Bitmap bitmap;

    public String getFbUserName() {
        return fbUserName;
    }

    public void setFbUserName(String fbUserName) {
        this.fbUserName = fbUserName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setUserId(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

