package com.zipcoder.crypto_exchange.controller;

import com.zipcoder.crypto_exchange.model.CryptoCurrency;
import com.zipcoder.crypto_exchange.service.CryptoCurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller exposing cryptocurrency exchange rate endpoints.
 * Uses CoinMarketCap API for data.
 * 
 * Endpoints:
 * - GET    /api/crypto                 - Get all tracked cryptocurrencies
 * - GET    /api/crypto/{symbol}        - Get specific cryptocurrency by symbol
 * - POST   /api/crypto/{symbol}        - Add new cryptocurrency to track
 * - DELETE /api/crypto/{symbol}        - Remove cryptocurrency from tracking
 * - POST   /api/crypto/{symbol}/refresh - Force refresh from API
 * - POST   /api/crypto/refresh-all     - Force refresh all tracked cryptocurrencies
 * - GET    /api/crypto/status          - Check API configuration status
 */
@RestController
@RequestMapping("/api/crypto")
public class CryptoCurrencyController {
    
    private static final Logger logger = LoggerFactory.getLogger(CryptoCurrencyController.class);
    
    private final CryptoCurrencyService cryptoCurrencyService;
    
    public CryptoCurrencyController(CryptoCurrencyService cryptoCurrencyService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
    }
    
    /**
     * GET /api/crypto
     * Retrieves all tracked cryptocurrency exchange rates.
     */
    @GetMapping
    public ResponseEntity<List<CryptoCurrency>> getAllCryptoCurrencies() {
        logger.info("Request: GET all cryptocurrencies");
        List<CryptoCurrency> currencies = cryptoCurrencyService.getAllCryptoCurrencies();
        return ResponseEntity.ok(currencies);
    }
    
    /**
     * GET /api/crypto/{symbol}
     * Retrieves data for a specific cryptocurrency by symbol.
     * Example: GET /api/crypto/BTC
     */
    @GetMapping("/{symbol}")
    public ResponseEntity<?> getCryptoCurrency(@PathVariable String symbol) {
        logger.info("Request: GET cryptocurrency {}", symbol);
        
        Optional<CryptoCurrency> crypto = cryptoCurrencyService.getCryptoCurrency(symbol);
        
        if (crypto.isPresent()) {
            return ResponseEntity.ok(crypto.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Cryptocurrency not found: " + symbol.toUpperCase());
            error.put("message", "Use POST /api/crypto/" + symbol + " to add it to tracking");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * POST /api/crypto/{symbol}
     * Adds a new cryptocurrency to track by symbol.
     * Example: POST /api/crypto/SOL
     */
    @PostMapping("/{symbol}")
    public ResponseEntity<?> addCryptoCurrency(@PathVariable String symbol) {
        logger.info("Request: POST add cryptocurrency {}", symbol);
        
        if (!cryptoCurrencyService.isApiConfigured()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "API not configured");
            error.put("message", "Please set 'coinmarketcap.api.key' in application.properties");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
        
        Optional<CryptoCurrency> crypto = cryptoCurrencyService.addCryptoCurrency(symbol);
        
        if (crypto.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(crypto.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Could not fetch data for " + symbol.toUpperCase());
            error.put("message", "The symbol may not be supported by CoinMarketCap API");
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * DELETE /api/crypto/{symbol}
     * Removes a cryptocurrency from tracking.
     * Example: DELETE /api/crypto/DOGE
     */
    @DeleteMapping("/{symbol}")
    public ResponseEntity<?> removeCryptoCurrency(@PathVariable String symbol) {
        logger.info("Request: DELETE cryptocurrency {}", symbol);
        
        boolean removed = cryptoCurrencyService.removeCryptoCurrency(symbol);
        
        if (removed) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Successfully removed " + symbol.toUpperCase() + " from tracking");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * POST /api/crypto/{symbol}/refresh
     * Forces a refresh of a specific cryptocurrency from the external API.
     * Example: POST /api/crypto/BTC/refresh
     */
    @PostMapping("/{symbol}/refresh")
    public ResponseEntity<?> refreshCryptoCurrency(@PathVariable String symbol) {
        logger.info("Request: POST refresh cryptocurrency {}", symbol);
        
        if (!cryptoCurrencyService.isApiConfigured()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "API not configured");
            error.put("message", "Please set 'coinmarketcap.api.key' in application.properties");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
        
        Optional<CryptoCurrency> crypto = cryptoCurrencyService.refreshCryptoCurrency(symbol);
        
        if (crypto.isPresent()) {
            return ResponseEntity.ok(crypto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * POST /api/crypto/refresh-all
     * Forces a refresh of all tracked cryptocurrencies from the external API.
     */
    @PostMapping("/refresh-all")
    public ResponseEntity<Map<String, String>> refreshAllCryptoCurrencies() {
        logger.info("Request: POST refresh all cryptocurrencies");
        
        if (!cryptoCurrencyService.isApiConfigured()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "API not configured");
            error.put("message", "Please set 'coinmarketcap.api.key' in application.properties");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
        
        cryptoCurrencyService.refreshAllCryptoCurrencies();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Refresh completed for all tracked cryptocurrencies");
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/crypto/status
     * Returns the API configuration status.
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("apiConfigured", cryptoCurrencyService.isApiConfigured());
        status.put("trackedCurrencies", cryptoCurrencyService.getAllCryptoCurrencies().size());
        status.put("message", cryptoCurrencyService.isApiConfigured() 
                ? "API is configured and ready" 
                : "API key not configured. Set 'coinmarketcap.api.key' in application.properties");
        return ResponseEntity.ok(status);
    }
}