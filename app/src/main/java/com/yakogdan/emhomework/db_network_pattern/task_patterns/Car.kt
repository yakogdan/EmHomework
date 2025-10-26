package com.yakogdan.emhomework.db_network_pattern.task_patterns

data class Car(
    val make: String,
    val model: String,
    val year: Int,
    val color: String,
) {

    class Builder {
        private var make: String = "Неизвестно"
        private var model: String = "Неизвестно"
        private var year: Int = -1
        private var color: String = "Неизвестно"

        fun make(value: String) = apply { make = value.trim() }
        fun model(value: String) = apply { model = value.trim() }
        fun year(value: Int) = apply { year = value }
        fun color(value: String) = apply { color = value.trim() }


        fun build(): Car = Car(
            make = make,
            model = model,
            year = year,
            color = color,
        )
    }

    interface Factory {

        fun createSedan(
            year: Int,
            color: String,
        ): Car

        fun createSUV(
            year: Int,
            color: String,
        ): Car
    }
}