package com.binance.coin_futures_client.model.trade;

import com.binance.coin_futures_client.constant.BinanceApiConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class Income {

    private String symbol;

    private String incomeType;

    private BigDecimal income;

    private String asset;

    private String info;

    private Long time;

    private String txId;

    private String tradeId;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getInfo() { return info; }

    public void setInfo(String info) { this.info = info; }

    public String getTxId() { return txId; }

    public void setTxId(String txId) { this.txId = txId; }

    public String getTradeId() { return tradeId; }

    public void setTradeId(String tradeId) { this.tradeId = tradeId; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
                .append("symbol", symbol).append("incomeType", incomeType)
                .append("income", income).append("asset", asset)
                .append("info", info).append("time", time)
                .append("txId", txId).append("tradeId", tradeId)
                .toString();
    }
}
