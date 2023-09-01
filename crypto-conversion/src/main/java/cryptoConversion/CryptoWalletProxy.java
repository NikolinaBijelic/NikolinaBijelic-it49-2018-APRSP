package cryptoConversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {

	@GetMapping("/crypto-wallet/email/{email}")
	CryptoWalletModel getCryptoWalletByEmail(@PathVariable String email);
			
	@PutMapping("/crypto-wallet")
	CryptoWalletModel updateCryptoWallet(@RequestBody CryptoWalletModel cryptoWallet);
		
}
