package com.eniovi.credit.application.system.repository

import com.eniovi.credit.application.system.entity.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface CreditRepository: JpaRepository<Credit, Long> {

    @Query("SELECT c FROM Credit c " +
            "WHERE c.customer.id = :customerId AND c.creditCode = :creditCode")
    fun findByCustomerIdAndCreditCode(customerId: Long, creditCode: UUID): Credit?

    @Query("SELECT c FROM Credit c " +
            "WHERE c.customer.id = :customerId")
    fun findAllByCustomerId(customerId: Long): List<Credit>
}