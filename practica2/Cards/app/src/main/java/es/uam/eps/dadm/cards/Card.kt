package es.uam.eps.dadm.cards

import java.lang.NullPointerException
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt
import java.io.Serializable
// Base classa for any card
open class Card (var question: String, var answer: String,  val id: String = UUID.randomUUID().toString(), val date: String = Date().toString()):  Serializable {
    private var repetitions = 0
    private var interval = 1
    var easiness = 2.5
    var expanded = false
    // This ones should probably be private but
    //  for simplicity reasons they are not (currently)
    var nextPracticeDate = 1
    var currentDate = 0
    var quality : Quality = Quality.NO


    companion object {
        // Asks for a question and answer and returns a new es.uam.eps.dadm.cards.Card
        fun readCard(): Card {

            var tipo: Int?
            do {
                print("Introduce el tipo 0 (es.uam.eps.dadm.cards.Card) 1 (es.uam.eps.dadm.cards.Cloze): ")
                tipo = readLine()?.toIntOrNull()
            } while (tipo==null || tipo !in 0..1)



            println("Nueva Tarjeta:")
            print("Teclee la pregunta: ")
            var question: String?
            do {
                question = readLine()
                if (question.isNullOrEmpty()) {
                    println("Error, introduce la pregunta de nuevo")
                    question = null
                }
            } while (question == null)
            print("Teclee la respuesta: ")
            var answer: String?
            do {
                answer = readLine()
                if (answer.isNullOrEmpty()) {
                    println("Error, introduce la respuesta de nuevo")
                    answer = null
                }
            } while (answer == null)

            // Create the desired card type
            return when (tipo) {
                0 ->  Card(question, answer)
                1 -> Cloze(question, answer)
                else -> throw NullPointerException("lecturaTarjeta genera valor nulo")
            }
        }
    }

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
        nextPracticeDate = currentDate+interval
    }

    // Shows details for each card
    fun details(){
        println("$question ($answer)        eas = %.2f rep = $repetitions int = $interval next = $nextPracticeDate".format(easiness))
    }


}
