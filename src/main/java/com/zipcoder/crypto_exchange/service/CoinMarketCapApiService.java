package com.zipcoder.crypto_exchange.service;

import com.zipcoder.crypto_exchange.model.CoinMarketCapResponse;
import com.zipcoder.crypto_exchange.model.CryptoCurrency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for communicating with the CoinMarketCap API.
 * Handles all external API calls and response mapping.
 * 
 * API Documentation: https://coinmarketcap.com/api/documentation/v1/
 */
@Service
public class CoinMarketCapApiService {
    
    private static final Logger logger = LoggerFactory.getLogger(CoinMarketCapApiService.class);
    private static final String CMC_API_BASE_URL = "https://pro-api.coinmarketcap.com";
    private static final String DEFAULT_CONVERT = "USD";
    
    private final WebClient webClient;
    private final String apiKey;
    
    public CoinMarketCapApiService(
            WebClient.Builder webClientBuilder,
            @Value("${coinmarketcap.api.key:}") String apiKey) {
        
        this.apiKey = apiKey;
        this.webClient = webClientBuilder
                .baseUrl(CMC_API_BASE_URL)
                .defaultHeader("X-CMC_PRO_API_KEY", apiKey)
                .defaultHeader("Accept", "application/json")
                .build();
        
        if (apiKey == null || apiKey.isEmpty()) {
            logger.warn("CoinMarketCap API key not configured! Set 'coinmarketcap.api.key' in application.properties");
        }
    }
    
    /**
     * Fetches the latest quote for a specific cryptocurrency symbol.
     * API endpoint: /v1/cryptocurrency/quotes/latest?symbol={symbol}&convert=USD
     */
    public Optional<CryptoCurrency> fetchQuoteBySymbol(String symbol) {
        if (apiKey == null || apiKey.isEmpty()) {
            logger.error("Cannot fetch data: API key not configured");
            return Optional.empty();
        }
        
        String upperSymbol = symbol.toUpperCase();
        
        try {
            logger.info("Fetching quote for symbol: {}", upperSymbol);
            
            CoinMarketCapResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/cryptocurrency/quotes/latest")
                            .queryParam("symbol", upperSymbol)
                            .queryParam("convert", DEFAULT_CONVERT)
                            .build())
                    .retrieve()
                    .bodyToMono(CoinMarketCapResponse.class)
                    .block();
            
            if (response != null && response.getStatus().getErrorCode() == 0 
                    && response.getData() != null 
                    && response.getData().containsKey(upperSymbol)) {
                
                CryptoCurrency crypto = mapToCryptoCurrency(response.getData().get(upperSymbol));
                logger.info("Successfully fetched data for {}: price=${}", upperSymbol, crypto.getPrice());
                return Optional.of(crypto);
            } else {
                String error = response != null && response.getStatus() != null 
                        ? response.getStatus().getErrorMessage() 
                        : "No response";
                logger.warn("Failed to fetch data for {}: {}", upperSymbol, error);
                return Optional.empty();
            }
        } catch (WebClientResponseException e) {
            logger.error("API error fetching {}: {} - {}", upperSymbol, e.getStatusCode(), e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error fetching data for {}: {}", upperSymbol, e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Fetches quotes for multiple cryptocurrency symbols in a single API call.
     * This is more efficient than making multiple individual calls.
     */
    public Map<String, CryptoCurrency> fetchQuotesBySymbols(List<String> symbols) {
        if (apiKey == null || apiKey.isEmpty()) {
            logger.error("Cannot fetch data: API key not configured");
            return Collections.emptyMap();
        }
        
        if (symbols == null || symbols.isEmpty()) {
            return Collections.emptyMap();
        }
        
        String symbolList = symbols.stream()
                .map(String::toUpperCase)
                .collect(Collectors.joining(","));
        
        try {
            logger.info("Fetching quotes for symbols: {}", symbolList);
            
            CoinMarketCapResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/cryptocurrency/quotes/latest")
                            .queryParam("symbol", symbolList)
                            .queryParam("convert", DEFAULT_CONVERT)
                            .build())
                    .retrieve()
                    .bodyToMono(CoinMarketCapResponse.class)
                    .block();
            
            if (response != null && response.getStatus().getErrorCode() == 0 
                    && response.getData() != null) {
                
                Map<String, CryptoCurrency> result = new HashMap<>();
                for (Map.Entry<String, CoinMarketCapResponse.CryptoData> entry : response.getData().entrySet()) {
                    result.put(entry.getKey(), mapToCryptoCurrency(entry.getValue()));
                }
                logger.info("Successfully fetched data for {} cryptocurrencies", result.size());
                return result;
            } else {
                logger.warn("Failed to fetch data for symbols: {}", symbolList);
                return Collections.emptyMap();
            }
        } catch (WebClientResponseException e) {
            logger.error("API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return Collections.emptyMap();
        } catch (Exception e) {
            logger.error("Error fetching data: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
    
    /**
     * Maps the API response data to our domain model.
     */
    private CryptoCurrency mapToCryptoCurrency(CoinMarketCapResponse.CryptoData data) {
        CryptoCurrency crypto = new CryptoCurrency();
        
        crypto.setCmcId(data.getId());
        crypto.setSymbol(data.getSymbol());
        crypto.setName(data.getName());
        crypto.setSlug(data.getSlug());
        crypto.setCmcRank(data.getCmcRank());
        crypto.setCirculatingSupply(data.getCirculatingSupply());
        crypto.setTotalSupply(data.getTotalSupply());
        crypto.setMaxSupply(data.getMaxSupply());
        
        // Get USD quote
        if (data.getQuote() != null && data.getQuote().containsKey(DEFAULT_CONVERT)) {
            CoinMarketCapResponse.Quote quote = data.getQuote().get(DEFAULT_CONVERT);
            crypto.setPrice(quote.getPrice());
            crypto.setVolume24h(quote.getVolume24h());
            crypto.setPercentChange1h(quote.getPercentChange1h());
            crypto.setPercentChange24h(quote.getPercentChange24h());
            crypto.setPercentChange7d(quote.getPercentChange7d());
            crypto.setMarketCap(quote.getMarketCap());
            
            // Parse the last_updated timestamp
            if (quote.getLastUpdated() != null) {
                try {
                    crypto.setLastUpdated(LocalDateTime.parse(
                            quote.getLastUpdated().replace("Z", ""),
                            DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                } catch (Exception e) {
                    crypto.setLastUpdated(LocalDateTime.now());
                }
            }
        }
        
        if (crypto.getLastUpdated() == null) {
            crypto.setLastUpdated(LocalDateTime.now());
        }
        
        return crypto;
    }
    
    /**
     * Checks if the API key is configured.
     */
    public boolean isApiKeyConfigured() {
        return apiKey != null && !apiKey.isEmpty();
    }
}