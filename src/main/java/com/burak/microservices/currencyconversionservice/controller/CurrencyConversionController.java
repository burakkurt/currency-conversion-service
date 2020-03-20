package com.burak.microservices.currencyconversionservice.controller;

import com.burak.microservices.currencyconversionservice.model.CurrencyConversionModel;
import com.burak.microservices.currencyconversionservice.service.CurrencyExchangeServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

    @Autowired
    private CurrencyExchangeServiceProxy proxy;

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionModel convertCurrency(@PathVariable String from,
                                                   @PathVariable String to,
                                                   @PathVariable BigDecimal quantity){

        Map<String, String> uriMap = new HashMap<>();
        uriMap.put("from", from);
        uriMap.put("to", to);

        ResponseEntity<CurrencyConversionModel> responseEntity = new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversionModel.class,
                uriMap);

        CurrencyConversionModel response = responseEntity.getBody();

        return new CurrencyConversionModel(response.getId(), from, to, response.getConversionMultiple(), quantity,
                quantity.multiply(response.getConversionMultiple()), response.getPort());
    }

    @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionModel convertCurrencyFeign(@PathVariable String from,
                                                        @PathVariable String to,
                                                        @PathVariable BigDecimal quantity){

        CurrencyConversionModel response = proxy.retrieveExchangeValue(from, to);

        logger.info("{}", response);

        return new CurrencyConversionModel(response.getId(), from, to, response.getConversionMultiple(), quantity,
                quantity.multiply(response.getConversionMultiple()), response.getPort());
    }

}
