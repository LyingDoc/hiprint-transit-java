//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tk.lyin.hiprint.utils.io;



import org.apache.commons.io.Charsets;
import org.apache.commons.io.output.StringBuilderWriter;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

public class IOUtils {

    public static String toString(InputStream input) throws IOException {
        return toString(input, Charset.defaultCharset());
    }

    public static String toString(InputStream input, Charset encoding) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        copy((InputStream) input, (Writer) sw, (Charset) encoding);
        return sw.toString();
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        return toString(input, Charsets.toCharset(encoding));
    }

    public static String toString(Reader input) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        copy((Reader) input, (Writer) sw);
        return sw.toString();
    }

    public static String toString(URI uri) throws IOException {
        return toString(uri, Charset.defaultCharset());
    }

    public static String toString(URI uri, Charset encoding) throws IOException {
        return toString(uri.toURL(), Charsets.toCharset(encoding));
    }

    public static String toString(URI uri, String encoding) throws IOException {
        return toString(uri, Charsets.toCharset(encoding));
    }

    public static String toString(URL url) throws IOException {
        return toString(url, Charset.defaultCharset());
    }

    public static String toString(URL url, Charset encoding) throws IOException {
        InputStream inputStream = url.openStream();

        String var3;
        try {
            var3 = toString(inputStream, encoding);
        } finally {
            inputStream.close();
        }

        return var3;
    }

    public static String toString(URL url, String encoding) throws IOException {
        return toString(url, Charsets.toCharset(encoding));
    }


    public static String toString(byte[] input, String encoding) throws IOException {
        return new String(input, Charsets.toCharset(encoding));
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copyLarge(input, output, new byte[4096]);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0L;

        int n;
        for (boolean var5 = false; -1 != (n = input.read(buffer)); count += (long) n) {
            output.write(buffer, 0, n);
        }

        return count;
    }


    public static void copy(InputStream input, Writer output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(InputStream input, Writer output, Charset encoding) throws IOException {
        InputStreamReader in = new InputStreamReader(input, org.apache.commons.io.Charsets.toCharset(encoding));
        copy((Reader) in, (Writer) output);
    }

    public static void copy(InputStream input, Writer output, String encoding) throws IOException {
        copy(input, output, org.apache.commons.io.Charsets.toCharset(encoding));
    }

    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int) count;
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        return copyLarge(input, output, new char[4096]);
    }

    public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
        long count = 0L;

        int n;
        for (boolean var5 = false; -1 != (n = input.read(buffer)); count += (long) n) {
            output.write(buffer, 0, n);
        }

        return count;
    }

    public static void copy(Reader input, OutputStream output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(Reader input, OutputStream output, Charset encoding) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output, org.apache.commons.io.Charsets.toCharset(encoding));
        copy((Reader) input, (Writer) out);
        out.flush();
    }

    public static void copy(Reader input, OutputStream output, String encoding) throws IOException {
        copy(input, output, org.apache.commons.io.Charsets.toCharset(encoding));
    }


}
