package com.yakogdan.emhomework.db_network_pattern.task_patterns

class ToyotaFactory : Car.Factory {

    override fun createSedan(year: Int, color: String): Car =
        Car.Builder()
            .make("Toyota")
            .model("Camry")
            .year(year)
            .color(color)
            .build()

    override fun createSUV(year: Int, color: String): Car =
        Car.Builder()
            .make("Toyota")
            .model("RAV4")
            .year(year)
            .color(color)
            .build()
}