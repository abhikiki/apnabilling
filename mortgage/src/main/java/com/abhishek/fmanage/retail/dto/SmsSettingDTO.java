package com.abhishek.fmanage.retail.dto;

public class SmsSettingDTO {

	private String smsUserId = "";
	private String smsPassword = "";
	private String smsSenderId = "";
	private String smsGatewayUrl = "";
	
	public SmsSettingDTO(){}
	
	public String getSmsUserId() {
		return smsUserId;
	}
	public void setSmsUserId(String smsUserId) {
		this.smsUserId = smsUserId;
	}
	public String getSmsPassword() {
		return smsPassword;
	}
	public void setSmsPassword(String smsPassword) {
		this.smsPassword = smsPassword;
	}
	public String getSmsSenderId() {
		return smsSenderId;
	}
	public void setSmsSenderId(String smsSenderId) {
		this.smsSenderId = smsSenderId;
	}
	public String getSmsGatewayUrl() {
		return smsGatewayUrl;
	}
	public void setSmsGatewayUrl(String smsGatewayUrl) {
		this.smsGatewayUrl = smsGatewayUrl;
	}
}
