package com.eniovi.credit.application.system.controller

import com.eniovi.credit.application.system.dto.CustomerDto
import com.eniovi.credit.application.system.dto.CustomerUpdateDto
import com.eniovi.credit.application.system.dto.CustomerView
import com.eniovi.credit.application.system.entity.Customer
import com.eniovi.credit.application.system.service.impl.CustomerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @PostMapping
    fun save(@RequestBody customerDto: CustomerDto): String {
        val savedCustomer: Customer = customerService.save(customerDto.toCustomer())
        return "Customer ${savedCustomer.email} saved!"
    }

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long): CustomerView {
        val customer: Customer = customerService.findById(id)
        return CustomerView(customer)
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = customerService.delete(id)

    @PatchMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody customerUpdateDto: CustomerUpdateDto): CustomerView {
        val customerUpdated: Customer = customerService.update(id, customerUpdateDto)
        return CustomerView(customerUpdated)
    }
}