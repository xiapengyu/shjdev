package com.yunjian.modules.automat.util;

import java.io.*;

public class StreamUtil {

    private StreamUtil() {
    }

    public static void io(Reader in, Writer out) throws IOException {
        io(in, out, -1);
    }

    public static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = 4096;
        }

        char[] buffer = new char[bufferSize];

        int amount;
        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }
}
