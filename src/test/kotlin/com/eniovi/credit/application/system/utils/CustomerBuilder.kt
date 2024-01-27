package com.eniovi.credit.application.system.utils

import com.eniovi.credit.application.system.dto.CustomerDto
import com.eniovi.credit.application.system.dto.CustomerUpdateDto
import java.math.BigDecimal

class CustomerBuilder {
    fun builderCustomerDto(
        firstName: String = "Enio",
        lastName: String = "Santos",
        cpf: String = "345.091.580-09",
        email: String = "enio@teste.com.br",
        income: BigDecimal = BigDecimal.valueOf(1000.00),
        password: String = "123456",
        street: String = "Rua do Enio",
        zipCode: String = "12345"
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        zipCode = zipCode,
        street = street
    )

    fun builderCustomerUpdateDto(
        firstName: String = "Eniovi",
        lastName: String = "Santos Atualizado",
        income: BigDecimal = BigDecimal.valueOf(2000.00),
        street: String = "Rua do Enio Atualizada",
        zipCode: String = "123456"
    ) = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )
}