package com.cdsautomatico.apparkame2.utils;

import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by alangvara on 07/12/17.
 */

public class FormErrors {

    HashMap<String, EditText> inputs = new HashMap<>();

    public void setInput(String key, EditText editText){
        inputs.put(key, editText);
    }

    public void renderErrors(JSONObject json) throws JSONException{
        Iterator<?> keys = json.keys();

        while( keys.hasNext() ) {
            String key = (String) keys.next();

            if(inputs.containsKey(key)) {
                JSONArray errors = json.getJSONArray(key);

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < errors.length(); i++) {
                    stringBuilder.append("* " + errors.getString(i) + "\n");
                }
                inputs.get(key).setError(stringBuilder.toString());
            }
        }
    }

}
