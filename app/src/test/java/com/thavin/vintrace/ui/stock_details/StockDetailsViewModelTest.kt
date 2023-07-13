package com.thavin.vintrace.ui.stock_details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.thavin.vintrace.domain.stock_details.StockDetailsRepository
import com.thavin.vintrace.domain.stock_details.model.StockComponents
import com.thavin.vintrace.domain.stock_details.model.StockDetails
import com.thavin.vintrace.domain.stock_details.model.StockLevels
import com.thavin.vintrace.ui.stock_details.contract.StockDetailsEvent
import com.thavin.vintrace.ui.stock_details.contract.StockDetailsIntent
import com.thavin.vintrace.ui.stock_details.contract.StockDetailsState
import com.thavin.vintrace.util.ResourceResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StockDetailsViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var stockDetailsViewModel: StockDetailsViewModel
    private val stockDetailsRepository = mockk<StockDetailsRepository>(relaxed = true)
    private val savedStateHandle = mockk<SavedStateHandle>()
    private val key = "stockId"
    private val stockId = "CHRD/EU/2016"

    private val stockDetails =
        StockDetails(
            images = listOf("generic image"),
            code = "CHRD/EU/2016",
            description = "Standard EU export bottle",
            secondaryDescription = "Bottle - 750 ml",
            color = "#DAC88B",
            beverageDescription = "2016 Chardonnay YV/MP",
            ownerName = "vintrace Winery",
            unitName = "Single bottle (x1)",
            stockLevels = StockLevels(
                onHand = 68000,
                committed = 20000,
                inProduction = 35000,
                available = 68000
            ),
            stockComponents = listOf(
                StockComponents(
                    id = "/stock-items/2",
                    code = "16YAVCRD01/BLK",
                    description = "2016 Chardonnay YV/MP",
                    quantity = "12"
                ),
            )
        )

    @Before
    fun beforeTests() {
        Dispatchers.setMain(dispatcher)
        every { savedStateHandle.get<String>(key) } returns stockId

        stockDetailsViewModel =
            StockDetailsViewModel(
                stockDetailsRepository,
                savedStateHandle
            )
    }

    @After
    fun afterTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun `The stock screen is initialised`() {
        with(stockDetailsViewModel.state.value) {
            assertThat(stockDetails).isEqualTo(StockDetails())
            assertThat(isError).isEqualTo(false)
            assertThat(errorMessage).isEqualTo(null)
            assertThat(isLoading).isEqualTo(false)
            assertThat(event).isEqualTo(StockDetailsEvent.Idle)
        }
    }

    @Test
    fun `The stock details is loading`() = runTest {
        coEvery { stockDetailsRepository.getStockDetails(stockId) } returns flowOf(
            ResourceResult.Loading(isLoading = true)
        )

        stockDetailsViewModel.processIntent(StockDetailsIntent.GetStockDetails(stockId))

        dispatcher.scheduler.advanceUntilIdle()

        stockDetailsViewModel.state.test {
            val emission = awaitItem()

            assertThat(emission).isEqualTo(
                StockDetailsState(
                    stockDetails = StockDetails(),
                    isError = false,
                    errorMessage = null,
                    isLoading = true,
                    event = StockDetailsEvent.Idle
                )
            )
        }
    }

    @Test
    fun `The stock details is successfully loaded`() = runTest {
        coEvery { stockDetailsRepository.getStockDetails(stockId) } returns flowOf(
            ResourceResult.Success(stockDetails)
        )

        stockDetailsViewModel.processIntent(StockDetailsIntent.GetStockDetails(stockId))

        dispatcher.scheduler.advanceUntilIdle()

        stockDetailsViewModel.state.test {
            val emission = awaitItem()

            assertThat(emission).isEqualTo(
                StockDetailsState(
                    stockDetails = stockDetails,
                    isError = false,
                    errorMessage = null,
                    isLoading = false,
                    event = StockDetailsEvent.Idle
                )
            )
        }
    }

    @Test
    fun `The stock details returns an error`() = runTest {
        coEvery { stockDetailsRepository.getStockDetails(stockId) } returns flowOf(
            ResourceResult.Error(message = "error")
        )

        stockDetailsViewModel.processIntent(StockDetailsIntent.GetStockDetails(stockId))

        dispatcher.scheduler.advanceUntilIdle()

        stockDetailsViewModel.state.test {
            val emission = awaitItem()

            assertThat(emission).isEqualTo(
                StockDetailsState(
                    stockDetails = StockDetails(),
                    isError = true,
                    errorMessage = "error",
                    isLoading = false,
                    event = StockDetailsEvent.Idle
                )
            )
        }
    }

    @Test
    fun `The back button is clicked`() = runTest {
        stockDetailsViewModel.processIntent(StockDetailsIntent.BackOnClick)

        stockDetailsViewModel.state.test {
            listOf(
                StockDetailsEvent.Idle,
                StockDetailsEvent.NavigateBack
            ).forEach { event ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(
                    StockDetailsState(
                        stockDetails = StockDetails(),
                        isError = false,
                        errorMessage = null,
                        isLoading = false,
                        event = event
                    )
                )
            }
        }
    }

    @Test
    fun `The edit button is clicked`() = runTest {
        stockDetailsViewModel.processIntent(StockDetailsIntent.EditOnClick("test"))

        stockDetailsViewModel.state.test {
            listOf(
                StockDetailsEvent.Idle,
                StockDetailsEvent.ShowToast("test")
            ).forEach { event ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(
                    StockDetailsState(
                        stockDetails = StockDetails(),
                        isError = false,
                        errorMessage = null,
                        isLoading = false,
                        event = event
                    )
                )
            }
        }
    }

    @Test
    fun `The more actions button is clicked`() = runTest {
        stockDetailsViewModel.processIntent(StockDetailsIntent.MoreActionsOnClick)

        stockDetailsViewModel.state.test {
            listOf(
                StockDetailsEvent.Idle,
                StockDetailsEvent.ShowWebView
            ).forEach { event ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(
                    StockDetailsState(
                        stockDetails = StockDetails(),
                        isError = false,
                        errorMessage = null,
                        isLoading = false,
                        event = event
                    )
                )
            }
        }
    }

    @Test
    fun `The component button is clicked`() = runTest {
        stockDetailsViewModel.processIntent(StockDetailsIntent.ComponentOnClick("testId"))

        stockDetailsViewModel.state.test {
            listOf(
                StockDetailsEvent.Idle,
                StockDetailsEvent.Navigate("testId")
            ).forEach { event ->
                val emission = awaitItem()
                assertThat(emission).isEqualTo(
                    StockDetailsState(
                        stockDetails = StockDetails(),
                        isError = false,
                        errorMessage = null,
                        isLoading = false,
                        event = event
                    )
                )
            }
        }
    }
}