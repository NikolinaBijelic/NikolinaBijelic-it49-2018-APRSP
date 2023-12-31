package bankAccount;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class BankAccount {

	@Id
	private long id;

	@Column(unique = true)
	private String email;
	
	@Column(name = "quantity_RSD")
	private BigDecimal quantityRSD;
	
	@Column(name = "quantity_EUR")
	private BigDecimal quantityEUR;
	
	@Column(name = "quantity_GBP")
	private BigDecimal quantityGBP;
	
	@Column(name = "quantity_USD")
	private BigDecimal quantityUSD;
	
	@Column(name = "quantity_CHF")
	private BigDecimal quantityCHF;

	@Transient
	private String environment;

	public BankAccount() {
		super();
	}

	public BankAccount(long id, String email, BigDecimal quantityRSD, BigDecimal quantityEUR, BigDecimal quantityGBP,
			BigDecimal quantityUSD, BigDecimal quantityCHF, String environment) {
		super();
		this.id = id;
		this.email = email;
		this.quantityRSD = quantityRSD;
		this.quantityEUR = quantityEUR;
		this.quantityGBP = quantityGBP;
		this.quantityUSD = quantityUSD;
		this.quantityCHF = quantityCHF;
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

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
}
