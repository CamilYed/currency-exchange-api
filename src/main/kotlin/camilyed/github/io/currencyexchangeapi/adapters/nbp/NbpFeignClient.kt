package camilyed.github.io.currencyexchangeapi.adapters.nbp

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import java.math.BigDecimal

@FeignClient(name = "nbpClient", url = "\${nbp.url}")
interface NbpFeignClient {
    @GetMapping
    fun getUsdToPlnRate(): NbpExchangeRateResponse
}

data class NbpExchangeRateResponse(
    val rates: List<Rate>,
)

data class Rate(
    val mid: BigDecimal,
)
