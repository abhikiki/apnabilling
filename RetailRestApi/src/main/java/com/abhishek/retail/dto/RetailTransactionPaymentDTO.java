package com.abhishek.retail.dto;

public class RetailTransactionPaymentDTO {

	private long transId = -1;
	private double totalCardPayment = 0.00;
	private double cashPayment = 0.00;
	private double chequePayment = 0.00;
	private double neftPayment = 0.00;
	private double rtgsPayment = 0.00;
	
	public RetailTransactionPaymentDTO(){
		
	}

	public long getTransId() {
		return transId;
	}
	public void setTransId(long transId) {
		this.transId = transId;
	}
	
	public double getCashPayment() {
		return cashPayment;
	}

	public void setCashPayment(double cashPayment) {
		this.cashPayment = cashPayment;
	}
	
	public double getTotalCardPayment() {
		return totalCardPayment;
	}
	public void setTotalCardPayment(double totalCardPayment) {
		this.totalCardPayment = totalCardPayment;
	}
	public double getChequePayment() {
		return chequePayment;
	}
	public void setChequePayment(double chequePayment) {
		this.chequePayment = chequePayment;
	}
	public double getNeftPayment() {
		return neftPayment;
	}
	public void setNeftPayment(double neftPayment) {
		this.neftPayment = neftPayment;
	}
	public double getRtgsPayment() {
		return rtgsPayment;
	}
	public void setRtgsPayment(double rtgsPayment) {
		this.rtgsPayment = rtgsPayment;
	}
	
	
}
