package com.finzly.csvapplication.model;

import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author USER This class is Margined Rate entity
 *
 */


@Entity
@Table(name = "margined_rates")
public class MarginedRate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "margined_rates_id") 
	private int marginedRatesId;
	
	@Column(name = "date") 
	private Date date;
	
	@Column(name = "currency_name") 
	private String currencyName;
	
	@Column(name = "currency_code") 
	private String currencyCode;
	
	@Column(name = "terms") 
	private String terms;
	
	@Column(name = "internal_spot_rate") 
	private double internalSpotRate;
	
	@Column(name = "buy_rate") 
	private double buyRate;
	
	@Column(name = "sell_rate") 
	private double sellRate;
	
	@Column(name = "boss_rate") 
	private double bossRate;
	
	@Column(name = "cut_off_time") 
	private String cutOffTime;

	
	public MarginedRate() {
	}


	public MarginedRate(Date date, String currencyName, String currencyCode, String terms, double internalSpotRate,
			double buyRate, double sellRate, double bossRate, String cutOffTime) {
		this.date = date;
		this.currencyName = currencyName;
		this.currencyCode = currencyCode;
		this.terms = terms;
		this.internalSpotRate = internalSpotRate;
		this.buyRate = buyRate;
		this.sellRate = sellRate;
		this.bossRate = bossRate;
		this.cutOffTime = cutOffTime;
	}


	public MarginedRate(Date date, String currencyName, String currencyCode, String terms, double internalSpotRate,
			double buyRate, double sellRate, double bossRate) {
		this.date = date;
		this.currencyName = currencyName;
		this.currencyCode = currencyCode;
		this.terms = terms;
		this.internalSpotRate = internalSpotRate;
		this.buyRate = buyRate;
		this.sellRate = sellRate;
		this.bossRate = bossRate;
//		this.cutOffTime = cutOffTime;
	}


	public int getMarginedRatesId() {
		return marginedRatesId;
	}


	public void setMarginedRatesId(int marginedRatesId) {
		this.marginedRatesId = marginedRatesId;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getCurrencyName() {
		return currencyName;
	}


	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}


	public String getCurrencyCode() {
		return currencyCode;
	}


	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}


	public String getTerms() {
		return terms;
	}


	public void setTerms(String terms) {
		this.terms = terms;
	}


	public double getInternalSpotRate() {
		return internalSpotRate;
	}


	public void setInternalSpotRate(double internalSpotRate) {
		this.internalSpotRate = internalSpotRate;
	}


	public double getBuyRate() {
		return buyRate;
	}


	public void setBuyRate(double buyRate) {
		this.buyRate = buyRate;
	}


	public double getSellRate() {
		return sellRate;
	}


	public void setSellRate(double sellRate) {
		this.sellRate = sellRate;
	}


	public double getBossRate() {
		return bossRate;
	}


	public void setBossRate(double bossRate) {
		this.bossRate = bossRate;
	}


	@Override
	public String toString() {
		return "MarginedRates [marginedRatesId=" + marginedRatesId + ", date=" + date + ", currencyName=" + currencyName
				+ ", currencyCode=" + currencyCode + ", terms=" + terms + ", internalSpotRate=" + internalSpotRate
				+ ", buyRate=" + buyRate + ", sellRate=" + sellRate + ", bossRate=" + bossRate + ", cutOffTime="
				+ cutOffTime + "]";
	}


	public String getCutOffTime() {
		return cutOffTime;
	}


	public void setCutOffTime(String cutOffTime) {
		this.cutOffTime = cutOffTime;
	}
	
	
}	


