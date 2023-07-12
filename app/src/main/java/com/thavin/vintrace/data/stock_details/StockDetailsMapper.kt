package com.thavin.vintrace.data.stock_details

import com.thavin.vintrace.domain.stock_details.model.StockComponents
import com.thavin.vintrace.domain.stock_details.model.StockDetails
import com.thavin.vintrace.domain.stock_details.model.StockLevels

private const val WHITESPACE = " "


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
