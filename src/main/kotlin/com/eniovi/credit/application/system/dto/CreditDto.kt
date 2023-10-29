package com.eniovi.credit.application.system.dto

import com.eniovi.credit.application.system.entity.Credit
import com.eniovi.credit.application.system.entity.Customer
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Credit Value is required")
    val creditValue: BigDecimal,
    @field:Future(message = "Day first installment must be after current date")
    val dayFirstInstallment: LocalDate,
    @field:Min(value = 1)
    @field:Max(value = 48)
    val numberOfInstallments: Int,
    @field:NotNull(message = "Customer Id is required")
    var customerId: Long
) {

    fun toCredit(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
