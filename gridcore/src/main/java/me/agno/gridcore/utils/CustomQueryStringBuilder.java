package me.agno.gridcore.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class CustomQueryStringBuilder {

    private final LinkedHashMap<String, List<String>> _query;

    public CustomQueryStringBuilder(LinkedHashMap<String, List<String>> query) {
        _query = query;
    }

    public String GetQueryStringWithParameter(String parameterName, String parameterValue) {
        if (parameterName == null || parameterName.isEmpty())
            throw new IllegalArgumentException("parameterName");

        if (_query.containsKey(parameterName))
            _query.replace(parameterName, Collections.singletonList(parameterValue));
        else
            _query.put(parameterName, Collections.singletonList(parameterValue));

        return toString();
    }

    public @Override String toString() {
        return GetQueryStringExcept(List.of());
    }

    public String GetQueryStringExcept(List<String> parameterNames) {
        var result = new StringBuilder();
        for (String key : _query.keySet()) {
            if (key == null || key.isEmpty() || parameterNames.contains(key))
                continue;
            var values = _query.get(key);
            if (values != null && !values.isEmpty()) {
                if (result.isEmpty())
                    result.append("?");
                for (String value : values) {
                    // added to support multiple filters
                    String[] vals = value.split(",");
                    for (String val : vals) {
                        result.append(key).append("=").append(URLEncoder.encode(val, StandardCharsets.UTF_8)).append("&");
                    }
                }
            }
        }
        String resultString = result.toString();
        return resultString.endsWith("&") ? resultString.substring(0, resultString.length() - 1) : resultString;
    }
}
