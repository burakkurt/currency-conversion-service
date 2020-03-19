package com.burak.microservices.currencyconversionservice.service;

import com.burak.microservices.currencyconversionservice.model.CurrencyConversionModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name="currency-exchange-service", url="localhost:8000")
public interface CurrencyExchangeServiceProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversionModel retrieveExchangeValue(@PathVariable String from, @PathVariable String to);

}
