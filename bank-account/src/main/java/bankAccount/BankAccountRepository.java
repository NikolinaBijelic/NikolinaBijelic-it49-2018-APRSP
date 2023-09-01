package bankAccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
	
	BankAccount getBankAccountByEmail(String email);
}