package com.eniovi.credit.application.system.service.impl

import com.eniovi.credit.application.system.dto.CustomerUpdateDto
import com.eniovi.credit.application.system.entity.Address
import com.eniovi.credit.application.system.entity.Customer
import com.eniovi.credit.application.system.exception.BusinessException
import com.eniovi.credit.application.system.repository.CustomerRepository
import com.eniovi.credit.application.system.service.ICustomerService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@ExtendWith(MockitoExtension::class)
class CustomerServiceTest {
    @Mock
    lateinit var customerRepository: CustomerRepository
    private lateinit var customerService: ICustomerService

    @BeforeEach
    fun init() {
        customerService = CustomerService(customerRepository)
    }

    @Test
    fun `should create customer`() {
        val fakeCustomer: Customer = buildCustomer()
        `when`(customerRepository.save(any())).thenReturn(fakeCustomer)

        val actual: Customer = customerService.save(fakeCustomer)

        assertNotNull(actual)
        assertEquals(fakeCustomer, actual)
        verify(customerRepository, times(1)).save(fakeCustomer)
    }

    @Test
    fun `should find customer by id`() {
        val fakeId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)

        `when`(customerRepository.findById(fakeId)).thenReturn(Optional.of(fakeCustomer))

        val actual: Customer = customerService.findById(fakeId)

        assertNotNull(actual)
        assertInstanceOf(Customer::class.java, actual)
        assertEquals(fakeCustomer, actual)
        verify(customerRepository, times(1)).findById(fakeId)
    }

    @Test
    fun `should not find customer by id and throws BusinessException`() {
        val fakeId: Long = Random.nextLong()

        `when`(customerRepository.findById(fakeId)).thenReturn(Optional.empty())

        val exception = assertThrows(BusinessException::class.java) { customerService.findById(fakeId) }
        assertEquals(exception.message, "Id $fakeId not found")
        verify(customerRepository, times(1)).findById(fakeId)
    }

    @Test
    fun `should update customer`() {
        val fakeId: Long = Random.nextLong()
        val fakeCustomerUpdateDto: CustomerUpdateDto = buildUpdateDto()

        val fakeCustomer = buildCustomer(id = fakeId)
        val fakeCustomerUpdated = fakeCustomerUpdateDto.toCustomer(fakeCustomer)

        `when`(customerRepository.findById(fakeId)).thenReturn(Optional.of(fakeCustomer))
        `when`(customerRepository.save(any())).thenReturn(fakeCustomerUpdated)

        val actual: Customer = customerService.update(fakeId, fakeCustomerUpdateDto)

        assertNotNull(actual)
        assertEquals(fakeCustomerUpdated, actual)
        verify(customerRepository, times(1)).save(fakeCustomerUpdated)
    }

    @Test
    fun `should not update customer and throws BusinessException`() {
        val fakeId: Long = Random.nextLong()
        val fakeCustomerUpdateDto: CustomerUpdateDto = buildUpdateDto()

        `when`(customerRepository.findById(fakeId)).thenReturn(Optional.empty())

        val exception = assertThrows(BusinessException::class.java) { customerService.update(fakeId, fakeCustomerUpdateDto) }
        assertEquals(exception.message, "Id $fakeId not found")
        verify(customerRepository, times(1)).findById(fakeId)
    }

    @Test
    fun `should delete customer by id`() {
        val fakeId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)

        `when`(customerRepository.findById(fakeId)).thenReturn(Optional.of(fakeCustomer))
        doNothing().`when`(customerRepository).delete(fakeCustomer)

        customerService.delete(fakeId)

        verify(customerRepository, times(1)).findById(fakeId)
        verify(customerRepository, times(1)).delete(fakeCustomer)
    }

    private fun buildCustomer(
        id: Long = 1L,
        firstName: String = "Enio",
        lastName: String = "Santos",
        cpf: String = "345.091.580-09",
        email: String = "enio@teste.com.br",
        income: BigDecimal = BigDecimal.valueOf(1000.00),
        password: String = "123456",
        street: String = "Rua do Enio",
        zipCode: String = "12345"
    ): Customer = Customer(
        id = id,
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street
        )
    )

    private fun buildUpdateDto(
        firstName: String = "Vinicius",
        lastName: String = "Santos",
        income: BigDecimal = BigDecimal.valueOf(1500.00),
        street: String = "Rua do Enio",
        zipCode: String = "12345"
    ): CustomerUpdateDto = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )

}