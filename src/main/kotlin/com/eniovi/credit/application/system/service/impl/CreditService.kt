package com.eniovi.credit.application.system.service.impl

import com.eniovi.credit.application.system.entity.Credit
import com.eniovi.credit.application.system.repository.CreditRepository
import com.eniovi.credit.application.system.service.ICreditService
import com.eniovi.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.UUID

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: ICustomerService
): ICreditService {

    override fun save(credit: Credit): Credit {
        credit.apply { customer = customerService.findById(credit.customer?.id!!) }
        return creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> =
        creditRepository.findAllByCustomerId(customerId)

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit =
        creditRepository.findByCustomerIdAndCreditCode(customerId, creditCode) ?:
        throw RuntimeException("Credit code $creditCode not found")
}