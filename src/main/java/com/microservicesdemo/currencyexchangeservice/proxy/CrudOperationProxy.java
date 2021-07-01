package com.microservicesdemo.currencyexchangeservice.proxy;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.microservicesdemo.currencyexchangeservice.bean.CurrencyExchange;

@FeignClient(name = "crud-services")
public interface CrudOperationProxy {
	
	@GetMapping("/getAll-currency-mapping")
	public List<CurrencyExchange> retrieveAll();
	
	@GetMapping("/get-by-id/{id}")
	public  Optional<CurrencyExchange> findById(@PathVariable Long id);
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public Optional<CurrencyExchange>  findByFromAndTo(@PathVariable String from, @PathVariable String to);
	
	@PostMapping("/add-currency-mapping")
	public CurrencyExchange save(@RequestBody CurrencyExchange currencyExchange);
	
	@PostMapping("/update-currency-mapping")
	public CurrencyExchange updateCurrencyMapping(@RequestBody CurrencyExchange currencyExchange);
	
	@PostMapping("/delete-by-id/{id}")
	public void deleteById(@PathVariable Long id);

}


