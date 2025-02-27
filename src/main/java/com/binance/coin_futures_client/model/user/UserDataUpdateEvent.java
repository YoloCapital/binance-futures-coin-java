package com.binance.coin_futures_client.model.user;

import com.binance.coin_futures_client.constant.BinanceApiConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserDataUpdateEvent {

    private String eventType;

    private Long eventTime;

    private Long transactionTime;

    private AccountUpdate accountUpdate;

    private OrderUpdate orderUpdate;

    private MarginCallUpdate marginCallUpdate;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public Long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public AccountUpdate getAccountUpdate() {
        return accountUpdate;
    }

    public void setAccountUpdate(AccountUpdate accountUpdate) {
        this.accountUpdate = accountUpdate;
    }

    public OrderUpdate getOrderUpdate() {
        return orderUpdate;
    }

    public void setOrderUpdate(OrderUpdate orderUpdate) {
        this.orderUpdate = orderUpdate;
    }

    public MarginCallUpdate getMarginCallUpdate() { return marginCallUpdate; }

    public void setMarginCallUpdate(MarginCallUpdate marginCallUpdate) { this.marginCallUpdate = marginCallUpdate; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE).append("eventType", eventType)
                .append("eventTime", eventTime).append("accountUpdate", accountUpdate)
                .append("orderUpdate", orderUpdate).append("marginCallUpdate", marginCallUpdate).toString();
    }
}
