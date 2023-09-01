package cryptoExchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CryptoExchangeController {

	@Autowired
	private Environment environment;

	@Autowired
	private CryptoExchangeRepository cryptoExchangeRepository;
	
	@GetMapping("/crypto-exchange/from/{from}/to/{to}")
	public CryptoExchange getExchange(@PathVariable String from, @PathVariable String to) throws Exception {
		
		String port = environment.getProperty("local.server.port");
		CryptoExchange temp = cryptoExchangeRepository.findByFromAndTo(from, to);
		
		if(temp == null) {
			throw new Exception("Exchange of forwarded currencies is not defined!");
		}
		
		return new CryptoExchange(temp.getId(), from, to, temp.getConversionMultiple(), port);
	}
	
}
