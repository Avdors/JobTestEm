package com.example.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class Offer(
    val id: String? = null,  // id может отсутствовать в некоторых объектах, поэтому он nullable
    val title: String,
    val link: String,
    val button: Button? = null // button присутствует не во всех объектах offers
)

@Serializable
data class Button(
    val text: String
)