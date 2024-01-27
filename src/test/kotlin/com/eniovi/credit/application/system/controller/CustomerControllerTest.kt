package com.eniovi.credit.application.system.controller

import com.eniovi.credit.application.system.dto.CustomerDto
import com.eniovi.credit.application.system.dto.CustomerUpdateDto
import com.eniovi.credit.application.system.entity.Customer
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerControllerTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/customers"
        val customerBuilder: CustomerBuilder = CustomerBuilder()
    }

    @Test
    fun `should create a customer and return 201 status`() {
        val customerDto: CustomerDto = customerBuilder.builderCustomerDto()
        val valueAsString = objectMapper.writeValueAsString(customerDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Enio"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Santos"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("345.091.580-09"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("enio@teste.com.br"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua do Enio"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(BigDecimal.valueOf(1000.0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
    }

    @Test
    fun `should not save a customer with same CPF or email and return 409 status`() {
        customerRepository.save(customerBuilder.builderCustomerDto().toCustomer())

        val customerDto: CustomerDto = customerBuilder.builderCustomerDto()
        val valueAsString = objectMapper.writeValueAsString(customerDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("org.springframework.dao.DataIntegrityViolationException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    @Test
    fun `should not save a customer with firstName empty and return 400 status`() {
        val customerDto: CustomerDto = customerBuilder.builderCustomerDto(firstName = "")
        val valueAsString = objectMapper.writeValueAsString(customerDto)

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
    fun `should find customer by id and return 200 status`() {
        val customer = customerRepository.save(customerBuilder.builderCustomerDto().toCustomer())

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Enio"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Santos"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("345.091.580-09"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("enio@teste.com.br"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua do Enio"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(BigDecimal.valueOf(1000.0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
    }

    @Test
    fun `should not find a customer with invalid id and return 400 status`() {
        val invalidId = 2L

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${invalidId}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value())).andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("com.eniovi.credit.application.system.exception.BusinessException")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    @Test
    fun `should delete customer by id`() {
        val customer: Customer = customerRepository.save(customerBuilder.builderCustomerDto().toCustomer())

        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `should not delete a customer with invalid id and return 400 status`() {
        val invalidId = 2L

        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/${invalidId}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value())).andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("com.eniovi.credit.application.system.exception.BusinessException")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    @Test
    fun `should update a customer and return 200 status`() {
        val customer: Customer = customerRepository.save(customerBuilder.builderCustomerDto().toCustomer())
        val customerUpdateDto: CustomerUpdateDto = customerBuilder.builderCustomerUpdateDto()

        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)

        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Eniovi"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Santos Atualizado"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("345.091.580-09"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("enio@teste.com.br"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("123456"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua do Enio Atualizada"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(BigDecimal.valueOf(2000.0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
    }

    @Test
    fun `should not update a customer with invalid id and return 400 status`() {
        val invalidId = 2L
        val customerUpdateDto: CustomerUpdateDto = customerBuilder.builderCustomerUpdateDto()

        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)

        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL/${invalidId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value())).andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("com.eniovi.credit.application.system.exception.BusinessException")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
    }

    @Test
    fun `should not update a customer with firstName empty and return 400 status`() {
        val customer: Customer = customerRepository.save(customerBuilder.builderCustomerDto().toCustomer())
        val customerUpdateDto: CustomerUpdateDto = customerBuilder.builderCustomerUpdateDto(firstName = "")
        val valueAsString = objectMapper.writeValueAsString(customerUpdateDto)

        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL/${customer.id}")
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



}