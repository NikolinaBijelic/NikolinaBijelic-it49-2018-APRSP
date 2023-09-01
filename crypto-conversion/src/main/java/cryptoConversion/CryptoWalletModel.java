package cryptoConversion;

import java.math.BigDecimal;

public class CryptoWalletModel {

	private long id;
	private String email;
	private BigDecimal quantityBTC;
	private BigDecimal quantityETH;
	private BigDecimal quantityBNB;
	private String environment;

	public CryptoWalletModel() {
		super();
	}

	public CryptoWalletModel(long id, String email, BigDecimal quantityBTC, BigDecimal quantityETH,
			BigDecimal quantityBNB, String environment) {
		super();
		this.id = id;
		this.email = email;
		this.quantityBTC = quantityBTC;
		this.quantityETH = quantityETH;
		this.quantityBNB = quantityBNB;
		this.environment = environment;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getQuantityBTC() {
		return quantityBTC;
	}

	public void setQuantityBTC(BigDecimal quantityBTC) {
		this.quantityBTC = quantityBTC;
	}

	public BigDecimal getQuantityETH() {
		return quantityETH;
	}

	public void setQuantityETH(BigDecimal quantityETH) {
		this.quantityETH = quantityETH;
	}

	public BigDecimal getQuantityBNB() {
		return quantityBNB;
	}

	public void setQuantityBNB(BigDecimal quantityBNB) {
		this.quantityBNB = quantityBNB;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

}