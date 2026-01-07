package com.zipcoder.crypto_exchange.repository;

import com.zipcoder.crypto_exchange.model.CryptoCurrency;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of CryptoCurrencyRepository.
 * the scheduled updates run in a separate thread.
 * Key is the cryptocurrency symbol (e.g., "BTC").
 */
@Repository
public class InMemoryCryptoCurrencyRepository implements CryptoCurrencyRepository {
    
    // Thread-safe map to store cryptocurrency data, keyed by symbol
    private final Map<String, CryptoCurrency> dataStore = new ConcurrentHashMap<>();
    
    @Override
    public CryptoCurrency save(CryptoCurrency cryptoCurrency) {
        if (cryptoCurrency == null || cryptoCurrency.getSymbol() == null) {
            throw new IllegalArgumentException("CryptoCurrency and its symbol cannot be null");
        }
        dataStore.put(cryptoCurrency.getSymbol().toUpperCase(), cryptoCurrency);
        return cryptoCurrency;
    }
    
    @Override
    public Optional<CryptoCurrency> findBySymbol(String symbol) {
        if (symbol == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(dataStore.get(symbol.toUpperCase()));
    }
    
    @Override
    public List<CryptoCurrency> findAll() {
        return new ArrayList<>(dataStore.values());
    }
    
    @Override
    public boolean deleteBySymbol(String symbol) {
        if (symbol == null) {
            return false;
        }
        return dataStore.remove(symbol.toUpperCase()) != null;
    }
    
    @Override
    public boolean existsBySymbol(String symbol) {
        if (symbol == null) {
            return false;
        }
        return dataStore.containsKey(symbol.toUpperCase());
    }
    
    @Override
    public void deleteAll() {
        dataStore.clear();
    }
    
    @Override
    public long count() {
        return dataStore.size();
    }
    
    @Override
    public List<String> getAllSymbols() {
        return new ArrayList<>(dataStore.keySet());
    }
}