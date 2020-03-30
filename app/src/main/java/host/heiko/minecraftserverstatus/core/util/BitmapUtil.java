package host.heiko.minecraftserverstatus.core.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

class BitmapUtil {

    /**
     * Converts a base64 encoded string to a bitmap.
     *
     * @param base64 The base64 encoded string.
     * @return Bitmap.
     */
    static Bitmap parseBase64(String base64) {
        if(base64.startsWith("data:image/png;base64,")) {
            base64 = base64.replace("data:image/png;base64,", "");
        }
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
