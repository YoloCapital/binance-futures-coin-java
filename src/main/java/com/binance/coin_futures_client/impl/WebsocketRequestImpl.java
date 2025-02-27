package com.binance.coin_futures_client.impl;

import java.util.LinkedList;
import java.util.List;

import com.binance.coin_futures_client.impl.utils.JsonWrapper;
import com.binance.coin_futures_client.impl.utils.JsonWrapperArray;

import com.binance.coin_futures_client.SubscriptionErrorHandler;
import com.binance.coin_futures_client.SubscriptionListener;
import com.binance.coin_futures_client.impl.utils.Channels;
import com.binance.coin_futures_client.model.enums.CandlestickInterval;
import com.binance.coin_futures_client.model.enums.ContractType;
import com.binance.coin_futures_client.model.event.AggregateTradeEvent;
import com.binance.coin_futures_client.model.event.CandlestickEvent;
import com.binance.coin_futures_client.model.event.LiquidationOrderEvent;
import com.binance.coin_futures_client.model.event.MarkPriceEvent;
import com.binance.coin_futures_client.model.event.OrderBookEvent;
import com.binance.coin_futures_client.model.event.SymbolBookTickerEvent;
import com.binance.coin_futures_client.model.event.SymbolMiniTickerEvent;
import com.binance.coin_futures_client.model.event.SymbolTickerEvent;
import com.binance.coin_futures_client.model.market.OrderBookEntry;
import com.binance.coin_futures_client.model.user.*;

class WebsocketRequestImpl {

    WebsocketRequestImpl() {
    }

    WebsocketRequest<AggregateTradeEvent> subscribeAggregateTradeEvent(String symbol,
            SubscriptionListener<AggregateTradeEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<AggregateTradeEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Aggregate Trade for " + symbol + "***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.aggregateTradeChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            AggregateTradeEvent result = new AggregateTradeEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            result.setSymbol(jsonWrapper.getString("s"));
            result.setId(jsonWrapper.getLong("a"));
            result.setPrice(jsonWrapper.getBigDecimal("p"));
            result.setQty(jsonWrapper.getBigDecimal("q"));
            result.setFirstId(jsonWrapper.getLong("f"));
            result.setLastId(jsonWrapper.getLong("l"));
            result.setTime(jsonWrapper.getLong("T"));
            result.setIsBuyerMaker(jsonWrapper.getBoolean("m"));
            return result;
        };
        return request;
    }

    WebsocketRequest<MarkPriceEvent> subscribeMarkPriceEvent(String symbol,
            SubscriptionListener<MarkPriceEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<MarkPriceEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Mark Price for " + symbol + "***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.markPriceChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            MarkPriceEvent result = new MarkPriceEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            result.setSymbol(jsonWrapper.getString("s"));
            result.setMarkPrice(jsonWrapper.getBigDecimal("p"));
            result.setEstimatedSettlePrice(jsonWrapper.getBigDecimal("P"));
            if (!jsonWrapper.getString("r").equals(""))
                result.setFundingRate(jsonWrapper.getBigDecimal("r"));
            result.setNextFundingTime(jsonWrapper.getLong("T"));
            return result;
        };
        return request;
    }

    WebsocketRequest<CandlestickEvent> subscribeCandlestickEvent(String symbol, CandlestickInterval interval,
            SubscriptionListener<CandlestickEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<CandlestickEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Candlestick for " + symbol + "***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.candlestickChannel(symbol, interval));

        request.jsonParser = (jsonWrapper) -> {
            CandlestickEvent result = new CandlestickEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            result.setSymbol(jsonWrapper.getString("s"));
            JsonWrapper jsondata = jsonWrapper.getJsonObject("k");
            result.setStartTime(jsondata.getLong("t"));
            result.setCloseTime(jsondata.getLong("T"));
            result.setSymbol(jsondata.getString("s"));
            result.setInterval(jsondata.getString("i"));
            result.setFirstTradeId(jsondata.getLong("f"));
            result.setLastTradeId(jsondata.getLong("L"));
            result.setOpen(jsondata.getBigDecimal("o"));
            result.setClose(jsondata.getBigDecimal("c"));
            result.setHigh(jsondata.getBigDecimal("h"));
            result.setLow(jsondata.getBigDecimal("l"));
            result.setVolume(jsondata.getBigDecimal("v"));
            result.setNumTrades(jsondata.getLong("n"));
            result.setIsClosed(jsondata.getBoolean("x"));
            result.setBaseAssetVolume(jsondata.getBigDecimal("q"));
            result.setTakerBuyQuoteAssetVolume(jsondata.getBigDecimal("V"));
            result.setTakerBuyBaseAssetVolume(jsondata.getBigDecimal("Q"));
            result.setIgnore(jsondata.getLong("B"));
            return result;
        };
        return request;
    }

    WebsocketRequest<CandlestickEvent> subscribeContinuousCandlestickEvent(String pair, ContractType contractType, CandlestickInterval interval,
                                                                           SubscriptionListener<CandlestickEvent> subscriptionListener,
                                                                           SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(pair, "pair")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<CandlestickEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Continuous Candlestick for " + pair + " " + contractType + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.continuousCandlestickChannel(pair, contractType, interval));

        request.jsonParser = (jsonWrapper) -> {
            CandlestickEvent result = new CandlestickEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            result.setPair(jsonWrapper.getString("ps"));
            result.setContractType(jsonWrapper.getString("ct"));
            JsonWrapper jsondata = jsonWrapper.getJsonObject("k");
            result.setStartTime(jsondata.getLong("t"));
            result.setCloseTime(jsondata.getLong("T"));
            result.setInterval(jsondata.getString("i"));
            result.setFirstTradeId(jsondata.getLong("f"));
            result.setLastTradeId(jsondata.getLong("L"));
            result.setOpen(jsondata.getBigDecimal("o"));
            result.setClose(jsondata.getBigDecimal("c"));
            result.setHigh(jsondata.getBigDecimal("h"));
            result.setLow(jsondata.getBigDecimal("l"));
            result.setVolume(jsondata.getBigDecimal("v"));
            result.setNumTrades(jsondata.getLong("n"));
            result.setIsClosed(jsondata.getBoolean("x"));
            result.setBaseAssetVolume(jsondata.getBigDecimal("q"));
            result.setTakerBuyQuoteAssetVolume(jsondata.getBigDecimal("V"));
            result.setTakerBuyBaseAssetVolume(jsondata.getBigDecimal("Q"));
            result.setIgnore(jsondata.getLong("B"));
            return result;
        };
        return request;
    }

    WebsocketRequest<CandlestickEvent> subscribeIndexPriceCandlestickEvent(String pair, CandlestickInterval interval,
                                                                           SubscriptionListener<CandlestickEvent> subscriptionListener,
                                                                           SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(pair, "pair")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<CandlestickEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Index Price Candlestick for " + pair + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.indexPriceCandlestickChannel(pair, interval));

        request.jsonParser = (jsonWrapper) -> {
            CandlestickEvent result = new CandlestickEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            result.setPair(jsonWrapper.getString("ps"));
            JsonWrapper jsondata = jsonWrapper.getJsonObject("k");
            result.setStartTime(jsondata.getLong("t"));
            result.setCloseTime(jsondata.getLong("T"));
            result.setInterval(jsondata.getString("i"));
            result.setFirstTradeId(jsondata.getLong("f"));
            result.setLastTradeId(jsondata.getLong("L"));
            result.setOpen(jsondata.getBigDecimal("o"));
            result.setClose(jsondata.getBigDecimal("c"));
            result.setHigh(jsondata.getBigDecimal("h"));
            result.setLow(jsondata.getBigDecimal("l"));
            result.setVolume(jsondata.getBigDecimal("v"));
            result.setNumTrades(jsondata.getLong("n"));
            result.setIsClosed(jsondata.getBoolean("x"));
            result.setBaseAssetVolume(jsondata.getBigDecimal("q"));
            result.setTakerBuyQuoteAssetVolume(jsondata.getBigDecimal("V"));
            result.setTakerBuyBaseAssetVolume(jsondata.getBigDecimal("Q"));
            result.setIgnore(jsondata.getLong("B"));
            return result;
        };
        return request;
    }

    WebsocketRequest<CandlestickEvent> subscribeMarkPriceCandlestickEvent(String symbol, CandlestickInterval interval,
                                                                 SubscriptionListener<CandlestickEvent> subscriptionListener,
                                                                 SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<CandlestickEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Mark Price Candlestick for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.markPricecandlestickChannel(symbol, interval));

        request.jsonParser = (jsonWrapper) -> {
            CandlestickEvent result = new CandlestickEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            result.setPair(jsonWrapper.getString("ps"));
            JsonWrapper jsondata = jsonWrapper.getJsonObject("k");
            result.setStartTime(jsondata.getLong("t"));
            result.setCloseTime(jsondata.getLong("T"));
            result.setSymbol(jsondata.getString("s"));
            result.setInterval(jsondata.getString("i"));
            result.setFirstTradeId(jsondata.getLong("f"));
            result.setLastTradeId(jsondata.getLong("L"));
            result.setOpen(jsondata.getBigDecimal("o"));
            result.setClose(jsondata.getBigDecimal("c"));
            result.setHigh(jsondata.getBigDecimal("h"));
            result.setLow(jsondata.getBigDecimal("l"));
            result.setVolume(jsondata.getBigDecimal("v"));
            result.setNumTrades(jsondata.getLong("n"));
            result.setIsClosed(jsondata.getBoolean("x"));
            result.setBaseAssetVolume(jsondata.getBigDecimal("q"));
            result.setTakerBuyQuoteAssetVolume(jsondata.getBigDecimal("V"));
            result.setTakerBuyBaseAssetVolume(jsondata.getBigDecimal("Q"));
            result.setIgnore(jsondata.getLong("B"));
            return result;
        };
        return request;
    }
    WebsocketRequest<SymbolMiniTickerEvent> subscribeSymbolMiniTickerEvent(String symbol,
            SubscriptionListener<SymbolMiniTickerEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<SymbolMiniTickerEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Individual Symbol Mini Ticker for " + symbol + "***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.miniTickerChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            SymbolMiniTickerEvent result = new SymbolMiniTickerEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            result.setSymbol(jsonWrapper.getString("s"));
            result.setPair(jsonWrapper.getString("ps"));
            result.setOpen(jsonWrapper.getBigDecimal("o"));
            result.setClose(jsonWrapper.getBigDecimal("c"));
            result.setHigh(jsonWrapper.getBigDecimal("h"));
            result.setLow(jsonWrapper.getBigDecimal("l"));
            result.setTotalTradedQuoteAssetVolume(jsonWrapper.getBigDecimal("v"));
            result.setTotalTradedBaseAssetVolume(jsonWrapper.getBigDecimal("q"));
            return result;
        };
        return request;
    }

    WebsocketRequest<List<SymbolMiniTickerEvent>> subscribeAllMiniTickerEvent(
            SubscriptionListener<List<SymbolMiniTickerEvent>> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<List<SymbolMiniTickerEvent>> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***All Market Mini Tickers"; 
        request.connectionHandler = (connection) -> connection.send(Channels.miniTickerChannel());

        request.jsonParser = (jsonWrapper) -> {
            List<SymbolMiniTickerEvent> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach(item -> {
                SymbolMiniTickerEvent element = new SymbolMiniTickerEvent();
                element.setEventType(item.getString("e"));
                element.setEventTime(item.getLong("E"));
                element.setSymbol(item.getString("s"));
                element.setPair(item.getString("ps"));
                element.setOpen(item.getBigDecimal("o"));
                element.setClose(item.getBigDecimal("c"));
                element.setHigh(item.getBigDecimal("h"));
                element.setLow(item.getBigDecimal("l"));
                element.setTotalTradedQuoteAssetVolume(item.getBigDecimal("v"));
                element.setTotalTradedBaseAssetVolume(item.getBigDecimal("q"));
                result.add(element);
            });
            return result;
        };
        return request;
    }

    WebsocketRequest<SymbolTickerEvent> subscribeSymbolTickerEvent(String symbol,
            SubscriptionListener<SymbolTickerEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<SymbolTickerEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Individual Symbol Ticker for " + symbol + "***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.tickerChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            SymbolTickerEvent result = new SymbolTickerEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            result.setSymbol(jsonWrapper.getString("s"));
            result.setPair(jsonWrapper.getString("ps"));
            result.setPriceChange(jsonWrapper.getBigDecimal("p"));
            result.setPriceChangePercent(jsonWrapper.getBigDecimal("P"));
            result.setWeightedAvgPrice(jsonWrapper.getBigDecimal("w"));
            result.setLastPrice(jsonWrapper.getBigDecimal("c"));
            result.setLastQty(jsonWrapper.getBigDecimal("Q"));
            result.setOpen(jsonWrapper.getBigDecimal("o"));
            result.setHigh(jsonWrapper.getBigDecimal("h"));
            result.setLow(jsonWrapper.getBigDecimal("l"));
            result.setTotalTradedQuoteAssetVolume(jsonWrapper.getBigDecimal("v"));
            result.setTotalTradedBaseAssetVolume(jsonWrapper.getBigDecimal("q"));
            result.setOpenTime(jsonWrapper.getLong("O"));
            result.setCloseTime(jsonWrapper.getLong("C"));
            result.setFirstId(jsonWrapper.getLong("F"));
            result.setLastId(jsonWrapper.getLong("L"));
            result.setCount(jsonWrapper.getLong("n"));
            return result;
        };
        return request;
    }

    WebsocketRequest<List<SymbolTickerEvent>> subscribeAllTickerEvent(
            SubscriptionListener<List<SymbolTickerEvent>> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<List<SymbolTickerEvent>> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***All Market Tickers"; 
        request.connectionHandler = (connection) -> connection.send(Channels.tickerChannel());

        request.jsonParser = (jsonWrapper) -> {
            List<SymbolTickerEvent> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach(item -> {
                SymbolTickerEvent element = new SymbolTickerEvent();
                element.setEventType(item.getString("e"));
                element.setEventTime(item.getLong("E"));
                element.setSymbol(item.getString("s"));
                element.setPair(item.getString("ps"));
                element.setPriceChange(item.getBigDecimal("p"));
                element.setPriceChangePercent(item.getBigDecimal("P"));
                element.setWeightedAvgPrice(item.getBigDecimal("w"));
                element.setLastPrice(item.getBigDecimal("c"));
                element.setLastQty(item.getBigDecimal("Q"));
                element.setOpen(item.getBigDecimal("o"));
                element.setHigh(item.getBigDecimal("h"));
                element.setLow(item.getBigDecimal("l"));
                element.setTotalTradedQuoteAssetVolume(item.getBigDecimal("v"));
                element.setTotalTradedBaseAssetVolume(item.getBigDecimal("q"));
                element.setOpenTime(item.getLong("O"));
                element.setCloseTime(item.getLong("C"));
                element.setFirstId(item.getLong("F"));
                element.setLastId(item.getLong("L"));
                element.setCount(item.getLong("n"));
                result.add(element);
            });
           
            return result;
        };
        return request;
    }

    WebsocketRequest<SymbolBookTickerEvent> subscribeSymbolBookTickerEvent(String symbol,
            SubscriptionListener<SymbolBookTickerEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<SymbolBookTickerEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Individual Symbol Book Ticker for " + symbol + "***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.bookTickerChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            SymbolBookTickerEvent result = new SymbolBookTickerEvent();
            result.setOrderBookUpdateId(jsonWrapper.getLong("u"));
            result.setSymbol(jsonWrapper.getString("s"));
            result.setPair(jsonWrapper.getString("ps"));
            result.setBestBidPrice(jsonWrapper.getBigDecimal("b"));
            result.setBestBidQty(jsonWrapper.getBigDecimal("B"));
            result.setBestAskPrice(jsonWrapper.getBigDecimal("a"));
            result.setBestAskQty(jsonWrapper.getBigDecimal("A"));
            return result;
        };
        return request;
    }

    WebsocketRequest<SymbolBookTickerEvent> subscribeAllBookTickerEvent(
            SubscriptionListener<SymbolBookTickerEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<SymbolBookTickerEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***All Market Book Tickers***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.bookTickerChannel());

        request.jsonParser = (jsonWrapper) -> {
            SymbolBookTickerEvent result = new SymbolBookTickerEvent();
            result.setOrderBookUpdateId(jsonWrapper.getLong("u"));
            result.setSymbol(jsonWrapper.getString("s"));
            result.setPair(jsonWrapper.getString("ps"));
            result.setBestBidPrice(jsonWrapper.getBigDecimal("b"));
            result.setBestBidQty(jsonWrapper.getBigDecimal("B"));
            result.setBestAskPrice(jsonWrapper.getBigDecimal("a"));
            result.setBestAskQty(jsonWrapper.getBigDecimal("A"));
            return result;
        };
        return request;
    }

    WebsocketRequest<LiquidationOrderEvent> subscribeSymbolLiquidationOrderEvent(String symbol,
            SubscriptionListener<LiquidationOrderEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<LiquidationOrderEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Individual Symbol Liquidation Order for " + symbol + "***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.liquidationOrderChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            LiquidationOrderEvent result = new LiquidationOrderEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            JsonWrapper jsondata = jsonWrapper.getJsonObject("o");
            result.setSymbol(jsondata.getString("s"));
            result.setPair(jsondata.getString("ps"));
            result.setSide(jsondata.getString("S"));
            result.setType(jsondata.getString("o"));
            result.setTimeInForce(jsondata.getString("f"));
            result.setOrigQty(jsondata.getBigDecimal("q"));
            result.setPrice(jsondata.getBigDecimal("p"));
            result.setAveragePrice(jsondata.getBigDecimal("ap"));
            result.setOrderStatus(jsondata.getString("X"));
            result.setLastFilledQty(jsondata.getBigDecimal("l"));
            result.setLastFilledAccumulatedQty(jsondata.getBigDecimal("z"));
            result.setTime(jsondata.getLong("T"));
            return result;
        };
        return request;
    }

    WebsocketRequest<LiquidationOrderEvent> subscribeAllLiquidationOrderEvent(
            SubscriptionListener<LiquidationOrderEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<LiquidationOrderEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***All Liquidation Orders***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.liquidationOrderChannel());

        request.jsonParser = (jsonWrapper) -> {
            LiquidationOrderEvent result = new LiquidationOrderEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            JsonWrapper jsondata = jsonWrapper.getJsonObject("o");
            result.setSymbol(jsondata.getString("s"));
            result.setPair(jsondata.getString("ps"));
            result.setSide(jsondata.getString("S"));
            result.setType(jsondata.getString("o"));
            result.setTimeInForce(jsondata.getString("f"));
            result.setOrigQty(jsondata.getBigDecimal("q"));
            result.setPrice(jsondata.getBigDecimal("p"));
            result.setAveragePrice(jsondata.getBigDecimal("ap"));
            result.setOrderStatus(jsondata.getString("X"));
            result.setLastFilledQty(jsondata.getBigDecimal("l"));
            result.setLastFilledAccumulatedQty(jsondata.getBigDecimal("z"));
            result.setTime(jsondata.getLong("T"));
            return result;
        };
        return request;
    }

    WebsocketRequest<OrderBookEvent> subscribeBookDepthEvent(String symbol, Integer limit,
            SubscriptionListener<OrderBookEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(limit, "limit")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<OrderBookEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Partial Book Depth for " + symbol + "***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.bookDepthChannel(symbol, limit));

        request.jsonParser = (jsonWrapper) -> {
            OrderBookEvent result = new OrderBookEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            result.setTransactionTime(jsonWrapper.getLong("T"));
            result.setSymbol(jsonWrapper.getString("s"));
            result.setPair(jsonWrapper.getString("ps"));
            result.setFirstUpdateId(jsonWrapper.getLong("U"));
            result.setLastUpdateId(jsonWrapper.getLong("u"));
            result.setLastUpdateIdInlastStream(jsonWrapper.getLong("pu"));

            List<OrderBookEntry> elementList = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("b");
            dataArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(item.getBigDecimalAt(1));
                elementList.add(element);
            });
            result.setBids(elementList);

            List<OrderBookEntry> askList = new LinkedList<>();
            JsonWrapperArray askArray = jsonWrapper.getJsonArray("a");
            askArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(item.getBigDecimalAt(1));
                askList.add(element);
            });
            result.setAsks(askList);
            
            return result;
        };
        return request;
    }

    WebsocketRequest<OrderBookEvent> subscribeDiffDepthEvent(String symbol,
            SubscriptionListener<OrderBookEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<OrderBookEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Partial Book Depth for " + symbol + "***"; 
        request.connectionHandler = (connection) -> connection.send(Channels.diffDepthChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            OrderBookEvent result = new OrderBookEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));
            result.setTransactionTime(jsonWrapper.getLong("T"));
            result.setSymbol(jsonWrapper.getString("s"));
            result.setPair(jsonWrapper.getString("ps"));
            result.setFirstUpdateId(jsonWrapper.getLong("U"));
            result.setLastUpdateId(jsonWrapper.getLong("u"));
            result.setLastUpdateIdInlastStream(jsonWrapper.getLong("pu"));

            List<OrderBookEntry> elementList = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("b");
            dataArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(item.getBigDecimalAt(1));
                elementList.add(element);
            });
            result.setBids(elementList);

            List<OrderBookEntry> askList = new LinkedList<>();
            JsonWrapperArray askArray = jsonWrapper.getJsonArray("a");
            askArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(item.getBigDecimalAt(1));
                askList.add(element);
            });
            result.setAsks(askList);
            
            return result;
        };
        return request;
    }

    WebsocketRequest<UserDataUpdateEvent> subscribeUserDataEvent(String listenKey,
            SubscriptionListener<UserDataUpdateEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(listenKey, "listenKey")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<UserDataUpdateEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***User Data***";
        request.connectionHandler = (connection) -> connection.send(Channels.userDataChannel(listenKey));

        request.jsonParser = (jsonWrapper) -> {
            UserDataUpdateEvent result = new UserDataUpdateEvent();
            result.setEventType(jsonWrapper.getString("e"));
            result.setEventTime(jsonWrapper.getLong("E"));

            if(jsonWrapper.getString("e").equals("ACCOUNT_UPDATE")) {
                result.setTransactionTime(jsonWrapper.getLong("T"));

                AccountUpdate accountUpdate = new AccountUpdate();

                accountUpdate.setEventReasonType(jsonWrapper.getJsonObject("a").getString("m"));

                List<BalanceUpdate> balanceList = new LinkedList<>();
                JsonWrapperArray dataArray = jsonWrapper.getJsonObject("a").getJsonArray("B");
                dataArray.forEach(item -> {
                    BalanceUpdate balance = new BalanceUpdate();
                    balance.setAsset(item.getString("a"));
                    balance.setWalletBalance(item.getBigDecimal("wb"));
                    balance.setCrossWalletBalance(item.getBigDecimal("cw"));
                    balanceList.add(balance);
                });
                accountUpdate.setBalances(balanceList);

                List<PositionUpdate> positionList = new LinkedList<>();
                JsonWrapperArray datalist = jsonWrapper.getJsonObject("a").getJsonArray("P");
                datalist.forEach(item -> {
                    PositionUpdate position = new PositionUpdate();
                    position.setSymbol(item.getString("s"));
                    position.setAmount(item.getBigDecimal("pa"));
                    position.setEntryPrice(item.getBigDecimal("ep"));
                    position.setPreFee(item.getBigDecimal("cr"));
                    position.setUnrealizedPnl(item.getBigDecimal("up"));
                    position.setMarginType(item.getString("mt"));
                    position.setIsolatedWallet(item.getBigDecimal("iw"));
                    position.setPositionSide(item.getString("ps"));
                    positionList.add(position);
                });
                accountUpdate.setPositions(positionList);

                result.setAccountUpdate(accountUpdate); 

            } else if(jsonWrapper.getString("e").equals("ORDER_TRADE_UPDATE")) {
                result.setTransactionTime(jsonWrapper.getLong("T"));

                OrderUpdate orderUpdate = new OrderUpdate();
                JsonWrapper jsondata = jsonWrapper.getJsonObject("o");
                orderUpdate.setSymbol(jsondata.getString("s"));
                orderUpdate.setClientOrderId(jsondata.getString("c"));
                orderUpdate.setSide(jsondata.getString("S"));
                orderUpdate.setType(jsondata.getString("o"));
                orderUpdate.setTimeInForce(jsondata.getString("f"));
                orderUpdate.setOrigQty(jsondata.getBigDecimal("q"));
                orderUpdate.setPrice(jsondata.getBigDecimal("p"));
                orderUpdate.setAvgPrice(jsondata.getBigDecimal("ap"));
                orderUpdate.setStopPrice(jsondata.getBigDecimal("sp"));
                orderUpdate.setExecutionType(jsondata.getString("x"));
                orderUpdate.setOrderStatus(jsondata.getString("X"));
                orderUpdate.setOrderId(jsondata.getLong("i"));
                orderUpdate.setLastFilledQty(jsondata.getBigDecimal("l"));
                orderUpdate.setCumulativeFilledQty(jsondata.getBigDecimal("z"));
                orderUpdate.setLastFilledPrice(jsondata.getBigDecimal("L"));
                orderUpdate.setOrderTradeTime(jsondata.getLong("T"));
                orderUpdate.setTradeID(jsondata.getLong("t"));
                orderUpdate.setBidsNotional(jsondata.getBigDecimal("b"));
                orderUpdate.setAsksNotional(jsondata.getBigDecimal("a"));
                orderUpdate.setIsMarkerSide(jsondata.getBoolean("m"));
                orderUpdate.setIsReduceOnly(jsondata.getBoolean("R"));
                orderUpdate.setWorkingType(jsondata.getString("wt"));
                orderUpdate.setOriginalOrderType(jsondata.getString(("ot")));
                orderUpdate.setPositionSide(jsondata.getString(("ps")));
                if(jsondata.containKey("N"))
                    orderUpdate.setCommissionAsset(jsondata.getString("N"));
                if(jsondata.containKey("n"))
                    orderUpdate.setCommissionAmount(jsondata.getBigDecimal("n"));
                if(jsondata.containKey("AP"))
                    orderUpdate.setActivationPrice(jsondata.getBigDecimal("AP"));
                if(jsondata.containKey("cr"))
                    orderUpdate.setCallbackRate(jsondata.getBigDecimal("cr"));

                result.setOrderUpdate(orderUpdate); 
            } else if(jsonWrapper.getString("e").equals("MARGIN_CALL")) {
                MarginCallUpdate marginCallUpdate = new MarginCallUpdate();

                marginCallUpdate.setCrossWalletBalance(jsonWrapper.getBigDecimal("cw"));

                List<PositionUpdate> positionList = new LinkedList<>();
                JsonWrapperArray datalist = jsonWrapper.getJsonArray("p");
                datalist.forEach(item -> {
                    PositionUpdate position = new PositionUpdate();
                    position.setSymbol(item.getString("s"));
                    position.setPositionSide(item.getString("ps"));
                    position.setAmount(item.getBigDecimal("pa"));
                    position.setMarginType(item.getString("mt"));
                    position.setIsolatedWallet(item.getBigDecimal("iw"));
                    position.setMarkPrice(item.getBigDecimal("mp"));
                    position.setUnrealizedPnl(item.getBigDecimal("up"));
                    position.setMaintMarginRequired(item.getBigDecimal("mm"));
                    positionList.add(position);
                });
                marginCallUpdate.setPositions(positionList);

                result.setMarginCallUpdate(marginCallUpdate);
            }

            return result;
        };
        return request;
    }

}
