package com.dicoding.myaqidahmobile.ui.jadwal

import androidx.arch.core.executor.testing.*
import androidx.lifecycle.*
import com.dicoding.myaqidahmobile.core.data.*
import com.dicoding.myaqidahmobile.core.domain.model.*
import com.dicoding.myaqidahmobile.core.domain.usecase.*
import com.dicoding.myaqidahmobile.ui.utils.*
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.koin.core.context.*
import org.koin.dsl.*
import org.koin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class DoctorViewModelTest : KoinTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Define test module
        val testModule = module {
            single<DoctorUseCase> { mockk() }
            single { DoctorViewModel(get()) }
        }

        startKoin {
            printLogger()
            modules(testModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `getDoctors returns success`() = runTest(testDispatcher) {
        val mockDoctors = DataDummy.generateDummyDoctorEntity()

        val mockDoctorUseCase: DoctorUseCase = get()

        coEvery { mockDoctorUseCase.getAllDoctor() } returns flow {
            emit(Resource.Success(mockDoctors))
        }

        val viewModel: DoctorViewModel = get()
        val observer = Observer<Resource<List<JadwalDoctors>>> {}

        try {
            viewModel.getDoctors.observeForever(observer)
            val result = viewModel.getDoctors.getOrAwaitValue()

            Assert.assertTrue(result is Resource.Success)
            Assert.assertEquals(mockDoctors.size, (result as Resource.Success).data?.size)
        } finally {
            viewModel.getDoctors.removeObserver(observer)
        }
    }

    @Test
    fun `getDoctors returns error`() = runTest(testDispatcher) {
        val mockDoctorUseCase: DoctorUseCase = get()

        coEvery { mockDoctorUseCase.getAllDoctor() } returns flow {
            emit(Resource.Error("Error occurred"))
        }

        val viewModel: DoctorViewModel = get()
        val observer = Observer<Resource<List<JadwalDoctors>>> {}

        try {
            viewModel.getDoctors.observeForever(observer)
            val result = viewModel.getDoctors.getOrAwaitValue()

            Assert.assertTrue(result is Resource.Error)
            Assert.assertEquals("Error occurred", (result as Resource.Error).message)
        } finally {
            viewModel.getDoctors.removeObserver(observer)
        }
    }
}
