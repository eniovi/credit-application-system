package com.eniovi.credit.application.system.controller

import com.eniovi.credit.application.system.dto.CustomerDto
import com.eniovi.credit.application.system.dto.CustomerUpdateDto
import com.eniovi.credit.application.system.dto.CustomerView
import com.eniovi.credit.application.system.entity.Customer
import com.eniovi.credit.application.system.service.ICustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/customers")
class CustomerController(
    private val customerService: ICustomerService
) {

    @PostMapping
    fun save(@RequestBody @Valid customerDto: CustomerDto): ResponseEntity<String> {
        val savedCustomer: Customer = customerService.save(customerDto.toCustomer())
        val response = "Customer ${savedCustomer.email} saved!"
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerView> {
        val customer: Customer = customerService.findById(id)
        return ResponseEntity.ok(CustomerView(customer))
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = customerService.delete(id)

    @PatchMapping("{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid customerUpdateDto: CustomerUpdateDto
    ): ResponseEntity<CustomerView> {
        val customerUpdated: Customer = customerService.update(id, customerUpdateDto)
        return ResponseEntity.ok(CustomerView(customerUpdated))
    }
}