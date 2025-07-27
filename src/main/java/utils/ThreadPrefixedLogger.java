package utils;


import java.io.PrintStream;

public class ThreadPrefixedLogger {
    private static final ThreadLocal<String> threadPrefix = new ThreadLocal<>();

    public static void setPrefix(String prefix) {
        threadPrefix.set(prefix);
    }

    public static void clearPrefix() {
        threadPrefix.remove();
    }

    public static void init() {
        System.setOut(new PrefixPrintStream(System.out));
    }

    private static class PrefixPrintStream extends PrintStream {
        public PrefixPrintStream(PrintStream original) {
            super(original);
        }

        @Override
        public void println(String x) {
            String prefix = threadPrefix.get();
            if (prefix != null) {
                super.println("[" + prefix + "] " + x);
            } else {
                super.println(x);
            }
        }
    }
}
