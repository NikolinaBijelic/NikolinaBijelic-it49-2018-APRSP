package cryptoTrade;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {

	@GetMapping("/bank-account/email/{email}")
	BankAccountModel getBankAccountByEmail(@PathVariable String email);
		
	@PutMapping("/bank-account")
	BankAccountModel updateBankAccount(@RequestBody BankAccountModel bankAccount);
}