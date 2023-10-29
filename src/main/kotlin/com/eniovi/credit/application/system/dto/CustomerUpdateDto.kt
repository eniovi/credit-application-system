package com.eniovi.credit.application.system.dto

import com.eniovi.credit.application.system.entity.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "First Name is required")
    val firstName: String,
    @field:NotEmpty(message = "Last Name is required")
    val lastName: String,
    @field:NotNull(message = "Income is required")
    val income: BigDecimal,
    @field:NotEmpty(message = "Zip Code is required")
    val zipCode: String,
    @field:NotEmpty(message = "Street is required")
    val street: String
) {
    fun toCustomer(costumer: Customer): Customer {
        costumer.firstName = this.firstName
        costumer.lastName = this.lastName
        costumer.income = this.income
        costumer.address.zipCode = this.zipCode
        costumer.address.street = this.street

        return costumer
    }
}
