package com.yakogdan.emhomework.db_network_pattern.task_patterns

class BMWFactory : Car.Factory {

    override fun createSedan(year: Int, color: String): Car =
        Car.Builder()
            .make("BMW")
            .model("3 Series")
            .year(year)
            .color(color)
            .build()

    override fun createSUV(year: Int, color: String): Car =
        Car.Builder()
            .make("BMW")
            .model("X5")
            .year(year)
            .color(color)
            .build()
}