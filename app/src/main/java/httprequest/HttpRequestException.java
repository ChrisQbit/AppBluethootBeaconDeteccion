package httprequest;

import android.util.Log;

/**
 * Created by alangvara on 27/06/17.
 */

public class HttpRequestException extends Exception{

    private static final String TAG = "HttpRequestException";

    private int httpStatusCode = 0;
    private String httpResponseText;

    public HttpRequestException(String message, Throwable cause){
        super(message, cause);
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getHttpResponseText() {
        return httpResponseText;
    }

    public void setHttpResponseText(String httpResponseText) {
        this.httpResponseText = httpResponseText;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        if(httpResponseText != null) {
            Log.e(TAG, httpResponseText);
        }
    }
}
