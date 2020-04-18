package es.uam.eps.dadm.cards

import org.joda.time.DateTime
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt
import java.io.Serializable
// Base class for any card
open class Card (var question: String, var answer: String,  val id: String = UUID.randomUUID().toString(), val date: String = DateTime.now().toLocalDate().toString()):  Serializable {
    private var repetitions = 0
    private var interval = 1
    private var easiness = 2.5
    var expanded = false
    // This ones should probably be private but
    //  for simplicity reasons they are not (currently)
    var nextPracticeDate: DateTime = DateTime.now()
    var quality : Quality = Quality.NO



    // Shows the anwser and stores the input quality
    open fun show() {
        println(question)

        print("\tINTRO para ver la respuesta: ")
        readLine()

        println("\t$answer")

        do {
            print("\tTeclea 0 (Dificil) 3 (Dudo) 5 (Facil): ")
            quality = readLine().let{
                Quality.intToQuality(it?.toIntOrNull())
            }
        } while(quality == Quality.NO)
    }

    // Updates the card info based on the quality received
    fun update(){
        // Recalculates properties based on quality
        val q = quality.value
        easiness = max(1.3, easiness + 0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
        // Update repetitions
        if (q < 3) repetitions = 0
        else repetitions++
        // Update interval
        if (repetitions <= 1)       interval = 1
        else if (repetitions == 2)  interval = 6
        else                        interval = (interval*easiness).roundToInt()

        // Set nextPracticeDate
        nextPracticeDate = nextPracticeDate.plusDays(interval)
    }


}
