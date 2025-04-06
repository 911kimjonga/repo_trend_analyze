package com.repo.auth

import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class AuthApplicationUnitTest {

    @Mock
    private lateinit var mock: Mock

    @InjectMocks
    lateinit var injectMocks: InjectMocks

    companion object {
        private var longValue: Long = 0L
        private lateinit var stringValue: String
    }

    @BeforeEach
    fun setUp() {
        longValue = 0L
        stringValue = "default"
    }

    @Test
    fun test() {

        val value = longValue

        whenever(mock).thenReturn(null)

        val result = transaction { injectMocks }

        assertTrue(true)
        verify(mock).mockMaker

    }

}