package com.example.service;

import com.example.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProductCacheService {

    private static final Logger log = LoggerFactory.getLogger(ProductCacheService.class);
    private static final String PREFIX = "product::";

    private final RedisTemplate<String, Product> redisTemplate;

    public ProductCacheService(RedisTemplate<String, Product> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveProduct(Product product) {
        String key = PREFIX + product.getId();
        redisTemplate.opsForValue().set(key, product, 10, TimeUnit.MINUTES);
        log.info("Saved product {} to Redis with key {}", product.getId(), key);
    }

    public Product getProduct(String id) {
        String key = PREFIX + id;
        Product product = redisTemplate.opsForValue().get(key);
        log.info("Fetched product {} from Redis with key {}", id, key);
        return product;
    }

    public void deleteProduct(String id) {
        String key = PREFIX + id;
        redisTemplate.delete(key);
        log.info("Deleted product from Redis with key {}", key);
    }
}
