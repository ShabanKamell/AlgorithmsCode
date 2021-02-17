package algs.shared.util

import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.net.Socket
import java.net.URL
import java.util.*
import java.util.regex.Pattern

/*************************************************************************
 * Compilation:  javac In.java
 * Execution:    java In   (basic test --- see source for required files)
 *
 * Reads in data of various types from standard input, files, and URLs.
 *
 */
/**
 * *Input*. This class provides methods for reading strings and numbers from standard input, file input, URLs, and sockets.
 *
 *
 * The Locale used is: language = English, country = US. This is consistent with the formatting conventions with Java floating-point
 * literals, command-line arguments (via [Double.parseDouble]) and standard output.
 *
 *
 * For additional documentation, see [Section 3.1](http://introcs.cs.princeton.edu/31datatype) of *Introduction to
 * Programming in Java: An Interdisciplinary Approach* by Robert Sedgewick and Kevin Wayne.
 *
 *
 * Like [Scanner], reading a token also consumes preceding Java whitespace, reading a full line consumes the following
 * end-of-line delimeter, while reading a character consumes nothing extra.
 *
 *
 * Whitespace is defined in [Character.isWhitespace]. Newlines consist of \n, \r, \r\n, and Unicode hex code points
 * 0x2028, 0x2029, 0x0085; see <tt>[
 * Scanner.java](http://www.docjar.com/html/api/java/util/Scanner.java.html)</tt> (NB: Java 6u23 and earlier uses only \r, \r, \r\n).
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class In {
    private var scanner: Scanner? = null
    /*** end: section (1 of 2) of code duplicated from In to StdIn  */
    /**
     * Create an input stream from standard input.
     */
    constructor() {
        scanner = Scanner(BufferedInputStream(System.`in`), charsetName)
        scanner!!.useLocale(usLocale)
    }

    /**
     * Create an input stream from a socket.
     */
    constructor(socket: Socket) {
        try {
            val `is` = socket.getInputStream()
            scanner = Scanner(BufferedInputStream(`is`), charsetName)
            scanner!!.useLocale(usLocale)
        } catch (ioe: IOException) {
            System.err.println("Could not open $socket")
        }
    }

    /**
     * Create an input stream from a URL.
     */
    constructor(url: URL) {
        try {
            val site = url.openConnection()
            val `is` = site.getInputStream()
            scanner = Scanner(BufferedInputStream(`is`), charsetName)
            scanner!!.useLocale(usLocale)
        } catch (ioe: IOException) {
            System.err.println("Could not open $url")
        }
    }

    /**
     * Create an input stream from a file.
     */
    constructor(file: File) {
        try {
            scanner = Scanner(file, charsetName)
            scanner!!.useLocale(usLocale)
        } catch (ioe: IOException) {
            System.err.println("Could not open $file")
        }
    }

    /**
     * Create an input stream from a filename or web page name.
     */
    constructor(s: String) {
        try {
            // first try to read file from local file system
            val file = File(s)
            if (file.exists()) {
                scanner = Scanner(file, charsetName)
                scanner!!.useLocale(usLocale)
                return
            }

            // next try for files included in jar
            var url = javaClass.getResource(s)

            // or URL from web
            if (url == null) {
                url = URL(s)
            }
            val site = url.openConnection()
            val `is` = site.getInputStream()
            scanner = Scanner(BufferedInputStream(`is`), charsetName)
            scanner!!.useLocale(usLocale)
        } catch (ioe: IOException) {
            System.err.println("Could not open $s")
        }
    }

    /**
     * Create an input stream from a given Scanner source; use with <tt>new Scanner(String)</tt> to read from a string.
     *
     *
     * Note that this does not create a defensive copy, so the scanner will be mutated as you read on.
     */
    constructor(scanner: Scanner?) {
        this.scanner = scanner
    }

    /**
     * Does the input stream exist?
     */
    fun exists(): Boolean {
        return scanner != null
    }
    /***
     * begin: section (2 of 2) of code duplicated from In to StdIn, with all methods changed from "public" to "public static"
     */
    /**
     * Is the input empty (except possibly for whitespace)? Use this to know whether the next call to [.readString],
     * [.readDouble], etc will succeed.
     */
    val isEmpty: Boolean
        get() = !scanner!!.hasNext()

    /**
     * Does the input have a next line? Use this to know whether the next call to [.readLine] will succeed.
     *
     *
     * Functionally equivalent to [.hasNextChar].
     */
    fun hasNextLine(): Boolean {
        return scanner!!.hasNextLine()
    }

    /**
     * Is the input empty (including whitespace)? Use this to know whether the next call to [.readChar] will succeed.
     *
     *
     * Functionally equivalent to [.hasNextLine].
     */
    fun hasNextChar(): Boolean {
        scanner!!.useDelimiter(EMPTY_PATTERN)
        val result = scanner!!.hasNext()
        scanner!!.useDelimiter(WHITESPACE_PATTERN)
        return result
    }

    /**
     * Read and return the next line.
     */
    fun readLine(): String? {
        val line: String?
        line = try {
            scanner!!.nextLine()
        } catch (e: Exception) {
            null
        }
        return line
    }

    /**
     * Read and return the next character.
     */
    fun readChar(): Char {
        scanner!!.useDelimiter(EMPTY_PATTERN)
        val ch = scanner!!.next()
        assert(ch.length == 1) { "Internal (Std)In.readChar() error!" + " Please contact the authors." }
        scanner!!.useDelimiter(WHITESPACE_PATTERN)
        return ch[0]
    }

    /**
     * Read and return the remainder of the input as a string.
     */
    fun readAll(): String {
        if (!scanner!!.hasNextLine()) return ""
        val result = scanner!!.useDelimiter(EVERYTHING_PATTERN).next()
        // not that important to reset delimeter, since now scanner is empty
        scanner!!.useDelimiter(WHITESPACE_PATTERN) // but let's do it anyway
        return result
    }

    /**
     * Read and return the next string.
     */
    fun readString(): String {
        return scanner!!.next()
    }

    /**
     * Read and return the next int.
     */
    fun readInt(): Int {
        return scanner!!.nextInt()
    }

    /**
     * Read and return the next double.
     */
    fun readDouble(): Double {
        return scanner!!.nextDouble()
    }

    /**
     * Read and return the next float.
     */
    fun readFloat(): Float {
        return scanner!!.nextFloat()
    }

    /**
     * Read and return the next long.
     */
    fun readLong(): Long {
        return scanner!!.nextLong()
    }

    /**
     * Read and return the next short.
     */
    fun readShort(): Short {
        return scanner!!.nextShort()
    }

    /**
     * Read and return the next byte.
     */
    fun readByte(): Byte {
        return scanner!!.nextByte()
    }

    /**
     * Read and return the next boolean, allowing case-insensitive "true" or "1" for true, and "false" or "0" for false.
     */
    fun readBoolean(): Boolean {
        val s = readString()
        if (s.equals("true", ignoreCase = true)) return true
        if (s.equals("false", ignoreCase = true)) return false
        if (s == "1") return true
        if (s == "0") return false
        throw InputMismatchException()
    }

    /**
     * Read all strings until the end of input is reached, and return them.
     */
    fun readAllStrings(): Array<String?> {
        // we could use readAll.trim().split(), but that's not consistent
        // since trim() uses characters 0x00..0x20 as whitespace
        val tokens = WHITESPACE_PATTERN.split(readAll())
        if (tokens.size == 0 || tokens[0]!!.length > 0) return tokens
        val decapitokens = arrayOfNulls<String>(tokens.size - 1)
        for (i in 0 until tokens.size - 1) decapitokens[i] = tokens[i + 1]
        return decapitokens
    }

    /**
     * Read all ints until the end of input is reached, and return them.
     */
    fun readAllInts(): IntArray {
        val fields = readAllStrings()
        val vals = IntArray(fields.size)
        for (i in fields.indices) vals[i] = fields[i]!!.toInt()
        return vals
    }

    /**
     * Read all doubles until the end of input is reached, and return them.
     */
    fun readAllDoubles(): DoubleArray {
        val fields = readAllStrings()
        val vals = DoubleArray(fields.size)
        for (i in fields.indices) vals[i] = fields[i]!!.toDouble()
        return vals
    }
    /*** end: section (2 of 2) of code duplicated from In to StdIn  */
    /**
     * Close the input stream.
     */
    fun close() {
        scanner!!.close()
    }

    companion object {
        /*** begin: section (1 of 2) of code duplicated from In to StdIn  */ // assume Unicode UTF-8 encoding
        private const val charsetName = "UTF-8"

        // assume language = English, country = US for consistency with System.out.
        private val usLocale = Locale("en", "US")

        // the default token separator; we maintain the invariant that this value
        // is held by the scanner's delimiter between calls
        private val WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+")

        // makes whitespace characters significant
        private val EMPTY_PATTERN = Pattern.compile("")

        // used to read the entire input. source:
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        private val EVERYTHING_PATTERN = Pattern.compile("\\A")

        /**
         * Reads all ints from a file
         *
         */
        @Deprecated("Clearer to use <tt>new In(filename)</tt>.{@link #readAllInts()}")
        fun readInts(filename: String): IntArray {
            return In(filename).readAllInts()
        }

        /**
         * Reads all doubles from a file
         *
         */
        @Deprecated("Clearer to use <tt>new In(filename)</tt>.{@link #readAllDoubles()}")
        fun readDoubles(filename: String): DoubleArray {
            return In(filename).readAllDoubles()
        }

        /**
         * Reads all strings from a file
         *
         */
        @Deprecated("Clearer to use <tt>new In(filename)</tt>.{@link #readAllStrings()}")
        fun readStrings(filename: String): Array<String?> {
            return In(filename).readAllStrings()
        }

        /**
         * Test client.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            var `in`: In
            val urlName = "http://introcs.cs.princeton.edu/stdlib/InTest.txt"

            // read from a URL
            println("readAll() from URL $urlName")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In(urlName)
                println(`in`.readAll())
            } catch (e: Exception) {
                println(e)
            }
            println()

            // read one line at a time from URL
            println("readLine() from URL $urlName")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In(urlName)
                while (!`in`.isEmpty) {
                    val s = `in`.readLine()
                    println(s)
                }
            } catch (e: Exception) {
                println(e)
            }
            println()

            // read one string at a time from URL
            println("readString() from URL $urlName")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In(urlName)
                while (!`in`.isEmpty) {
                    val s = `in`.readString()
                    println(s)
                }
            } catch (e: Exception) {
                println(e)
            }
            println()

            // read one line at a time from file in current directory
            println("readLine() from current directory")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In("./InTest.txt")
                while (!`in`.isEmpty) {
                    val s = `in`.readLine()
                    println(s)
                }
            } catch (e: Exception) {
                println(e)
            }
            println()

            // read one line at a time from file using relative path
            println("readLine() from relative path")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In("../stdlib/InTest.txt")
                while (!`in`.isEmpty) {
                    val s = `in`.readLine()
                    println(s)
                }
            } catch (e: Exception) {
                println(e)
            }
            println()

            // read one char at a time
            println("readChar() from file")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In("InTest.txt")
                while (!`in`.isEmpty) {
                    val c = `in`.readChar()
                    print(c)
                }
            } catch (e: Exception) {
                println(e)
            }
            println()
            println()

            // read one line at a time from absolute OS X / Linux path
            println("readLine() from absolute OS X / Linux path")
            println("---------------------------------------------------------------------------")
            `in` = In("/n/fs/introcs/www/java/stdlib/InTest.txt")
            try {
                while (!`in`.isEmpty) {
                    val s = `in`.readLine()
                    println(s)
                }
            } catch (e: Exception) {
                println(e)
            }
            println()

            // read one line at a time from absolute Windows path
            println("readLine() from absolute Windows path")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In("G:\\www\\introcs\\stdlib\\InTest.txt")
                while (!`in`.isEmpty) {
                    val s = `in`.readLine()
                    println(s)
                }
                println()
            } catch (e: Exception) {
                println(e)
            }
            println()
        }
    }
}