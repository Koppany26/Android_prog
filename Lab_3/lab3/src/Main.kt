import kotlin.random.Random

// 1. Item - data class
data class Item(
    val id: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)

// 2. ItemRepository - adatbázis réteg (CRUD műveletekkel)
class ItemRepository {
    private val items = mutableListOf<Item>()

    init {
        // Inicializálás 10 sporttal kapcsolatos kérdéssel
        items.add(Item(1, "Melyik sportágban ismert kifejezés a 'home run'?", listOf("Kosárlabda", "Baseball", "Tenisz", "Jégkorong"), 1))
        items.add(Item(2, "Hány játékos van egy labdarúgócsapatban a pályán egy időben?", listOf("9", "10", "11", "12"), 2))
        items.add(Item(3, "Melyik ország nyerte a legtöbb FIFA labdarúgó-világbajnokságot?", listOf("Németország", "Olaszország", "Argentína", "Brazília"), 3))
        items.add(Item(4, "Milyen hosszú egy hivatalos maratoni futás?", listOf("40 km", "42.195 km", "45 km", "50 km"), 1))
        items.add(Item(5, "Kinek a nevéhez fűződik a legtöbb Forma-1-es világbajnoki cím (holtversenyben)?", listOf("Ayrton Senna", "Alain Prost", "Michael Schumacher", "Sebastian Vettel"), 2))
        items.add(Item(6, "Melyik sportban használnak 'puck'-ot (korongot)?", listOf("Jégkorong", "Vízilabda", "Rögbi", "Krikett"), 0))
        items.add(Item(7, "Hány percig tart egy szabályos kosárlabda mérkőzés az NBA-ben (hosszabbítás nélkül)?", listOf("40 perc", "48 perc", "60 perc", "90 perc"), 1))
        items.add(Item(8, "Melyik sportág legendás alakja Usain Bolt?", listOf("Úszás", "Kerékpározás", "Atlétika", "Súlyemelés"), 2))
        items.add(Item(9, "Milyen színű a Tour de France összetett győztesének trikója?", listOf("Piros", "Zöld", "Pöttyös", "Sárga"), 3))
        items.add(Item(10, "Hol rendezték az első újkori olimpiai játékokat 1896-ban?", listOf("Párizs", "London", "Athén", "Róma"), 2))
    }

    // CRUD Műveletek
    fun findAll(): List<Item> = items.toList()

    fun findById(id: Int): Item? = items.find { it.id == id }

    fun create(item: Item) {
        if (items.none { it.id == item.id }) {
            items.add(item)
        }
    }

    fun update(item: Item) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            items[index] = item
        }
    }

    fun delete(id: Int) {
        items.removeIf { it.id == id }
    }
}

// 3. ItemService - Üzleti logika
class ItemService(private val repository: ItemRepository) {
    fun getRandomItems(count: Int): List<Item> {
        val allItems = repository.findAll()
        if (count > allItems.size) {
            throw IllegalArgumentException("Nincs elegendő kérdés az adatbázisban ($count kért, ${allItems.size} elérhető).")
        }
        return allItems.shuffled().take(count)
    }
}

// 4. ItemController - Kvíz vezérlés
class ItemController(private val service: ItemService) {
    fun quiz(questionCount: Int) {
        val questions = try {
            service.getRandomItems(questionCount)
        } catch (e: IllegalArgumentException) {
            println("Hiba: ${e.message}")
            return
        }

        var correctAnswers = 0

        println("=== SPORT KVÍZ INDUL ===")

        for ((index, item) in questions.withIndex()) {
            println("\n${index + 1}. Kérdés: ${item.question}")

            for ((ansIndex, answer) in item.answers.withIndex()) {
                println("  ${ansIndex + 1}) $answer")
            }

            var userAnswerIndex = -1
            while (userAnswerIndex !in 0 until item.answers.size) {
                print("Válaszod sorszáma (1-${item.answers.size}): ")
                val input = readlnOrNull()
                userAnswerIndex = (input?.toIntOrNull() ?: 0) - 1

                if (userAnswerIndex !in 0 until item.answers.size) {
                    println("Érvénytelen bevitel, kérlek a megadott számok közül válassz!")
                }
            }

            if (userAnswerIndex == item.correctAnswerIndex) {
                println("✅ Helyes válasz!")
                correctAnswers++
            } else {
                val correctAnswerText = item.answers[item.correctAnswerIndex]
                println("❌ Helytelen! A helyes válasz: $correctAnswerText")
            }
        }

        println("\n========================")
        println("$correctAnswers/${questions.size}")
        println("========================")
    }
}

// Főprogram
fun main() {
    val repository = ItemRepository()
    val service = ItemService(repository)
    val controller = ItemController(service)

    // Indítunk egy 5 kérdéses kvízt a 10 elérhetőből
    controller.quiz(10)
}