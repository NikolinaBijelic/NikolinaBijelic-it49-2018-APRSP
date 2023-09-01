package cryptoTrade;

import java.math.BigDecimal;

public class BankAccountModel {

	private long id;
	private String email;
	private BigDecimal quantityRSD;
	private BigDecimal quantityEUR;
	private BigDecimal quantityGBP;
	private BigDecimal quantityUSD;
	private BigDecimal quantityCHF;

	public BankAccountModel() {
		super();
	}

	public BankAccountModel(long id, String email, BigDecimal quantityRSD, BigDecimal quantityEUR, BigDecimal quantityGBP,
			BigDecimal quantityUSD, BigDecimal quantityCHF) {
		super();
		this.id = id;
		this.email = email;
		this.quantityRSD = quantityRSD;
		this.quantityEUR = quantityEUR;
		this.quantityGBP = quantityGBP;
		this.quantityUSD = quantityUSD;
		this.quantityCHF = quantityCHF;
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

	public BigDecimal getQuantityRSD() {
		return quantityRSD;
	}

	public void setQuantityRSD(BigDecimal quantityRSD) {
		this.quantityRSD = quantityRSD;
	}

	public BigDecimal getQuantityEUR() {
		return quantityEUR;
	}

	public void setQuantityEUR(BigDecimal quantityEUR) {
		this.quantityEUR = quantityEUR;
	}

	public BigDecimal getQuantityGBP() {
		return quantityGBP;
	}

	public void setQuantityGBP(BigDecimal quantityGBP) {
		this.quantityGBP = quantityGBP;
	}

	public BigDecimal getQuantityUSD() {
		return quantityUSD;
	}

	public void setQuantityUSD(BigDecimal quantityUSD) {
		this.quantityUSD = quantityUSD;
	}

	public BigDecimal getQuantityCHF() {
		return quantityCHF;
	}

	public void setQuantityCHF(BigDecimal quantityCHF) {
		this.quantityCHF = quantityCHF;
	}
	
}

