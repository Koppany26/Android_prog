import java.net.URL
import java.time.LocalDate
import java.util.TreeSet
import java.util.HashSet

// =====================================================================
// PROBLEM 1: Szótár (Interfaces, Classes, Enums, Singletons)
// =====================================================================

interface IDictionary {
    fun add(word: String): Boolean
    fun find(word: String): Boolean
    fun size(): Int
}

enum class DictionaryType {
    ARRAY_LIST, TREE_SET, HASH_SET
}

class ListDictionary : IDictionary {
    private val words: MutableList<String>

    init {
        println("Betöltés webről ListDictionary-be...")
        val url = URL("https://www.ms.sapientia.ro/~manyi/dict.txt")
        words = url.openStream().bufferedReader().readLines().toMutableList()
    }

    override fun add(word: String): Boolean = words.add(word)
    override fun find(word: String): Boolean = words.contains(word)
    override fun size(): Int = words.size
}

class TreeSetDictionary : IDictionary {
    private val words: TreeSet<String>

    init {
        println("Betöltés webről TreeSetDictionary-be...")
        val url = URL("https://www.ms.sapientia.ro/~manyi/dict.txt")
        words = TreeSet(url.openStream().bufferedReader().readLines())
    }

    override fun add(word: String): Boolean = words.add(word)
    override fun find(word: String): Boolean = words.contains(word)
    override fun size(): Int = words.size
}

class HashSetDictionary : IDictionary {
    private val words: HashSet<String>

    init {
        println("Betöltés webről HashSetDictionary-be...")
        val url = URL("https://www.ms.sapientia.ro/~manyi/dict.txt")
        words = HashSet(url.openStream().bufferedReader().readLines())
    }

    override fun add(word: String): Boolean = words.add(word)
    override fun find(word: String): Boolean = words.contains(word)
    override fun size(): Int = words.size
}

// Singleton szolgáltató osztály (object)
object DictionaryProvider {
    // Ezek lazy módon jönnek létre, így csak akkor töltődik le, ha tényleg kérjük
    private var listDictionary: IDictionary? = null
    private var treeSetDictionary: IDictionary? = null
    private var hashSetDictionary: IDictionary? = null

    fun createDictionary(type: DictionaryType): IDictionary {
        return when (type) {
            DictionaryType.ARRAY_LIST -> {
                if (listDictionary == null) listDictionary = ListDictionary()
                listDictionary!!
            }
            DictionaryType.TREE_SET -> {
                if (treeSetDictionary == null) treeSetDictionary = TreeSetDictionary()
                treeSetDictionary!!
            }
            DictionaryType.HASH_SET -> {
                if (hashSetDictionary == null) hashSetDictionary = HashSetDictionary()
                hashSetDictionary!!
            }
        }
    }
}


// =====================================================================
// PROBLEM 2: Extension functions (Kiterjesztés függvények)
// =====================================================================

// 1. Monogram készítő (pl. John Smith -> JS) - Ciklus nélkül!
fun String.monogram(): String = this.split(" ").map { it.first().uppercaseChar() }.joinToString("")

// 2. Lista elemeinek összefűzése szeparátorral (compact function)
fun List<String>.joinBySeparator(separator: String): String = this.joinToString(separator)

// 3. Leghosszabb string a listában (compact function)
fun List<String>.longest(): String? = this.maxByOrNull { it.length }


// =====================================================================
// PROBLEM 3: Date (Data class, Companion, Comparable)
// =====================================================================

// Default értékek a mai napra állítva
data class Date(
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().monthValue,
    val day: Int = LocalDate.now().dayOfMonth
) : Comparable<Date> {

    // Natural ordering: Év, majd hónap, majd nap alapján
    override fun compareTo(other: Date): Int {
        if (this.year != other.year) return this.year.compareTo(other.year)
        if (this.month != other.month) return this.month.compareTo(other.month)
        return this.day.compareTo(other.day)
    }
}

// Leap year ellenőrzés
fun Date.isLeapYear(): Boolean {
    return (this.year % 4 == 0 && this.year % 100 != 0) || this.year % 400 == 0
}

// Valid dátum ellenőrzés
fun Date.isValid(): Boolean {
    if (year < 1 || month !in 1..12 || day < 1) return false

    val maxDays = when (month) {
        2 -> if (isLeapYear()) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
    return day <= maxDays
}



fun main() {
    println("==== PROBLEM 1: Szótár ====")
    // Létrehozunk egy HASH_SET alapú szótárat a Provideren keresztül
    val dict: IDictionary = DictionaryProvider.createDictionary(DictionaryType.HASH_SET)
    println("Number of words: ${dict.size()}")

    var word: String?
    while (true) {
        print("Mit keresel? (Írd be: quit a kilépéshez): ")
        word = readlnOrNull() // readLine() helyett ez az újabb ajánlott Kotlinban
        if (word == "quit") {
            println("Kilépés a szótárból...\n")
            break
        }
        println("Result: ${word?.let { dict.find(it) }}")
    }

    //---------------------------------------------------------
    println("==== PROBLEM 2: Extension functions ====")
    val name = "John Smith"
    println("Név: $name -> Monogram: ${name.monogram()}")

    val fruits = listOf("apple", "pear", "strawberry", "melon")
    println("Lista szeparátorral: ${fruits.joinBySeparator(" # ")}")
    println("Leghosszabb szó: ${fruits.longest()}")
    println()

    //---------------------------------------------------------
    println("==== PROBLEM 3: Date ====")
    val validDates = mutableListOf<Date>()

    println("Érvénytelen generált dátumok:")
    // Addig generálunk, amíg nincs 10 érvényes
    while (validDates.size < 10) {
        val randomYear = (2000..2024).random()
        val randomMonth = (1..15).random() // Hogy legyen érvénytelen is!
        val randomDay = (1..40).random()   // Hogy legyen érvénytelen is!

        val newDate = Date(randomYear, randomMonth, randomDay)

        if (newDate.isValid()) {
            validDates.add(newDate)
        } else {
            println(newDate) // Kiírjuk az érvényteleneket
        }
    }

    println("\n10 Érvényes dátum (eredeti sorrend):")
    validDates.forEach { println(it) }

    println("\nRendezett dátumok (Natural ordering):")
    val sortedDates = validDates.sorted()
    sortedDates.forEach { println(it) }

    println("\nFordított rendezés (Reversed):")
    sortedDates.reversed().forEach { println(it) }

    println("\nEgyéni rendezés (Custom: csak hónap alapján):")
    // A Comparator interface használata:
    val customComparator = Comparator<Date> { d1, d2 -> d1.month.compareTo(d2.month) }
    validDates.sortedWith(customComparator).forEach { println(it) }
}