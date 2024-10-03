package camilyed.github.io.currencyexchangeapi.testing.abilties

import camilyed.github.io.currencyexchangeapi.testing.builders.CreateAccountJsonBuilder
import org.springframework.http.ResponseEntity

interface CreateAccountAbility : MakeRequestAbility {
    fun createAccount(builder: CreateAccountJsonBuilder): ResponseEntity<String> {
        val accountJson = builder.build()
        return post("/api/accounts", accountJson, String::class.java)
    }
}
