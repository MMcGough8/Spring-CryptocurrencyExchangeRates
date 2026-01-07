package com.zipcoder.crypto_exchange.service;

import com.zipcoder.crypto_exchange.model.CryptoCurrency;
import java.util.List;
import java.util.Optional;

/**
 * Service interface defining business logic operations for cryptocurrency data.
 * Uses CoinMarketCap API for data retrieval.
 */
public interface CryptoCurrencyService {
    
    Optional<CryptoCurrency> getCryptoCurrency(String symbol);

    List<CryptoCurrency> getAllCryptoCurrencies();
    
    Optional<CryptoCurrency> addCryptoCurrency(String symbol);

    boolean removeCryptoCurrency(String symbol);

    Optional<CryptoCurrency> refreshCryptoCurrency(String symbol);
 
    void refreshAllCryptoCurrencies();

    boolean isApiConfigured();
}