// Quality of question enum
enum class Quality (val value: Int) {
    NO(-1),
    DIFICIL(0),
    DUDO(3),
    FACIL(5);

    companion object {
        // Parses
        fun intToQuality(i : Int?): Quality {
            return when (i) {
                0 -> DIFICIL
                3 -> DUDO
                5 -> FACIL
                else -> NO
            }
        }
    }
}