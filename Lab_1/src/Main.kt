
fun isPrime(number: Int): Boolean {
    // A 0, az 1 és a negatív számok nem prímek
    if (number <= 1) return false

    // Végigmegyünk az osztókon 2-től a szám feléig
    for (i in 2..number / 2) {
        if (number % i == 0) {
            return false // Ha találunk osztót, akkor nem prím
        }
    }
    return true // Ha nem találtunk osztót, akkor prím
}

// --- 4. Feladat segédfüggvényei (Kódolás / Dekódolás) ---
// Egyszerű eltolásos titkosítás (minden karaktert a következőre cserél az ábécében)
fun encode(msg: String): String = msg.map { it + 1 }.joinToString("")
fun decode(msg: String): String = msg.map { it - 1 }.joinToString("")

// Higher-order (magasabb rendű) függvény, ami paraméterként egy másik függvényt vár
fun messageCoding(msg: String, func: (String) -> String): String {
    return func(msg)
}

// --- 5. Feladat segédfüggvénye (Compact function) ---
// Egy soros (compact) függvény, ami szűri és kiírja a páros számokat
fun printEvenNumbers(list: List<Int>) = println(list.filter { it % 2 == 0 })


// --- 9. Feladat segédfüggvénye (Anagrammák) ---
fun groupAnagrams(strs: Array<String>): List<List<String>> {
    // Csoportosítjuk a szavakat aszerint, hogy mik a betűik ábécé sorrendbe rakva
    return strs.groupBy { it.lowercase().toCharArray().sorted().joinToString("") }.values.toList()
}


fun main() {
    println("EX 1")
    val a=2
    val b=3
    println("$a+$b = ${a+b}")
//------------------------------------------
    println("EX 2")
    val daysOfWeek = listOf("Monday","Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    println("A het napjai: ")
    for(day in daysOfWeek){
        println(day)
    }

    val tDays = daysOfWeek.filter { it.startsWith("T") }
    println("\n'T' betűvel kezdődő napok: $tDays")

    // 4. Szűrés: 'e' betűt tartalmazó napok
    val containsE = daysOfWeek.filter { it.contains("e") }
    println("\n'e' betűt tartalmazó napok: $containsE")

    // 5. Szűrés: Pontosan 6 karakter hosszú napok
    val lengthOf6 = daysOfWeek.filter { it.length == 6 }
    println("\n6 betűs napok: $lengthOf6")

//------------------------------------------------------------
    println("EX 3")
    val myRange = 1..50

    println("Prímszámok a $myRange tartományban:")

    // Végigmegyünk a tartomány számain
    for (num in myRange) {
        // Ha a függvényünk igazat ad vissza, kiíratjuk a számot
        if (isPrime(num)) {
            print("$num ") // Itt 'print'-et használunk 'println' helyett, hogy egy sorba kerüljenek
        }
    }
    println()

    //------------------------------------------
    println("\nEX 4")
    val originalMessage = "almafa"
    println("Eredeti üzenet: $originalMessage")

    // Kódolás a messageCoding függvénnyel (átadjuk az encode függvény hivatkozását a :: operátorral)
    val encodedMsg = messageCoding(originalMessage, ::encode)
    println("Kódolva: $encodedMsg")

    // Dekódolás
    val decodedMsg = messageCoding(encodedMsg, ::decode)
    println("Dekódolva: $decodedMsg")

    //------------------------------------------
    println("\nEX 5")
    val numberList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    print("Páros számok a listából: ")
    printEvenNumbers(numberList)

    //------------------------------------------
    println("\nEX 6")
    val ints = listOf(1, 2, 3, 4, 5)

    val doubled = ints.map { it * 2 }
    println("Duplázott számok: $doubled")

    // uppercase() használata nagybetűsítésre
    println("Nagybetűs napok: ${daysOfWeek.map { it.uppercase() }}")

    // Első karakter kivágása és nagybetűsítése
    println("Első karakterek: ${daysOfWeek.map { it.first().uppercaseChar() }}")

    // Hosszúságok
    println("Napok hossza: ${daysOfWeek.map { it.length }}")

    // Átlagos hossz
    val avgLength = daysOfWeek.map { it.length }.average()
    println("Átlagos hossz: $avgLength")

    //------------------------------------------
    println("\nEX 7")
    // Módosítható listává alakítás
    val mutableDays = daysOfWeek.toMutableList()

    // 'n' betűt tartalmazó napok eltávolítása (ignoreCase = true biztosítja a kis/nagybetű függetlenséget)
    mutableDays.removeAll { it.contains("n", ignoreCase = true) }
    println("List n betű nélkül: $mutableDays")

    // Elemek kiíratása index-szel
    for ((index, item) in mutableDays.withIndex()) {
        println("Item at $index is $item")
    }

    // Sorbarendezés
    mutableDays.sort()
    println("Sorbarendezve: $mutableDays")

    //------------------------------------------
    println("\nEX 8")
    // 10 elemű tömb generálása 0 és 100 közötti véletlenszámokkal
    val randomArray = IntArray(10) { (0..100).random() }

    println("Generált tömb elemei:")
    randomArray.forEach { println(it) }

    println("Növekvő sorrendben: ${randomArray.sorted().joinToString()}")

    println("Van-e páros szám benne? ${randomArray.any { it % 2 == 0 }}")

    println("Minden szám páros-e? ${randomArray.all { it % 2 == 0 }}")

    // Átlag számítása forEach használatával (ahogy a feladat kérte)
    var sum = 0.0
    randomArray.forEach { sum += it }
    println("Generált számok átlaga: ${sum / randomArray.size}")

    //------------------------------------------
    println("\nEX 9")
    val inputStrings = arrayOf("eat", "tea", "tan", "ate", "nat", "bat")
    val groupedAnagrams = groupAnagrams(inputStrings)

    println("Anagramma csoportok:")
    for (group in groupedAnagrams) {
        println(group)
    }
}