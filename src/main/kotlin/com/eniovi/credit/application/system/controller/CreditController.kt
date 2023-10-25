package com.eniovi.credit.application.system.controller

import com.eniovi.credit.application.system.dto.CreditDto
import com.eniovi.credit.application.system.dto.CreditView
import com.eniovi.credit.application.system.entity.Credit
import com.eniovi.credit.application.system.service.impl.CreditService
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("api/credits")
class CreditController(
    private val creditService: CreditService
) {

    @PostMapping
    fun save(@RequestBody creditDto: CreditDto): String {
        val creditSaved: Credit = creditService.save(creditDto.toCredit())
        return "Credit ${creditSaved.creditCode} - Customer ${creditSaved.customer?.firstName} saved!"
    }

    @GetMapping("{customerId}")
    fun findAllByCustomerId(@PathVariable customerId: Long): List<CreditView> {
        val creditList = creditService.findAllByCustomer(customerId)
        return creditList.stream().map { credit: Credit -> CreditView(credit) }.collect(Collectors.toList())
    }

    @GetMapping("{creditCode}")
    fun findByCreditCode(@PathVariable creditCode: UUID, @RequestParam(value = "customerId") customerId: Long): CreditView {
        val credit: Credit = creditService.findByCreditCode(customerId, creditCode)
        return CreditView(credit)
    }

}