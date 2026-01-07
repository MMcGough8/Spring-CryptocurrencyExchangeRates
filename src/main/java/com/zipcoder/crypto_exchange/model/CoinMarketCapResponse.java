package com.zipcoder.crypto_exchange.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * DTO for mapping the CoinMarketCap API response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinMarketCapResponse {
    
    private Status status;
    private Map<String, CryptoData> data;
    
    // Status nested class
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {
        private String timestamp;
        
        @JsonProperty("error_code")
        private Integer errorCode;
        
        @JsonProperty("error_message")
        private String errorMessage;
        
        private Integer elapsed;
        
        @JsonProperty("credit_count")
        private Integer creditCount;
        
        public String getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        
        public Integer getErrorCode() {
            return errorCode;
        }
        
        public void setErrorCode(Integer errorCode) {
            this.errorCode = errorCode;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        
        public Integer getElapsed() {
            return elapsed;
        }
        
        public void setElapsed(Integer elapsed) {
            this.elapsed = elapsed;
        }
        
        public Integer getCreditCount() {
            return creditCount;
        }
        
        public void setCreditCount(Integer creditCount) {
            this.creditCount = creditCount;
        }
    }
    
    // CryptoData nested class
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CryptoData {
        private Integer id;
        private String name;
        private String symbol;
        private String slug;
        
        @JsonProperty("cmc_rank")
        private Integer cmcRank;
        
        @JsonProperty("circulating_supply")
        private Double circulatingSupply;
        
        @JsonProperty("total_supply")
        private Double totalSupply;
        
        @JsonProperty("max_supply")
        private Double maxSupply;
        
        private Map<String, Quote> quote;
        
        public Integer getId() {
            return id;
        }
        
        public void setId(Integer id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getSymbol() {
            return symbol;
        }
        
        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
        
        public String getSlug() {
            return slug;
        }
        
        public void setSlug(String slug) {
            this.slug = slug;
        }
        
        public Integer getCmcRank() {
            return cmcRank;
        }
        
        public void setCmcRank(Integer cmcRank) {
            this.cmcRank = cmcRank;
        }
        
        public Double getCirculatingSupply() {
            return circulatingSupply;
        }
        
        public void setCirculatingSupply(Double circulatingSupply) {
            this.circulatingSupply = circulatingSupply;
        }
        
        public Double getTotalSupply() {
            return totalSupply;
        }
        
        public void setTotalSupply(Double totalSupply) {
            this.totalSupply = totalSupply;
        }
        
        public Double getMaxSupply() {
            return maxSupply;
        }
        
        public void setMaxSupply(Double maxSupply) {
            this.maxSupply = maxSupply;
        }
        
        public Map<String, Quote> getQuote() {
            return quote;
        }
        
        public void setQuote(Map<String, Quote> quote) {
            this.quote = quote;
        }
    }
    
    // Quote nested class
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Quote {
        private Double price;
        
        @JsonProperty("volume_24h")
        private Double volume24h;
        
        @JsonProperty("percent_change_1h")
        private Double percentChange1h;
        
        @JsonProperty("percent_change_24h")
        private Double percentChange24h;
        
        @JsonProperty("percent_change_7d")
        private Double percentChange7d;
        
        @JsonProperty("market_cap")
        private Double marketCap;
        
        @JsonProperty("last_updated")
        private String lastUpdated;
        
        public Double getPrice() {
            return price;
        }
        
        public void setPrice(Double price) {
            this.price = price;
        }
        
        public Double getVolume24h() {
            return volume24h;
        }
        
        public void setVolume24h(Double volume24h) {
            this.volume24h = volume24h;
        }
        
        public Double getPercentChange1h() {
            return percentChange1h;
        }
        
        public void setPercentChange1h(Double percentChange1h) {
            this.percentChange1h = percentChange1h;
        }
        
        public Double getPercentChange24h() {
            return percentChange24h;
        }
        
        public void setPercentChange24h(Double percentChange24h) {
            this.percentChange24h = percentChange24h;
        }
        
        public Double getPercentChange7d() {
            return percentChange7d;
        }
        
        public void setPercentChange7d(Double percentChange7d) {
            this.percentChange7d = percentChange7d;
        }
        
        public Double getMarketCap() {
            return marketCap;
        }
        
        public void setMarketCap(Double marketCap) {
            this.marketCap = marketCap;
        }
        
        public String getLastUpdated() {
            return lastUpdated;
        }
        
        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
    }
    
    // Main class Getters and Setters
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Map<String, CryptoData> getData() {
        return data;
    }
    
    public void setData(Map<String, CryptoData> data) {
        this.data = data;
    }
}