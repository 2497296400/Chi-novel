package io.github.xxyopen.novel.config.wrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final Map<String, String> REPLACE_RULE = new HashMap<>();

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    static {
        REPLACE_RULE.put("<", "&lt;");
        REPLACE_RULE.put(">", "&gt;");
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues != null) {
            String[] replace = parameterValues;
            for (int i = 0; i < replace.length; i++) {
                int index = i;
                REPLACE_RULE.forEach((k, v) -> {
                    replace[index] = replace[index].replaceAll(k, v);
                });
            }
            return replace;
        }
        return new String[0];
    }
}
