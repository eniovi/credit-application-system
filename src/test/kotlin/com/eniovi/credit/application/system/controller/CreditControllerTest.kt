package com.eniovi.credit.application.system.controller

import com.eniovi.credit.application.system.dto.CreditDto
import com.eniovi.credit.application.system.entity.Customer
import com.eniovi.credit.application.system.enumx.Status
import com.eniovi.credit.application.system.repository.CreditRepository
import com.eniovi.credit.application.system.repository.CustomerRepository
import com.eniovi.credit.application.system.utils.CustomerBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CreditControllerTest {
    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/credits"
        val customerBuilder: CustomerBuilder = CustomerBuilder()
    }

    @Test
    fun `should create a credit and return 201 status`() {
        val customer = customerRepository.save(CustomerControllerTest.customerBuilder.builderCustomerDto().toCustomer())
        val creditDto: CreditDto = builderCreditDto(customerId = customer.id!!)
        val valueAsString = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(BigDecimal.valueOf(1500.00)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.IN_PROGRESS.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("enio@teste.com.br"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(BigDecimal.valueOf(1000.00)))
    }

    @Test
    fun `should not save a credit with dayFirstInstallment not a future date and return 400 status`() {
        val customer: Customer =
            customerRepository.save(CustomerControllerTest.customerBuilder.builderCustomerDto().toCustomer())

        val creditDto: CreditDto = builderCreditDto(customerId = customer.id!!, dayFirstInstallment = LocalDate.now())
        val valueAsString = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    @Test
    fun `should not save a credit with numberOfInstallments smaller than 1 and return 400 status`() {
        val customer: Customer =
            customerRepository.save(CustomerControllerTest.customerBuilder.builderCustomerDto().toCustomer())

        val creditDto: CreditDto = builderCreditDto(customerId = customer.id!!, numberOfInstallments = 0)
        val valueAsString = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    @Test
    fun `should not save a credit with numberOfInstallments bigger than 48 and return 400 status`() {
        val customer: Customer =
            customerRepository.save(CustomerControllerTest.customerBuilder.builderCustomerDto().toCustomer())

        val creditDto: CreditDto = builderCreditDto(customerId = customer.id!!, numberOfInstallments = 49)
        val valueAsString = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(1500.00),
        dayFirstInstallment: LocalDate = LocalDate.now().plusDays(5),
        numberOfInstallments: Int = 5,
        customerId: Long = 1L
    ): CreditDto = CreditDto(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId
    )
}