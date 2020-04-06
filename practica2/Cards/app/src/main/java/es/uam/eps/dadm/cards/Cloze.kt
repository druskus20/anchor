// Special type of card for  *text* questions
class Cloze(question : String, answer: String) :  Card(question, answer)  {

    override fun show () {
        println(question)

        var input: String? = null
        while (input == null) {
            print("\tINTRO para ver la respuesta: ")
            input = readLine()
        }

        // Prints the answer, substitutes answer into *...* if present
        println(question.replaceFirst("\\*.*\\*".toRegex(), answer))

        do {
            print("\tTeclea 0 (Dificil) 3 (Dudo) 5 (Facil): ")
            quality = readLine().let{
                Quality.intToQuality(it?.toIntOrNull())
            }
        } while(quality == Quality.NO)
    }
}
