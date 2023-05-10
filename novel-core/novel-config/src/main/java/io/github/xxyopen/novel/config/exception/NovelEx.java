package io.github.xxyopen.novel.config.exception;

import java.net.BindException;

public class NovelEx extends RuntimeException {
    private static String message = "未知错误";

    public NovelEx(String message) {
        super(message);
    }

    public static void cast() throws BindException {
        throw new BindException(message);
    }

    public static void cast(String message) {
        throw new NovelEx(message);
    }
}
