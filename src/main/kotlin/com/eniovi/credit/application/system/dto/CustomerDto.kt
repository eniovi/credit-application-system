package com.eniovi.credit.application.system.dto

import com.eniovi.credit.application.system.entity.Address
import com.eniovi.credit.application.system.entity.Customer
import java.math.BigDecimal

data class CustomerDto(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val password: String,
    val zipCode: String,
    val street: String
) {
    fun toCustomer(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        email = this.email,
        password = this.password,
        income = this.income,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )
}
