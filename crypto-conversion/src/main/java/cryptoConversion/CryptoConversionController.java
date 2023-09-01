package cryptoConversion;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
public class CryptoConversionController {

	//@Autowired
	private CryptoExchangeProxy cryptoExchangeProxy;
	
	//@Autowired
	private CryptoWalletProxy cryptoWalletProxy;
	
	@RateLimiter(name = "default")
	@GetMapping("/crypto-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CryptoConversion getConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) throws Exception {
		
		HashMap<String,String> uriVariables = new HashMap<>();
		uriVariables.put("from",from);
		uriVariables.put("to", to);
		
		ResponseEntity<CryptoConversion> response = new RestTemplate().getForEntity
				("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CryptoConversion.class, uriVariables);
		
		CryptoConversion temp = response.getBody();
		
		return new CryptoConversion(temp.getId(), from, to, temp.getConversionMultiple(), quantity, quantity.multiply(temp.getConversionMultiple()), temp.getEnvironment());
	}
	
	@GetMapping("/crypto-conversion/from/{from}/to/{to}/quantity/{quantity}/user/{email}")
	@RateLimiter(name = "default")
	public CryptoWalletModel getConversionByUserEmail(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity, @PathVariable String email) {
		
		CryptoWalletModel cryptoWallet = cryptoWalletProxy.getCryptoWalletByEmail(email);	
		CryptoConversion cryptoConversion = cryptoExchangeProxy.getExchange(from, to);
		
		if(from.equals("BTC")) {
			if(quantity.compareTo(cryptoWallet.getQuantityBTC()) > 0) 
				throw new RuntimeException("User doesn't have enough " + from + " in the crypto wallet.");
			cryptoWallet.setQuantityBTC(cryptoWallet.getQuantityBTC().subtract(quantity));
		} 
		else if(from.equals("ETH")) {
			if(quantity.compareTo(cryptoWallet.getQuantityETH()) > 0) 
				throw new RuntimeException("User doesn't have enough " + from + " in the crypto wallet.");
			cryptoWallet.setQuantityETH(cryptoWallet.getQuantityETH().subtract(quantity));
		} 
		else if(from.equals("BNB")) {
			if(quantity.compareTo(cryptoWallet.getQuantityBNB()) > 0) 
				throw new RuntimeException("User doesn't have enough " + from + " in the crypto wallet.");
			cryptoWallet.setQuantityBNB(cryptoWallet.getQuantityBNB().subtract(quantity));
		} 
		 
		
		if (to.equals("BTC")) {
			cryptoWallet.setQuantityBTC(cryptoWallet.getQuantityBTC().add(quantity.multiply(cryptoConversion.getConversionMultiple())));
		} 
		else if (to.equals("ETH")) {
			cryptoWallet.setQuantityETH(cryptoWallet.getQuantityETH().add(quantity.multiply(cryptoConversion.getConversionMultiple())));
		}
		else if (to.equals("BNB")) {
			cryptoWallet.setQuantityBNB(cryptoWallet.getQuantityBNB().add(quantity.multiply(cryptoConversion.getConversionMultiple())));
		} 
		
		return cryptoWalletProxy.updateCryptoWallet(cryptoWallet);
	}
}

