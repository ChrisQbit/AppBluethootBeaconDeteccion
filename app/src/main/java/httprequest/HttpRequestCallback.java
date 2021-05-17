package httprequest;

import org.json.JSONObject;

/**
 * Created by alangvara on 27/06/17.
 */

public interface HttpRequestCallback {

    void onSuccess(JSONObject json);

    void onError(HttpRequestException e);
}
