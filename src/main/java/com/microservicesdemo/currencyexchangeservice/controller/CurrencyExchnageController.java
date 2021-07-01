package com.microservicesdemo.currencyexchangeservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.microservicesdemo.currencyexchangeservice.bean.CurrencyExchange;
import com.microservicesdemo.currencyexchangeservice.proxy.CrudOperationProxy;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CurrencyExchnageController {
	@Autowired
	CrudOperationProxy crudOperationProxy;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public Optional<CurrencyExchange> retrieveExchangeValuebyProxy(@PathVariable String from,@PathVariable String to) {
		String port =  env.getProperty("local.server.port");
		Optional<CurrencyExchange> ce = crudOperationProxy.findByFromAndTo(from, to);
		ce.ifPresent((c)->c.setEnvironment(port));
		return ce;
	}
	@GetMapping("/getAll-currency-mapping")
	//@Retry(name = "default", fallbackMethod = "hardcodedResponse")
	@CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse" )
	public List<CurrencyExchange> retrieveAll(){
		String port =  env.getProperty("local.server.port");
		 List<CurrencyExchange> c = crudOperationProxy.retrieveAll();
		 c.forEach((currency)->currency.setEnvironment(currency.getEnvironment() + " ->" + "crud-services:" + port));
		 return c;
	}
	@GetMapping("/get-by-id/{id}")
	public  Optional<CurrencyExchange> findById(@PathVariable Long id){
		String port =  env.getProperty("local.server.port");
		String path = "/get-by-id/" + id;
		Optional<CurrencyExchange> c = crudOperationProxy.findById(id);
		c.ifPresent((ce)->ce.setEnvironment(ce.getEnvironment() + " -> " + "currency-exchange-service: " + port ));
		return c;
	}
	
	@PostMapping("/add-currency-mapping")
	public CurrencyExchange save(@RequestBody CurrencyExchange currencyExchange) {
		String port =  env.getProperty("local.server.port");
		CurrencyExchange ce = crudOperationProxy.save(currencyExchange);
		ce.setEnvironment( ce.getEnvironment()+ " -> " + "currency-exchange-service:" + port);
		return ce;
	}
	
	@PostMapping("/update-currency-mapping")
	public CurrencyExchange updateCurrencyMapping(@RequestBody CurrencyExchange currencyExchange) {
		String port =  env.getProperty("local.server.port");
		CurrencyExchange ce = crudOperationProxy.updateCurrencyMapping(currencyExchange);
		ce.setEnvironment( ce.getEnvironment()+ " -> " + "currency-exchange-service:" + port);
		return ce;
	}
	
	@PostMapping("/delete-by-id/{id}")
	public void deleteById(@PathVariable Long id) {
		crudOperationProxy.deleteById(id);
	}
	
	
	public List hardcodedResponse(java.lang.Throwable e) {
		String error = "Error Occurred in Crud-Operation microservice";
		List l  = new ArrayList<>();
		l.add(error);
		return l;
	}
	
	
//	 interface java.util.List 
//	 class com.microservicesdemo.currencyexchangeservice.controller.CurrencyExchnageController.hardcodedResponse(,class java.lang.Throwable)
}
