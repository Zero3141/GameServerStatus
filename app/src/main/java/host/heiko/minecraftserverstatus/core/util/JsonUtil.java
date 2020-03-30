package host.heiko.minecraftserverstatus.core.util;

import org.json.JSONArray;
import org.json.JSONException;

class JsonUtil {

    /**
     * Parses an json array of strings into a string array.
     *
     * @param jsonArray The json array.
     * @return String array
     * @throws JSONException Thrown when element at index does not exist.
     */
    static String[] parseArray(JSONArray jsonArray) throws JSONException {
        String[] strings = new String[jsonArray.length()];
        for(int i = 0; i < strings.length; i++) {
            strings[i] = jsonArray.get(i).toString();
        }
        return strings;
    }


}
