package uk.co.thinkofdeath.vanillacord.helper;

import java.io.PrintStream;
import java.io.PrintWriter;

public class QuietException extends RuntimeException {
    private final Throwable e;

    QuietException(String text) {
        super(text);
        this.e = null;
    }

    static QuietException notify(String text) {
        QuietException e = new QuietException(text);
        System.out.println((text == null) ? "VanillaCord has disconnected a player because of an unspecified error." : "VanillaCord has disconnected a player with the following error message:");
        return e;
    }

    static QuietException show(String text) {
        QuietException e = notify(text);
        System.out.println('\t' + text);
        return e;
    }

    QuietException(Throwable e) {
        this.e = e;
    }

    public void printStackTrace(PrintStream s) {}

    public void printStackTrace(PrintWriter s) {}

    public String toString() {
        if (this.e != null)
            return this.e.toString();
        if (getMessage() != null)
            return getMessage();
        return super.toString();
    }
}
