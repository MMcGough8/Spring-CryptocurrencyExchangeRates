package com.zipcoder.crypto_exchange.model;
import java.time.LocalDateTime;

/**
 * Domain model representing cryptocurrency exchange rate data.
 * This maps to the quote information from CoinMarketCap API.
 */
public class CryptoCurrency {
    
    private Integer cmcId;              // CoinMarketCap unique ID
    private String symbol;              // Currency symbol (e.g., "BTC")
    private String name;                // Full name (e.g., "Bitcoin")
    private String slug;                // URL slug (e.g., "bitcoin")
    private Integer cmcRank;            // CoinMarketCap ranking
    private Double circulatingSupply;   // Circulating supply
    private Double totalSupply;         // Total supply
    private Double maxSupply;           // Maximum supply
    private Double price;               // Current price in USD
    private Double volume24h;           // 24h trading volume
    private Double percentChange1h;     // 1 hour price change %
    private Double percentChange24h;    // 24 hour price change %
    private Double percentChange7d;     // 7 day price change %
    private Double marketCap;           // Market capitalization
    private LocalDateTime lastUpdated;
    
    // Default constructor
    public CryptoCurrency() {
    }
    
    public Integer getCmcId() {
        return cmcId;
    }
    
    public void setCmcId(Integer cmcId) {
        this.cmcId = cmcId;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    @Override
    public String toString() {
        return "CryptoCurrency{" +
                "cmcId=" + cmcId +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", cmcRank=" + cmcRank +
                ", price=" + price +
                ", marketCap=" + marketCap +
                ", percentChange24h=" + percentChange24h +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}