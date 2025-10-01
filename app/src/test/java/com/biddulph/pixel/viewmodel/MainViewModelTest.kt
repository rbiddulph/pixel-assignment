package com.biddulph.pixel.viewmodel

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MainViewModelTest {

    @Test
    fun `check state launches as loading`() = runTest {
        val viewModel = MainViewModel()
        assertTrue(viewModel.state.value is MainViewState.Loading)

    }
}