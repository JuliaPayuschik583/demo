package com.example.demo.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CurrencyRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Integer getFactor(String fromCurrency, String toCurrency) {
        String sql = "SELECT factor FROM currencies WHERE from_currency = ? AND to_currency = ?";
        Integer factor = jdbcTemplate.queryForObject(sql, new Object[]{fromCurrency, toCurrency}, Integer.class);
        System.out.println(factor);
        return factor;
    }
}
