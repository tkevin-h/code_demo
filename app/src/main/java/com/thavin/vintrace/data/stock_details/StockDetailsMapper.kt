package com.thavin.vintrace.data.stock_details

import com.thavin.vintrace.domain.stock_details.model.StockComponents
import com.thavin.vintrace.domain.stock_details.model.StockDetails
import com.thavin.vintrace.domain.stock_details.model.StockLevels

private const val WHITESPACE = " "
private const val EMPTY = ""
private const val STOCK_ITEM_1 = "stock-item-1"
private const val STOCK_ITEM_2 = "stock-item-2"
private const val STOCK_ITEM_3 = "stock-item-3"
private const val STOCK_ITEM_4 = "stock-item-4"
private const val STOCK_ITEM_5 = "stock-item-5"


fun StockDetailsResponse.toStockDetails() =
    StockDetails(
        images = mapImages(this.images),
        code = this.code,
        description = this.description,
        secondaryDescription = this.secondaryDescription,
        color = this.beverageProperties.colour,
        beverageDescription = this.beverageProperties.description,
        ownerName = this.owner.name,
        unitName = this.unit.name,
        stockLevels = this.quantity.toStockLevels(),
        stockComponents = this.components.map {
            it.toStockComponents()
        }
    )

private fun mapImages(images: List<Images>) =
    images.map {
        it.endpoint
    }

private fun Quantity.toStockLevels() =
    StockLevels(
        onHand = this.onHand,
        committed = this.committed,
        inProduction = this.ordered,
        available = calculateAvailableStock(this.onHand, this.ordered, this.committed)
    )

private fun calculateAvailableStock(onHand: Int, ordered: Int, committed: Int) =
    (onHand + ordered) - committed

private fun Components.toStockComponents() =
    StockComponents(
        id = mapEndpoint(this.endpoint),
        code = this.code,
        description = this.description,
        quantity = mapComponentQuantity(this.quantity, this.unitRequired, this.unit.abbreviation)
    )

private fun mapComponentQuantity(quantity: String, showUnit: Boolean, abbreviation: String) =
    if (showUnit) {
        quantity + WHITESPACE + abbreviation
    } else {
        quantity
    }

private fun mapEndpoint(endpoint: String) =
    when (endpoint) {
        StockEndpoints.ITEM1.endpoint -> { STOCK_ITEM_1 }
        StockEndpoints.ITEM2.endpoint -> { STOCK_ITEM_2 }
        StockEndpoints.ITEM3.endpoint -> { STOCK_ITEM_3 }
        StockEndpoints.ITEM4.endpoint -> { STOCK_ITEM_4 }
        StockEndpoints.ITEM5.endpoint -> { STOCK_ITEM_5 }
        else -> EMPTY
    }

