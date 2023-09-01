package bankAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private BankAccountRepository bankAccountRepository;

	@GetMapping("/bank-account/email/{email}")
	public BankAccount getBankAccountByEmail(@PathVariable String email) {
		
		try {
		String port = environment.getProperty("local.server.port");
		BankAccount temp = bankAccountRepository.getBankAccountByEmail(email);
		
		return new BankAccount(temp.getId(), email, temp.getQuantityRSD(), temp.getQuantityEUR(), 
				temp.getQuantityGBP(), temp.getQuantityUSD(),temp.getQuantityCHF(), port);
		}
		catch(Throwable e)
		{
			
			return null;
		}
	}

	@PutMapping("/bank-account")
	public BankAccount updateBankAccount(@RequestBody BankAccount bankAccount) {
		
		if (!bankAccountRepository.existsById(bankAccount.getId())) {
			throw new RuntimeException("Bank account doesn't exist!");
		}
		
		bankAccountRepository.save(bankAccount);
		String port = environment.getProperty("local.server.port");
		bankAccount.setEnvironment(port);
		return bankAccount;
	}
	
}
