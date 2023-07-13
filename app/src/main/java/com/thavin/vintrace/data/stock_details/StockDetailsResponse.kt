package com.thavin.vintrace.data.stock_details

import kotlinx.serialization.Serializable

@Serializable
data class StockDetailsResponse(
    val images: List<Images>,
    val code: String,
    val description: String,
    val secondaryDescription: String? = null,
    val beverageProperties: BeverageProperties? = null,
    val unit: Unit,
    val owner: Owner,
    val quantity: Quantity,
    val components: List<Components>
)

@Serializable
data class BeverageProperties(
    val colour: String,
    val description: String
)

@Serializable
data class Images(
    val endpoint: String
)

@Serializable
data class Quantity(
    val onHand: Int,
    val committed: Int,
    val ordered: Int
)

@Serializable
data class Components(
    val endpoint: String,
    val code: String,
    val description: String,
    val unit: Unit,
    val unitRequired: Boolean,
    val quantity: String
) {
    @Serializable
    data class Unit(
        val abbreviation: String
    )
}

@Serializable
data class Unit(
    val name: String
)

@Serializable
data class Owner(
    val name: String
)