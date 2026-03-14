package haze.command

// created by dicves_recode on 23.12.2025
abstract class Command(
    val name: String,
    val desc: String,
    val usage: String,
    vararg val aliases: String
) {
    init {
        Commands.add(this)
    }

    abstract fun execute(args: List<String>)
}
