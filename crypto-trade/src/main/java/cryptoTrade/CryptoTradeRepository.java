package cryptoTrade;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoTradeRepository extends JpaRepository<CryptoTrade, Long> {

	CryptoTrade findByFromAndTo(String from, String to);
	
}

