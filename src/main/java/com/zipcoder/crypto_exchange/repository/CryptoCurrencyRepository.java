package com.zipcoder.crypto_exchange.repository;

import com.zipcoder.crypto_exchange.model.CryptoCurrency;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CryptoCurrency CRUD operations.
 * Following the standard repository pattern for data access.
 * Uses cryptocurrency symbol (e.g., "BTC") as the unique identifier.
 */
public interface CryptoCurrencyRepository {
    
    /**
     * Create or save a cryptocurrency record
     */
    CryptoCurrency save(CryptoCurrency cryptoCurrency);
    
    /**
     * Find a cryptocurrency by its symbol (e.g., "BTC", "ETH")
     */
    Optional<CryptoCurrency> findBySymbol(String symbol);
    
    /**
     * Get all stored cryptocurrencies
     */
    List<CryptoCurrency> findAll();
    
    /**
     * Delete a cryptocurrency by its symbol
     */
    boolean deleteBySymbol(String symbol);
    
    /**
     * Check if a cryptocurrency exists
     */
    boolean existsBySymbol(String symbol);
    
    /**
     * Delete all stored cryptocurrencies
     */
    void deleteAll();
    
    /**
     * Count total stored cryptocurrencies
     */
    long count();
    
    /**
     * Get all tracked symbols
     */
    List<String> getAllSymbols();
}