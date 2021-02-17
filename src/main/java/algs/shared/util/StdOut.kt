/******************************************************************************
 * Compilation:  javac StdOut.java
 * Execution:    java StdOut
 * Dependencies: none
 *
 * Writes data of various types to standard output.
 *
 */
package algs.shared.util

import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.io.UnsupportedEncodingException
import java.util.*

/**
 * This class provides methods for printing strings and numbers to standard output.
 *
 *
 * **Getting started.**
 * To use this class, you must have `StdOut.class` in your
 * Java classpath. If you used our autoinstaller, you should be all set.
 * Otherwise, either download
 * [stdlib.jar](https://introcs.cs.princeton.edu/java/code/stdlib.jar)
 * and add to your Java classpath or download
 * [StdOut.java](https://introcs.cs.princeton.edu/java/stdlib/StdOut.java)
 * and put a copy in your working directory.
 *
 *
 * Here is an example program that uses `StdOut`:
 * <pre>
 * public class TestStdOut {
 * public static void main(String[] args) {
 * int a = 17;
 * int b = 23;
 * int sum = a + b;
 * StdOut.println("Hello, World");
 * StdOut.printf("%d + %d = %d\n", a, b, sum);
 * }
 * }
</pre> *
 *
 *
 * **Differences with System.out.**
 * The behavior of `StdOut` is similar to that of [System.out],
 * but there are a few technical differences:
 *
 *  *  `StdOut` coerces the character-set encoding to UTF-8,
 * which is a standard character encoding for Unicode.
 *  *  `StdOut` coerces the locale to [Locale.US],
 * for consistency with [StdIn], [Double.parseDouble],
 * and floating-point literals.
 *  *  `StdOut` *flushes* standard output after each call to
 * `print()` so that text will appear immediately in the terminal.
 *
 *
 *
 * **Reference.**
 * For additional documentation,
 * see [Section 1.5](https://introcs.cs.princeton.edu/15inout) of
 * *Computer Science: An Interdisciplinary Approach*
 * by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
object StdOut {
    // force Unicode UTF-8 encoding; otherwise it's system dependent
    private const val CHARSET_NAME = "UTF-8"

    // assume language = English, country = US for consistency with StdIn
    private val LOCALE = Locale.US

    // send output here
    private var out: PrintWriter? = null

    /**
     * Terminates the current line by printing the line-separator string.
     */
    fun println() {
        out!!.println()
    }

    /**
     * Prints an object to this output stream and then terminates the line.
     *
     * @param x the object to print
     */
    fun println(x: Any?) {
        out!!.println(x)
    }

    /**
     * Prints a boolean to standard output and then terminates the line.
     *
     * @param x the boolean to print
     */
    fun println(x: Boolean) {
        out!!.println(x)
    }

    /**
     * Prints a character to standard output and then terminates the line.
     *
     * @param x the character to print
     */
    fun println(x: Char) {
        out!!.println(x)
    }

    /**
     * Prints a double to standard output and then terminates the line.
     *
     * @param x the double to print
     */
    fun println(x: Double) {
        out!!.println(x)
    }

    /**
     * Prints an integer to standard output and then terminates the line.
     *
     * @param x the integer to print
     */
    fun println(x: Float) {
        out!!.println(x)
    }

    /**
     * Prints an integer to standard output and then terminates the line.
     *
     * @param x the integer to print
     */
    fun println(x: Int) {
        out!!.println(x)
    }

    /**
     * Prints a long to standard output and then terminates the line.
     *
     * @param x the long to print
     */
    fun println(x: Long) {
        out!!.println(x)
    }

    /**
     * Prints a short integer to standard output and then terminates the line.
     *
     * @param x the short to print
     */
    fun println(x: Short) {
        out!!.println(x.toInt())
    }

    /**
     * Prints a byte to standard output and then terminates the line.
     *
     *
     * To write binary data, see [BinaryStdOut].
     *
     * @param x the byte to print
     */
    fun println(x: Byte) {
        out!!.println(x.toInt())
    }

    /**
     * Flushes standard output.
     */
    fun print() {
        out!!.flush()
    }

    /**
     * Prints an object to standard output and flushes standard output.
     *
     * @param x the object to print
     */
    fun print(x: Any?) {
        out!!.print(x)
        out!!.flush()
    }

    /**
     * Prints a boolean to standard output and flushes standard output.
     *
     * @param x the boolean to print
     */
    fun print(x: Boolean) {
        out!!.print(x)
        out!!.flush()
    }

    /**
     * Prints a character to standard output and flushes standard output.
     *
     * @param x the character to print
     */
    fun print(x: Char) {
        out!!.print(x)
        out!!.flush()
    }

    /**
     * Prints a double to standard output and flushes standard output.
     *
     * @param x the double to print
     */
    fun print(x: Double) {
        out!!.print(x)
        out!!.flush()
    }

    /**
     * Prints a float to standard output and flushes standard output.
     *
     * @param x the float to print
     */
    fun print(x: Float) {
        out!!.print(x)
        out!!.flush()
    }

    /**
     * Prints an integer to standard output and flushes standard output.
     *
     * @param x the integer to print
     */
    fun print(x: Int) {
        out!!.print(x)
        out!!.flush()
    }

    /**
     * Prints a long integer to standard output and flushes standard output.
     *
     * @param x the long integer to print
     */
    fun print(x: Long) {
        out!!.print(x)
        out!!.flush()
    }

    /**
     * Prints a short integer to standard output and flushes standard output.
     *
     * @param x the short integer to print
     */
    fun print(x: Short) {
        out!!.print(x.toInt())
        out!!.flush()
    }

    /**
     * Prints a byte to standard output and flushes standard output.
     *
     * @param x the byte to print
     */
    fun print(x: Byte) {
        out!!.print(x.toInt())
        out!!.flush()
    }

    /**
     * Prints a formatted string to standard output, using the specified format
     * string and arguments, and then flushes standard output.
     *
     *
     * @param format the [format string](http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax)
     * @param args   the arguments accompanying the format string
     */
    fun printf(format: String?, vararg args: Any?) {
        out!!.printf(LOCALE, format, *args)
        out!!.flush()
    }

    /**
     * Prints a formatted string to standard output, using the locale and
     * the specified format string and arguments; then flushes standard output.
     *
     * @param locale the locale
     * @param format the [format string](http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax)
     * @param args   the arguments accompanying the format string
     */
    fun printf(locale: Locale?, format: String?, vararg args: Any?) {
        out!!.printf(locale, format, *args)
        out!!.flush()
    }

    /**
     * Unit tests some of the methods in `StdOut`.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {

        // write to stdout
        println("Test")
        println(17)
        println(true)
        printf("%.6f\n", 1.0 / 7.0)
    }

    // this is called before invoking any methods
    init {
        try {
            out = PrintWriter(OutputStreamWriter(System.out, CHARSET_NAME), true)
        } catch (e: UnsupportedEncodingException) {
            println(e)
        }
    }
}