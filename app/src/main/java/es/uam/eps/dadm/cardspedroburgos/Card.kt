package es.uam.eps.dadm.cardspedroburgos

import org.joda.time.DateTime
import java.io.Serializable
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

// Base class for any card
open class Card (var question: String = "", var answer: String = "",  val id: String = UUID.randomUUID().toString(), val date: String = DateTime.now().toLocalDate().toString()):  Serializable {
    var repetitions = 0
    var interval = 1
    var easiness = 2.5
    var expanded = false
    // This ones should probably be private but
    //  for simplicity reasons they are not (currently)
    var nextPracticeDate: Date = DateTime.now().toDate()
    var quality : Quality = Quality.NO






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
        val temp = DateTime(nextPracticeDate)
        nextPracticeDate = temp.plusDays(interval).toDate()


    }

}
