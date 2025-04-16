package com.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.example.model.Product;
import com.redis.testcontainers.RedisContainer;

@SpringBootTest
@Testcontainers
public class ProductCacheServiceContainerIT {

    private static final Logger log = LoggerFactory.getLogger(ProductCacheServiceContainerIT.class);

    @Container
    static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7.2.4"));

    @DynamicPropertySource
    static void overrideRedisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    }

    @Autowired
    private ProductCacheService cacheService;

    @BeforeEach
    void setup() {
        log.info("Starting test with Redis at {}:{}", redisContainer.getHost(), redisContainer.getFirstMappedPort());
    }

    @Test
    @DisplayName("[Redis TC] Should save and retrieve product from Redis")
    void testSaveAndRetrieveProduct() {
        Product product = new Product("P101", "Monitor", 350.0);
        cacheService.saveProduct(product);

        Product cached = cacheService.getProduct("P101");

        assertThat(cached).isNotNull();
        assertThat(cached.getName()).isEqualTo("Monitor");
    }

    @Test
    @DisplayName("[Redis TC] Should delete product from Redis")
    void testDeleteProduct() {
        Product product = new Product("P202", "Mouse", 20.0);
        cacheService.saveProduct(product);

        cacheService.deleteProduct("P202");
        Product cached = cacheService.getProduct("P202");

        assertThat(cached).isNull();
    }
}
