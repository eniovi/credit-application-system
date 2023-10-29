package com.eniovi.credit.application.system.dto

import com.eniovi.credit.application.system.entity.Customer
import java.math.BigDecimal

data class CustomerUpdateDto(
    val firstName: String,
    val lastName: String,
    val income: BigDecimal,
    val zipCode: String,
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
