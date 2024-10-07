package camilyed.github.io.currencyexchangeapi.adapters.nbp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import feign.RequestLine
import java.math.BigDecimal

interface NbpFeignClient {
    @RequestLine("GET")
    fun getUsdToPlnRate(): NbpExchangeRateResponse
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class NbpExchangeRateResponse(
    val rates: List<Rate>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Rate(
    val mid: BigDecimal,
)
