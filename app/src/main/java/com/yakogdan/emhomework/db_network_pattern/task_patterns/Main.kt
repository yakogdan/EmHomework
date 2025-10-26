package com.yakogdan.emhomework.db_network_pattern.task_patterns

fun main() {

    val car = Car.Builder()
        .make("Lada")
        .model("Vesta")
        .year(2023)
        .color("Red")
        .build()

    println(car)


    val factory1: Car.Factory = ToyotaFactory()
    val factory2: Car.Factory = BMWFactory()

    val sedan = factory1.createSedan(
        year = 2024,
        color = "White",
    )

    val suv = factory2.createSUV(
        year = 2025,
        color = "Black",
    )

    println(sedan)
    println(suv)
}