package com.eniovi.credit.application.system.service.impl

import com.eniovi.credit.application.system.entity.Customer
import com.eniovi.credit.application.system.repository.CustomerRepository
import com.eniovi.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
): ICustomerService {

    override fun save(customer: Customer): Customer =
        customerRepository.save(customer)

    override fun findById(customerId: Long): Customer =
        customerRepository.findById(customerId)
            .orElseThrow { throw RuntimeException("Id $customerId not found") }

    override fun delete(customerId: Long) =
        customerRepository.deleteById(customerId)

    override fun update(customer: Customer): Customer {
        customerRepository.findById(customer.id!!)
        return save(customer)
    }
}