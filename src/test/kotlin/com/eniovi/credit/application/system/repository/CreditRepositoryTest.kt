package com.eniovi.credit.application.system.repository

import com.eniovi.credit.application.system.entity.Address
import com.eniovi.credit.application.system.entity.Credit
import com.eniovi.credit.application.system.entity.Customer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest() {
    @Autowired lateinit var creditRepository: CreditRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    @BeforeEach
    fun setup() {
        customer = testEntityManager.persist(buildCustomer())
        credit1 = testEntityManager.persist(buildCredit(customer = customer))
        credit2 = testEntityManager.persist(buildCredit(customer = customer))
    }

    @Test
    fun `should find credit by customer id and credit code`() {
        val creditCode1 = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a29")
        val creditCode2 = UUID.fromString("49f740be-46a7-449b-84e7-ff5b7986d7ef")
        val customerId: Long = customer.id!!

        credit1.creditCode = creditCode1
        credit2.creditCode = creditCode2

        val fakeCredit1 = creditRepository.findByCustomerIdAndCreditCode(customerId, creditCode1)
        val fakeCredit2 = creditRepository.findByCustomerIdAndCreditCode(customerId, creditCode2)

        assertNotNull(fakeCredit1)
        assertNotNull(fakeCredit2)
        assertEquals(fakeCredit1, credit1)
        assertEquals(fakeCredit2, credit2)
    }

    @Test
    fun `should find all credits by customer id`() {
        val customerId: Long = customer.id!!

        val creditList = creditRepository.findAllByCustomerId(customerId)

        assertNotNull(creditList)
        assertTrue(creditList.isNotEmpty())
        assertEquals(2, creditList.size)
        assertTrue(creditList.containsAll(listOf(credit1, credit2)))
    }

    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(500.00),
        dayFirstInstallment: LocalDate = LocalDate.of(2023, Month.APRIL, 22),
        numberOfInstallments: Int = 5,
        customer: Customer
    ): Credit = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer
    )

    private fun buildCustomer(
        firstName: String = "Enio",
        lastName: String = "Santos",
        cpf: String = "345.091.580-09",
        email: String = "enio@teste.com.br",
        income: BigDecimal = BigDecimal.valueOf(1000.00),
        password: String = "123456",
        street: String = "Rua do Enio",
        zipCode: String = "12345"
    ): Customer = Customer(
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


}