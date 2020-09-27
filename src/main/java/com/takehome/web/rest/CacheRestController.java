package com.takehome.web.rest;

import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheRestController {

    private final CacheManager cacheManager;

    public CacheRestController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping("/caches/reset")
    public ResponseEntity<String> resetAllCaches() {
        cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
