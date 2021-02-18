/******************************************************************************
 * Compilation:  javac StdRandom.java
 * Execution:    java StdRandom
 * Dependencies: StdOut.java
 *
 * A library of static methods to generate pseudo-random numbers from
 * different distributions (bernoulli, uniform, gaussian, discrete,
 * and exponential). Also includes a method for shuffling an array.
 *
 *
 * %  java StdRandom 5
 * seed = 1316600602069
 * 59 16.81826  true 8.83954  0
 * 32 91.32098  true 9.11026  0
 * 35 10.11874  true 8.95396  3
 * 92 32.88401  true 8.87089  0
 * 72 92.55791  true 9.46241  0
 *
 * % java StdRandom 5
 * seed = 1316600616575
 * 96 60.17070  true 8.72821  0
 * 79 32.01607  true 8.58159  0
 * 81 59.49065  true 9.10423  1
 * 96 51.65818  true 9.02102  0
 * 99 17.55771  true 8.99762  0
 *
 * % java StdRandom 5 1316600616575
 * seed = 1316600616575
 * 96 60.17070  true 8.72821  0
 * 79 32.01607  true 8.58159  0
 * 81 59.49065  true 9.10423  1
 * 96 51.65818  true 9.02102  0
 * 99 17.55771  true 8.99762  0
 *
 *
 * Remark
 * ------
 * - Relies on randomness of nextDouble() method in java.util.Random
 * to generate pseudo-random numbers in [0, 1).
 *
 * - This library allows you to set and get the pseudo-random number seed.
 *
 * - See http://www.honeylocust.com/RngPack/ for an industrial
 * strength random number generator in Java.
 *
 */
package algs.shared.util

import java.util.*

/**
 * The `StdRandom` class provides static methods for generating
 * random number from various discrete and continuous distributions,
 * including uniform, Bernoulli, geometric, Gaussian, exponential, Pareto,
 * Poisson, and Cauchy. It also provides method for shuffling an
 * array or subarray and generating random permutations.
 *
 *
 * By convention, all intervals are half open. For example,
 * `uniform(-1.0, 1.0)` returns a random number between
 * `-1.0` (inclusive) and `1.0` (exclusive).
 * Similarly, `shuffle(a, lo, hi)` shuffles the `hi - lo`
 * elements in the array `a[]`, starting at index `lo`
 * (inclusive) and ending at index `hi` (exclusive).
 *
 *
 * For additional documentation,
 * see [Section 2.2](https://introcs.cs.princeton.edu/22library) of
 * *Computer Science: An Interdisciplinary Approach*
 * by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
object StdRandom {
    private var random // pseudo-random number generator
            : Random? = null
    private var seed // pseudo-random number generator seed
            : Long = 0

    /**
     * Sets the seed of the pseudo-random number generator.
     * This method enables you to produce the same sequence of "random"
     * number for each execution of the program.
     * Ordinarily, you should call this method at most once per program.
     *
     * @param s the seed
     */
    fun setSeed(s: Long) {
        seed = s
        random = Random(seed)
    }

    /**
     * Returns the seed of the pseudo-random number generator.
     *
     * @return the seed
     */
    fun getSeed(): Long {
        return seed
    }

    /**
     * Returns a random real number uniformly in [0, 1).
     *
     * @return a random real number uniformly in [0, 1)
     */
    fun uniform(): Double {
        return random!!.nextDouble()
    }

    /**
     * Returns a random integer uniformly in [0, n).
     *
     * @param n number of possible integers
     * @return a random integer uniformly between 0 (inclusive) and `n` (exclusive)
     * @throws IllegalArgumentException if `n <= 0`
     */
    fun uniform(n: Int): Int {
        require(n > 0) { "argument must be positive: $n" }
        return random!!.nextInt(n)
    }

    /**
     * Returns a random long integer uniformly in [0, n).
     *
     * @param n number of possible `long` integers
     * @return a random long integer uniformly between 0 (inclusive) and `n` (exclusive)
     * @throws IllegalArgumentException if `n <= 0`
     */
    fun uniform(n: Long): Long {
        require(n > 0L) { "argument must be positive: $n" }

        // https://docs.oracle.com/javase/8/docs/api/java/util/Random.html#longs-long-long-long-
        var r = random!!.nextLong()
        val m = n - 1

        // power of two
        if (n and m == 0L) {
            return r and m
        }

        // reject over-represented candidates
        var u = r ushr 1
        while (u + m - u % n.also { r = it } < 0L) {
            u = random!!.nextLong() ushr 1
        }
        return r
    }
    ///////////////////////////////////////////////////////////////////////////
    //  STATIC METHODS BELOW RELY ON JAVA.UTIL.RANDOM ONLY INDIRECTLY VIA
    //  THE STATIC METHODS ABOVE.
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Returns a random real number uniformly in [0, 1).
     *
     * @return     a random real number uniformly in [0, 1)
     */
    @Deprecated("Replaced by {@link #uniform()}.")
    fun random(): Double {
        return uniform()
    }

    /**
     * Returns a random integer uniformly in [a, b).
     *
     * @param  a the left endpoint
     * @param  b the right endpoint
     * @return a random integer uniformly in [a, b)
     * @throws IllegalArgumentException if `b <= a`
     * @throws IllegalArgumentException if `b - a >= Integer.MAX_VALUE`
     */
    fun uniform(a: Int, b: Int): Int {
        require(!(b <= a || b.toLong() - a >= Int.MAX_VALUE)) { "invalid range: [$a, $b)" }
        return a + uniform(b - a)
    }

    /**
     * Returns a random real number uniformly in [a, b).
     *
     * @param  a the left endpoint
     * @param  b the right endpoint
     * @return a random real number uniformly in [a, b)
     * @throws IllegalArgumentException unless `a < b`
     */
    fun uniform(a: Double, b: Double): Double {
        require(a < b) { "invalid range: [$a, $b)" }
        return a + uniform() * (b - a)
    }
    /**
     * Returns a random boolean from a Bernoulli distribution with success
     * probability *p*.
     *
     * @param  p the probability of returning `true`
     * @return `true` with probability `p` and
     * `false` with probability `1 - p`
     * @throws IllegalArgumentException unless `0`  `p`  `1.0`
     */
    /**
     * Returns a random boolean from a Bernoulli distribution with success
     * probability 1/2.
     *
     * @return `true` with probability 1/2 and
     * `false` with probability 1/2
     */
    @JvmOverloads
    fun bernoulli(p: Double = 0.5): Boolean {
        require(p >= 0.0 && p <= 1.0) { "probability p must be between 0.0 and 1.0: $p" }
        return uniform() < p
    }

    /**
     * Returns a random real number from a standard Gaussian distribution.
     *
     * @return a random real number from a standard Gaussian distribution
     * (mean 0 and standard deviation 1).
     */
    fun gaussian(): Double {
        // use the polar form of the Box-Muller transform
        var r: Double
        var x: Double
        var y: Double
        do {
            x = uniform(-1.0, 1.0)
            y = uniform(-1.0, 1.0)
            r = x * x + y * y
        } while (r >= 1 || r == 0.0)
        return x * Math.sqrt(-2 * Math.log(r) / r)

        // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
        // is an independent random gaussian
    }

    /**
     * Returns a random real number from a Gaussian distribution with mean
     * and standard deviation .
     *
     * @param  mu the mean
     * @param  sigma the standard deviation
     * @return a real number distributed according to the Gaussian distribution
     * with mean `mu` and standard deviation `sigma`
     */
    fun gaussian(mu: Double, sigma: Double): Double {
        return mu + sigma * gaussian()
    }

    /**
     * Returns a random integer from a geometric distribution with success
     * probability *p*.
     * The integer represents the number of independent trials
     * before the first success.
     *
     * @param  p the parameter of the geometric distribution
     * @return a random integer from a geometric distribution with success
     * probability `p`; or `Integer.MAX_VALUE` if
     * `p` is (nearly) equal to `1.0`.
     * @throws IllegalArgumentException unless `p >= 0.0` and `p <= 1.0`
     */
    fun geometric(p: Double): Int {
        require(p >= 0) { "probability p must be greater than 0: $p" }
        require(p <= 1.0) { "probability p must not be larger than 1: $p" }
        // using algorithm given by Knuth
        return Math.ceil(Math.log(uniform()) / Math.log(1.0 - p)).toInt()
    }

    /**
     * Returns a random integer from a Poisson distribution with mean .
     *
     * @param  lambda the mean of the Poisson distribution
     * @return a random integer from a Poisson distribution with mean `lambda`
     * @throws IllegalArgumentException unless `lambda > 0.0` and not infinite
     */
    fun poisson(lambda: Double): Int {
        require(lambda > 0.0) { "lambda must be positive: $lambda" }
        require(!java.lang.Double.isInfinite(lambda)) { "lambda must not be infinite: $lambda" }
        // using algorithm given by Knuth
        // see http://en.wikipedia.org/wiki/Poisson_distribution
        var k = 0
        var p = 1.0
        val expLambda = Math.exp(-lambda)
        do {
            k++
            p *= uniform()
        } while (p >= expLambda)
        return k - 1
    }
    /**
     * Returns a random real number from a Pareto distribution with
     * shape parameter .
     *
     * @param  alpha shape parameter
     * @return a random real number from a Pareto distribution with shape
     * parameter `alpha`
     * @throws IllegalArgumentException unless `alpha > 0.0`
     */
    /**
     * Returns a random real number from the standard Pareto distribution.
     *
     * @return a random real number from the standard Pareto distribution
     */
    @JvmOverloads
    fun pareto(alpha: Double = 1.0): Double {
        require(alpha > 0.0) { "alpha must be positive: $alpha" }
        return Math.pow(1 - uniform(), -1.0 / alpha) - 1.0
    }

    /**
     * Returns a random real number from the Cauchy distribution.
     *
     * @return a random real number from the Cauchy distribution.
     */
    fun cauchy(): Double {
        return Math.tan(Math.PI * (uniform() - 0.5))
    }

    /**
     * Returns a random integer from the specified discrete distribution.
     *
     * @param  probabilities the probability of occurrence of each integer
     * @return a random integer from a discrete distribution:
     * `i` with probability `probabilities[i]`
     * @throws IllegalArgumentException if `probabilities` is `null`
     * @throws IllegalArgumentException if sum of array entries is not (very nearly) equal to `1.0`
     * @throws IllegalArgumentException unless `probabilities[i] >= 0.0` for each index `i`
     */
    fun discrete(probabilities: DoubleArray?): Int {
        requireNotNull(probabilities) { "argument array is null" }
        val EPSILON = 1.0E-14
        var sum = 0.0
        for (i in probabilities.indices) {
            require(probabilities[i] >= 0.0) { "array entry " + i + " must be nonnegative: " + probabilities[i] }
            sum += probabilities[i]
        }
        require(!(sum > 1.0 + EPSILON || sum < 1.0 - EPSILON)) { "sum of array entries does not approximately equal 1.0: $sum" }

        // the for loop may not return a value when both r is (nearly) 1.0 and when the
        // cumulative sum is less than 1.0 (as a result of floating-point roundoff error)
        while (true) {
            val r = uniform()
            sum = 0.0
            for (i in probabilities.indices) {
                sum = sum + probabilities[i]
                if (sum > r) return i
            }
        }
    }

    /**
     * Returns a random integer from the specified discrete distribution.
     *
     * @param  frequencies the frequency of occurrence of each integer
     * @return a random integer from a discrete distribution:
     * `i` with probability proportional to `frequencies[i]`
     * @throws IllegalArgumentException if `frequencies` is `null`
     * @throws IllegalArgumentException if all array entries are `0`
     * @throws IllegalArgumentException if `frequencies[i]` is negative for any index `i`
     * @throws IllegalArgumentException if sum of frequencies exceeds `Integer.MAX_VALUE` (2<sup>31</sup> - 1)
     */
    fun discrete(frequencies: IntArray?): Int {
        requireNotNull(frequencies) { "argument array is null" }
        var sum: Long = 0
        for (i in frequencies.indices) {
            require(frequencies[i] >= 0) { "array entry " + i + " must be nonnegative: " + frequencies[i] }
            sum += frequencies[i]
        }
        require(sum != 0L) { "at least one array entry must be positive" }
        require(sum < Int.MAX_VALUE) { "sum of frequencies overflows an int" }

        // pick index i with probabilitity proportional to frequency
        val r = uniform(sum.toInt()).toDouble()
        sum = 0
        for (i in frequencies.indices) {
            sum += frequencies[i]
            if (sum > r) return i
        }
        assert(false)
        return -1
    }

    /**
     * Returns a random real number from an exponential distribution
     * with rate .
     *
     * @param  lambda the rate of the exponential distribution
     * @return a random real number from an exponential distribution with
     * rate `lambda`
     * @throws IllegalArgumentException unless `lambda > 0.0`
     */
    fun exp(lambda: Double): Double {
        require(lambda > 0.0) { "lambda must be positive: $lambda" }
        return -Math.log(1 - uniform()) / lambda
    }

    /**
     * Rearranges the elements of the specified array in uniformly random order.
     *
     * @param  a the array to shuffle
     * @throws IllegalArgumentException if `a` is `null`
     */
    fun shuffle(a: Array<Any?>) {
        validateNotNull(a)
        val n = a.size
        for (i in 0 until n) {
            val r = i + uniform(n - i) // between i and n-1
            val temp = a[i]
            a[i] = a[r]
            a[r] = temp
        }
    }

    /**
     * Rearranges the elements of the specified array in uniformly random order.
     *
     * @param  a the array to shuffle
     * @throws IllegalArgumentException if `a` is `null`
     */
    fun shuffle(a: DoubleArray) {
        validateNotNull(a)
        val n = a.size
        for (i in 0 until n) {
            val r = i + uniform(n - i) // between i and n-1
            val temp = a[i]
            a[i] = a[r]
            a[r] = temp
        }
    }

    /**
     * Rearranges the elements of the specified array in uniformly random order.
     *
     * @param  a the array to shuffle
     * @throws IllegalArgumentException if `a` is `null`
     */
    fun shuffle(a: IntArray) {
        validateNotNull(a)
        val n = a.size
        for (i in 0 until n) {
            val r = i + uniform(n - i) // between i and n-1
            val temp = a[i]
            a[i] = a[r]
            a[r] = temp
        }
    }

    /**
     * Rearranges the elements of the specified array in uniformly random order.
     *
     * @param  a the array to shuffle
     * @throws IllegalArgumentException if `a` is `null`
     */
    fun shuffle(a: CharArray) {
        validateNotNull(a)
        val n = a.size
        for (i in 0 until n) {
            val r = i + uniform(n - i) // between i and n-1
            val temp = a[i]
            a[i] = a[r]
            a[r] = temp
        }
    }

    /**
     * Rearranges the elements of the specified subarray in uniformly random order.
     *
     * @param  a the array to shuffle
     * @param  lo the left endpoint (inclusive)
     * @param  hi the right endpoint (exclusive)
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    fun shuffle(a: Array<Any?>, lo: Int, hi: Int) {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)
        for (i in lo until hi) {
            val r = i + uniform(hi - i) // between i and hi-1
            val temp = a[i]
            a[i] = a[r]
            a[r] = temp
        }
    }

    /**
     * Rearranges the elements of the specified subarray in uniformly random order.
     *
     * @param  a the array to shuffle
     * @param  lo the left endpoint (inclusive)
     * @param  hi the right endpoint (exclusive)
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    fun shuffle(a: DoubleArray, lo: Int, hi: Int) {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)
        for (i in lo until hi) {
            val r = i + uniform(hi - i) // between i and hi-1
            val temp = a[i]
            a[i] = a[r]
            a[r] = temp
        }
    }

    /**
     * Rearranges the elements of the specified subarray in uniformly random order.
     *
     * @param  a the array to shuffle
     * @param  lo the left endpoint (inclusive)
     * @param  hi the right endpoint (exclusive)
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    fun shuffle(a: IntArray, lo: Int, hi: Int) {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)
        for (i in lo until hi) {
            val r = i + uniform(hi - i) // between i and hi-1
            val temp = a[i]
            a[i] = a[r]
            a[r] = temp
        }
    }

    /**
     * Returns a uniformly random permutation of *n* elements.
     *
     * @param  n number of elements
     * @throws IllegalArgumentException if `n` is negative
     * @return an array of length `n` that is a uniformly random permutation
     * of `0`, `1`, ..., `n-1`
     */
    fun permutation(n: Int): IntArray {
        require(n >= 0) { "argument is negative" }
        val perm = IntArray(n)
        for (i in 0 until n) perm[i] = i
        shuffle(perm)
        return perm
    }

    /**
     * Returns a uniformly random permutation of *k* of *n* elements.
     *
     * @param  n number of elements
     * @param  k number of elements to select
     * @throws IllegalArgumentException if `n` is negative
     * @throws IllegalArgumentException unless `0 <= k <= n`
     * @return an array of length `k` that is a uniformly random permutation
     * of `k` of the elements from `0`, `1`, ..., `n-1`
     */
    fun permutation(n: Int, k: Int): IntArray {
        require(n >= 0) { "argument is negative" }
        require(!(k < 0 || k > n)) { "k must be between 0 and n" }
        val perm = IntArray(k)
        for (i in 0 until k) {
            val r = uniform(i + 1) // between 0 and i
            perm[i] = perm[r]
            perm[r] = i
        }
        for (i in k until n) {
            val r = uniform(i + 1) // between 0 and i
            if (r < k) perm[r] = i
        }
        return perm
    }

    // throw an IllegalArgumentException if x is null
    // (x can be of type Object[], double[], int[], ...)
    private fun validateNotNull(x: Any?) {
        requireNotNull(x) { "argument is null" }
    }

    // throw an exception unless 0 <= lo <= hi <= length
    private fun validateSubarrayIndices(lo: Int, hi: Int, length: Int) {
        require(!(lo < 0 || hi > length || lo > hi)) { "subarray indices out of bounds: [$lo, $hi)" }
    }

    /**
     * Unit tests the methods in this class.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val n = args[0].toInt()
        if (args.size == 2) setSeed(args[1].toLong())
        val probabilities = doubleArrayOf(0.5, 0.3, 0.1, 0.1)
        val frequencies = intArrayOf(5, 3, 1, 1)
        val a = "A B C D E F G".split(" ").toTypedArray()
        StdOut.println("seed = " + getSeed())
        for (i in 0 until n) {
            StdOut.printf("%2d ", uniform(100))
            StdOut.printf("%8.5f ", uniform(10.0, 99.0))
            StdOut.printf("%5b ", bernoulli(0.5))
            StdOut.printf("%7.5f ", gaussian(9.0, 0.2))
            StdOut.printf("%1d ", discrete(probabilities))
            StdOut.printf("%1d ", discrete(frequencies))
            StdOut.printf("%11d ", uniform(100000000000L))
            shuffle(a)
            for (s in a) StdOut.print(s)
            StdOut.println()
        }
    }

    // static initializer
    init {
        // this is how the seed was set in Java 1.4
        seed = System.currentTimeMillis()
        random = Random(seed)
    }
}