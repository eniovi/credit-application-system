package com.eniovi.credit.application.system.service

import com.eniovi.credit.application.system.dto.CustomerUpdateDto
import com.eniovi.credit.application.system.entity.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(customerId: Long): Customer
    fun delete(customerId: Long)
    fun update(id: Long, customerUpdate: CustomerUpdateDto): Customer
}