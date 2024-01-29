package me.agno.gridjavacore.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A class for building custom query strings.
 */
public class CustomQueryStringBuilder {

    private final LinkedHashMap<String, List<String>> query;

    /**
     * Constructs a CustomQueryStringBuilder object with the given query.
     *
     * @param query a LinkedHashMap containing the query parameters and their values
     */
    public CustomQueryStringBuilder(LinkedHashMap<String, List<String>> query) {
        this.query = query;
    }

    /**
     * Returns the query string with the specified parameter added.
     *
     * @param parameterName the name of the parameter to add
     * @param parameterValue the value of the parameter to add
     * @return the query string with the specified parameter added
     * @throws IllegalArgumentException if parameterName is null or empty
     */
    public String getQueryStringWithParameter(String parameterName, String parameterValue) {
        if (parameterName == null || parameterName.isEmpty())
            throw new IllegalArgumentException("parameterName");

        if (this.query.containsKey(parameterName))
            this.query.replace(parameterName, Collections.singletonList(parameterValue));
        else
            this.query.put(parameterName, Collections.singletonList(parameterValue));

        return toString();
    }

    /**
     * Returns the query string representation of the CustomQueryStringBuilder object, excluding specified parameters.
     *
     * @return the query string representation of the CustomQueryStringBuilder object, excluding specified parameters
     */
    public @Override String toString() {
        return getQueryStringExcept(List.of());
    }

    /**
     * Returns the query string representation of the CustomQueryStringBuilder object,
     * excluding the specified parameters.
     *
     * @param parameterNames a list of parameter names to exclude from the query string
     * @return the query string representation of the CustomQueryStringBuilder object,
     * excluding the specified parameters
     */
    public String getQueryStringExcept(List<String> parameterNames) {
        var result = new StringBuilder();
        for (String key : this.query.keySet()) {
            if (key == null || key.isEmpty() || parameterNames.contains(key))
                continue;
            var values = this.query.get(key);
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
