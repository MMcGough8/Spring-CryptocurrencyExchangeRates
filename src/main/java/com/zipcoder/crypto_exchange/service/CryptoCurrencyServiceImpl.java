package com.zipcoder.crypto_exchange.service;

import com.zipcoder.crypto_exchange.model.CryptoCurrency;
import com.zipcoder.crypto_exchange.repository.CryptoCurrencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of CryptoCurrencyService.
 * Contains business logic and coordinates between repository and CoinMarketCap API service.
 * Includes scheduled task to refresh data every 5 minutes.
 */
@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    
    private static final Logger logger = LoggerFactory.getLogger(CryptoCurrencyServiceImpl.class);
    
    // Default cryptocurrencies to track on startup
    private static final String[] DEFAULT_SYMBOLS = {"BTC", "ETH", "BNB", "XRP", "ADA", "DOGE", "SOL", "DOT", "LTC", "MATIC"};
    
    private final CryptoCurrencyRepository repository;
    private final CoinMarketCapApiService apiService;
    
    public CryptoCurrencyServiceImpl(CryptoCurrencyRepository repository, 
                                      CoinMarketCapApiService apiService) {
        this.repository = repository;
        this.apiService = apiService;
    }
    
    /**
     * Initialize default cryptocurrency symbols on application startup.
     * Uses @EventListener instead of @PostConstruct for better compatibility.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeDefaultCurrencies() {
        if (!apiService.isApiKeyConfigured()) {
            logger.warn("CoinMarketCap API key not configured. Skipping initialization.");
            logger.warn("Please set 'coinmarketcap.api.key' in application.properties");
            return;
        }
        
        logger.info("Initializing default cryptocurrency tracking...");
        
        // Fetch all default symbols in one API call (more efficient)
        Map<String, CryptoCurrency> fetched = apiService.fetchQuotesBySymbols(List.of(DEFAULT_SYMBOLS));
        
        for (CryptoCurrency crypto : fetched.values()) {
            repository.save(crypto);
        }
        
        logger.info("Initialized {} cryptocurrencies", repository.count());
    }
    
    @Override
    public Optional<CryptoCurrency> getCryptoCurrency(String symbol) {
        return repository.findBySymbol(symbol.toUpperCase());
    }
    
    @Override
    public List<CryptoCurrency> getAllCryptoCurrencies() {
        return repository.findAll();
    }
    
    @Override
    public Optional<CryptoCurrency> addCryptoCurrency(String symbol) {
        String upperSymbol = symbol.toUpperCase();
        
        // Check if already tracked
        if (repository.existsBySymbol(upperSymbol)) {
            logger.info("Cryptocurrency {} already tracked, returning existing data", upperSymbol);
            return repository.findBySymbol(upperSymbol);
        }
        
        // Fetch from API and save
        Optional<CryptoCurrency> fetched = apiService.fetchQuoteBySymbol(upperSymbol);
        fetched.ifPresent(crypto -> {
            repository.save(crypto);
            logger.info("Added new cryptocurrency to tracking: {}", upperSymbol);
        });
        
        return fetched;
    }
    
    @Override
    public boolean removeCryptoCurrency(String symbol) {
        String upperSymbol = symbol.toUpperCase();
        boolean removed = repository.deleteBySymbol(upperSymbol);
        if (removed) {
            logger.info("Removed cryptocurrency from tracking: {}", upperSymbol);
        }
        return removed;
    }
    
    @Override
    public Optional<CryptoCurrency> refreshCryptoCurrency(String symbol) {
        String upperSymbol = symbol.toUpperCase();
        
        Optional<CryptoCurrency> fetched = apiService.fetchQuoteBySymbol(upperSymbol);
        fetched.ifPresent(repository::save);
        
        return fetched;
    }
    
    /**
     * Scheduled task to refresh all tracked cryptocurrencies every 5 minutes.
     * Uses batch API call for efficiency.
     */
    @Override
    @Scheduled(fixedRate = 300000) // 5 minutes in milliseconds
    public void refreshAllCryptoCurrencies() {
        if (!apiService.isApiKeyConfigured()) {
            logger.warn("Skipping scheduled refresh: API key not configured");
            return;
        }
        
        List<String> symbols = repository.getAllSymbols();
        if (symbols.isEmpty()) {
            logger.info("No cryptocurrencies to refresh");
            return;
        }
        
        logger.info("Starting scheduled refresh for {} cryptocurrencies...", symbols.size());
        
        // Fetch all symbols in one API call (efficient!)
        Map<String, CryptoCurrency> updated = apiService.fetchQuotesBySymbols(symbols);
        
        int successCount = 0;
        for (CryptoCurrency crypto : updated.values()) {
            repository.save(crypto);
            successCount++;
        }
        
        logger.info("Completed scheduled refresh. Updated {}/{} cryptocurrencies", 
                successCount, symbols.size());
    }
    
    @Override
    public boolean isApiConfigured() {
        return apiService.isApiKeyConfigured();
    }
}