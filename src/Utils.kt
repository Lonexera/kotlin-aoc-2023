import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/testfiles/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Gets all numbers in the string and converts them to [Int]
 */
fun String.getAllNumbers(): List<Int> =
    buildList {
        var currentIndex = 0

        while (currentIndex <= this@getAllNumbers.lastIndex) {
            currentIndex += when {
                this@getAllNumbers[currentIndex].isDigit() -> {
                    val numberStr = this@getAllNumbers.substring(currentIndex).takeWhile { it.isDigit() }
                    add(numberStr.toInt())

                    numberStr.length
                }
                else -> 1
            }
        }
    }

/**
 * Gets all big numbers in the string and converts them to [BigInteger]
 */
fun String.getAllBigIntegers(): List<BigInteger> =
    buildList {
        var currentIndex = 0

        while (currentIndex <= this@getAllBigIntegers.lastIndex) {

            currentIndex += when {
                this@getAllBigIntegers[currentIndex].isDigit() -> {
                    val numberStr = this@getAllBigIntegers.substring(currentIndex).takeWhile { it.isDigit() }
                    add(BigInteger(numberStr))

                    numberStr.length
                }
                else -> {
                    1
                }
            }
        }
    }

/**
 * Gets all big numbers in the string and converts them to [Long]
 */
fun String.getAllLongs(): List<Long> =
    buildList {
        var currentIndex = 0

        while (currentIndex <= this@getAllLongs.lastIndex) {

            currentIndex += when {
                this@getAllLongs[currentIndex].isDigit() -> {
                    val numberStr = this@getAllLongs.substring(currentIndex).takeWhile { it.isDigit() }
                    add(numberStr.toLong())

                    numberStr.length
                }
                else -> {
                    1
                }
            }
        }
    }
