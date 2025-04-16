package com.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test for ProductCacheService using external Redis.
 * Redis must be running locally (e.g., via Docker or native install).
 */
@SpringBootTest
public class ProductCacheServiceIT {

    private static final Logger log = LoggerFactory.getLogger(ProductCacheServiceIT.class);

    @Autowired
    private ProductCacheService cacheService;

    @BeforeEach
    void setup() {
        log.info("Starting integration test with Redis at localhost:6379 (external instance)");
    }

    @Test
    @DisplayName("[Redis Ext] Should save and retrieve product from Redis")
    void testSaveAndRetrieveProduct() {
        Product product = new Product("P101", "Monitor", 350.0);
        cacheService.saveProduct(product);

        Product cached = cacheService.getProduct("P101");

        assertThat(cached).isNotNull();
        assertThat(cached.getName()).isEqualTo("Monitor");
    }

    @Test
    @DisplayName("[Redis Ext] Should delete product from Redis")
    void testDeleteProduct() {
        Product product = new Product("P202", "Mouse", 20.0);
        cacheService.saveProduct(product);

        cacheService.deleteProduct("P202");
        Product cached = cacheService.getProduct("P202");

        assertThat(cached).isNull();
    }
}
