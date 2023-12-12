package helper

sealed interface Debug {

    operator fun invoke(action: () -> Unit)

    data object Enabled : Debug {
        override fun invoke(action: () -> Unit) {
            action()
        }
    }

    data object Disabled : Debug {
        override fun invoke(action: () -> Unit) {}
    }
}