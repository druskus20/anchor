import java.io.Serializable

// User command class
class Command(private val funct :(() -> Unit)?, val nombre : String) : Serializable {
    // A call funct wrapper
    fun call()  {
        funct?.invoke()
    }
}