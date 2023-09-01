package cryptoTrade;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.thoughtworks.xstream.converters.time.SystemClockConverter;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;


@RestController
public class CryptoTradeController {
	
	@Autowired
	private CryptoTradeRepository cryptoTradeRepository;
	
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;
	
	@Retry(name = "sample-api", fallbackMethod = "circuitBreakerError")
    @RateLimiter(name = "default")
	@Bulkhead(name = "default")
	@GetMapping("/crypto-trade/from/{from}/to/{to}/quantity/{quantity}/user/{email}")
	public Object cryptoTrade(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity, @PathVariable String email) throws Exception {
	try {
		if(from.equals("EUR")|| from.equals("USD")) {
			return currencyToCrypto(from,to,quantity,email);
		}else if(from.equals("RSD") || from.equals("GBP") || from.equals("CHF")) {
			return currencyToEURThenCrypto(from,to,quantity,email);
		}else if((from.equals("BTC") || from.equals("ETH") || from.equals("BNB")) && (to.equals("EUR") || to.equals("USD"))) {
		return cryptoToCurrency(from,to,quantity,email);
	}else if((from.equals("BTC") || from.equals("ETH") || from.equals("BNB")) && (to.equals("RSD") || to.equals("GBP") || to.equals("CHF"))) {
		return cryptoToEURCurrency(from,to,quantity,email);
	}else {
		throw new Exception("Unknown currency");
	}
	}catch(Exception e) {
		return e.getMessage();
	}
		
	}
	
	public CryptoWalletModel currencyToCrypto(String from, String to, BigDecimal quantity, String email) throws Exception {
		BankAccountModel bankAcc;
		CryptoWalletModel cryptoW;
		try {
			bankAcc = bankAccountProxy.getBankAccountByEmail(email);
			cryptoW = cryptoWalletProxy.getCryptoWalletByEmail(email);
			
			if(bankAcc==null || cryptoW==null) {
				throw new Exception();
			}
		}catch(Exception e) {
			throw new Exception("User doesn't exist!");
		}
		
		
		CryptoTrade cryptoTrade = cryptoTradeRepository.findByFromAndTo(from, to);
		
		
		
		if(from.equals("EUR")) {
		if(bankAcc.getQuantityEUR().compareTo(quantity)>0) {
			bankAcc.setQuantityEUR(bankAcc.getQuantityEUR().subtract(quantity));
			bankAccountProxy.updateBankAccount(bankAcc);
			
		}else{
			throw new Exception("Insufficient funds!");
			
		}
		}else{
			if(bankAcc.getQuantityUSD().compareTo(quantity)>0) {
				bankAcc.setQuantityUSD(bankAcc.getQuantityUSD().subtract(quantity));
				
			}else {
				throw new Exception("Insufficient funds!");
				
			}
		}
		
		if(to.equals("BTC")) {
			cryptoW.setQuantityBTC(cryptoW.getQuantityBTC().add(quantity.multiply(cryptoTrade.getConversionMultiple())));
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}
		else if(to.equals("ETH")) {
			cryptoW.setQuantityBTC(cryptoW.getQuantityETH().add(quantity.multiply(cryptoTrade.getConversionMultiple())));
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}
		else if(to.equals("BNB")) {
			cryptoW.setQuantityBNB(cryptoW.getQuantityBNB().add(quantity.multiply(cryptoTrade.getConversionMultiple())));
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}
		return cryptoW;
	}
	
	
public CryptoWalletModel currencyToEURThenCrypto(String from, String to, BigDecimal quantity, String email) throws Exception {
		
	BankAccountModel bankAcc = new BankAccountModel();
	CryptoWalletModel cryptoW = new CryptoWalletModel();
	try {
		bankAcc = bankAccountProxy.getBankAccountByEmail(email);
		cryptoW = cryptoWalletProxy.getCryptoWalletByEmail(email);
		if(bankAcc==null || cryptoW==null) {
			throw new Exception();
		}
	}catch(Exception e) {
		throw new Exception("User doesn't exist!");
	}
	
	CurrencyExchangeModel currencyExchange = new CurrencyExchangeModel();
	BigDecimal q;
		
	if(from.equals("RSD")) {
		    currencyExchange = currencyExchangeProxy.getExchange("RSD", "EUR");
			if(bankAcc.getQuantityRSD().compareTo(quantity)>0) {
				bankAcc.setQuantityRSD(bankAcc.getQuantityRSD().subtract(quantity));
				bankAccountProxy.updateBankAccount(bankAcc);
				q = quantity.multiply(currencyExchange.getConversionMultiple());
				
			}else {
				throw new Exception("Insufficient funds!");
			}
		
	}else if(from.equals("GBP")) {
		 currencyExchange = currencyExchangeProxy.getExchange("GBP", "EUR");
		 if(bankAcc.getQuantityGBP().compareTo(quantity)>0) {
				bankAcc.setQuantityGBP(bankAcc.getQuantityGBP().subtract(quantity));
				bankAccountProxy.updateBankAccount(bankAcc);
				q = quantity.multiply(currencyExchange.getConversionMultiple());
				
			}else {
				throw new Exception("Insufficient funds!");
			}
	}else if(from.equals("CHF")) {
		currencyExchange = currencyExchangeProxy.getExchange("CHF", "EUR");
		 if(bankAcc.getQuantityCHF().compareTo(quantity)>0) {
				bankAcc.setQuantityCHF(bankAcc.getQuantityCHF().subtract(quantity));
				bankAccountProxy.updateBankAccount(bankAcc);
				q = quantity.multiply(currencyExchange.getConversionMultiple());
			}else {
				throw new Exception("Insufficient funds!");
			}
	}
	
	
	
	
		CryptoTrade cryptoTrade = cryptoTradeRepository.findByFromAndTo("EUR", to);
		
	
		if(to.equals("BTC")) {
			cryptoW.setQuantityBTC(cryptoW.getQuantityBTC().add(quantity.multiply(cryptoTrade.getConversionMultiple())));
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}
		else if(to.equals("ETH")) {
			cryptoW.setQuantityBTC(cryptoW.getQuantityETH().add(quantity.multiply(cryptoTrade.getConversionMultiple())));
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}
		else if(to.equals("BNB")) {
			cryptoW.setQuantityBNB(cryptoW.getQuantityBNB().add(quantity.multiply(cryptoTrade.getConversionMultiple())));
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}
		return cryptoW;
	}

public BankAccountModel cryptoToCurrency(String from, String to, BigDecimal quantity, String email) throws Exception {
	
	BankAccountModel bankAcc = new BankAccountModel();
	CryptoWalletModel cryptoW = new CryptoWalletModel();
	try {
		bankAcc = bankAccountProxy.getBankAccountByEmail(email);
		cryptoW = cryptoWalletProxy.getCryptoWalletByEmail(email);
		if(bankAcc==null || cryptoW==null) {
			throw new Exception();
		}
	}catch(Exception e) {
		throw new Exception("User doesn't exist!");
	}
	
	CryptoTrade cryptoTrade = cryptoTradeRepository.findByFromAndTo(from, to);
	
	if(from.equals("BTC")) {
	if(cryptoW.getQuantityBTC().compareTo(quantity)>0) {
		cryptoW.setQuantityBTC(cryptoW.getQuantityBTC().subtract(quantity));
		cryptoWalletProxy.updateCryptoWallet(cryptoW);
		
	}else {
		throw new Exception("Insufficient funds!");
	}
	}else if(from.equals("ETH")){
		if(cryptoW.getQuantityETH().compareTo(quantity)>0) {
			cryptoW.setQuantityETH(cryptoW.getQuantityETH().subtract(quantity));
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}else {
			throw new Exception("Insufficient funds!");
		}
	}else {
		if(cryptoW.getQuantityBNB().compareTo(quantity)>0) {
			cryptoW.setQuantityBNB(cryptoW.getQuantityBNB().subtract(quantity));
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}else {
			throw new Exception("Insufficient funds!");
		}
	}
	
	
	if(to.equals("EUR")) {
		
		bankAcc.setQuantityEUR(bankAcc.getQuantityEUR().add(quantity.multiply(cryptoTrade.getConversionMultiple())));
		bankAccountProxy.updateBankAccount(bankAcc);
		
	}
	else {
		
		bankAcc.setQuantityUSD(bankAcc.getQuantityUSD().add(quantity.multiply(cryptoTrade.getConversionMultiple())));
		bankAccountProxy.updateBankAccount(bankAcc);
		
	}
	return bankAcc;
}


public BankAccountModel cryptoToEURCurrency(String from, String to, BigDecimal quantity, String email) throws Exception {
	
	BankAccountModel bankAcc = new BankAccountModel();
	CryptoWalletModel cryptoW = new CryptoWalletModel();
	try {
		bankAcc = bankAccountProxy.getBankAccountByEmail(email);
		cryptoW = cryptoWalletProxy.getCryptoWalletByEmail(email);
		if(bankAcc==null || cryptoW==null) {
			throw new Exception();
		}
	}catch(Exception e) {
		throw new Exception("User doesn't exist!");
	}
	
	CurrencyExchangeModel currencyExchange = new CurrencyExchangeModel();
	BigDecimal q;
		
	
	CryptoTrade cryptoTrade = cryptoTradeRepository.findByFromAndTo(from, "EUR"); //kripto u evre
	q = quantity.multiply(cryptoTrade.getConversionMultiple()); //kripto u evre
	

		
	if(from.equals("BTC")) {
		if(cryptoW.getQuantityBTC().compareTo(quantity)>0) {
			cryptoW.setQuantityBTC(cryptoW.getQuantityBTC().subtract(quantity)); //skidam kripto
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}else {
			throw new Exception("Insufficient funds!");
		}
	}else if(from.equals("ETH")) {

		if(cryptoW.getQuantityETH().compareTo(quantity)>0) {
			cryptoW.setQuantityBTC(cryptoW.getQuantityETH().subtract(quantity));
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}else {
			throw new Exception("Insufficient funds!");
		}
	}else if(from=="BNB") {
		if(cryptoW.getQuantityBNB().compareTo(q)>0) {
			cryptoW.setQuantityBNB(cryptoW.getQuantityBNB().subtract(quantity));
			cryptoWalletProxy.updateCryptoWallet(cryptoW);
			
		}else {
			throw new Exception("Insufficient funds!");
		}
		
	}
		
		
	
		if(to.equals("RSD")) {
			
			CurrencyExchangeModel ce = currencyExchangeProxy.getExchange("EUR", "RSD");
			bankAcc.setQuantityRSD(bankAcc.getQuantityRSD().add(q.multiply(ce.getConversionMultiple()))); //dodajem zeljenu valutu
			bankAccountProxy.updateBankAccount(bankAcc);
		
		}
		else if(to.equals("GBP")) {
			
			CurrencyExchangeModel ce = currencyExchangeProxy.getExchange("EUR", "GBP");
			bankAcc.setQuantityGBP(bankAcc.getQuantityGBP().add(q.multiply(ce.getConversionMultiple())));
			bankAccountProxy.updateBankAccount(bankAcc);
			
		}
		else if(to.equals("CHF")) {
			
			CurrencyExchangeModel ce = currencyExchangeProxy.getExchange("EUR", "CHF");
			bankAcc.setQuantityCHF(bankAcc.getQuantityCHF().add(q.multiply(ce.getConversionMultiple())));
			bankAccountProxy.updateBankAccount(bankAcc);
			
		}
		
		return bankAcc;
	}

public String circuitBreakerError(Exception e) {
	return "Circuit breaker error: " + e.getMessage();
}
}

