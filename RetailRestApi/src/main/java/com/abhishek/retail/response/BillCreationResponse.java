package com.abhishek.retail.response;

public class BillCreationResponse {

	private long transId = -1;
	private long invoiceId = -1;
	private long advanceReceiptId = -1;
	private boolean success;

	public BillCreationResponse(){
		
	}
	
	
	public long getAdvanceReceiptId() {
		return advanceReceiptId;
	}


	public void setAdvanceReceiptId(long advanceReceiptId) {
		this.advanceReceiptId = advanceReceiptId;
	}


	public long getTransId() {
		return transId;
	}
	public void setTransId(long transId) {
		this.transId = transId;
	}
	public long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
