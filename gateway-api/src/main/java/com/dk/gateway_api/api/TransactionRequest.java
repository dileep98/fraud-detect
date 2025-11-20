package com.dk.gateway_api.api;

import java.math.BigDecimal;

public class TransactionRequest {

    private String txId;
    private String accountId;
    private String merchantId;
    private BigDecimal amount;
    private String currency;
    private String ip;
    private String deviceId;
    private String channel;

    public String getTxId() { return txId; }
    public void setTxId(String txId) { this.txId = txId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
}
