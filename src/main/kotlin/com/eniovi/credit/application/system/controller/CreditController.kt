package com.eniovi.credit.application.system.controller

import com.eniovi.credit.application.system.dto.CreditDto
import com.eniovi.credit.application.system.dto.CreditView
import com.eniovi.credit.application.system.entity.Credit
import com.eniovi.credit.application.system.service.ICreditService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("api/credits")
class CreditController(
    private val creditService: ICreditService
) {

    @PostMapping
    fun save(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<CreditView> {
        val creditSaved: Credit = creditService.save(creditDto.toCredit())
        return ResponseEntity.status(HttpStatus.CREATED).body(CreditView(creditSaved))
    }

    @GetMapping("{customerId}")
    fun findAllByCustomerId(@PathVariable customerId: Long): ResponseEntity<List<CreditView>> {
        val creditList: List<Credit> = creditService.findAllByCustomer(customerId)
        val list: List<CreditView> =
            creditList.stream().map { credit: Credit -> CreditView(credit) }.collect(Collectors.toList())
        return ResponseEntity.ok(list)
    }

    @GetMapping
    fun findByCreditCode(
        @RequestParam(value = "creditCode") creditCode: UUID,
        @RequestParam(value = "customerId") customerId: Long
    ): ResponseEntity<CreditView> {
        val credit: Credit = creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.ok(CreditView(credit))
    }

}