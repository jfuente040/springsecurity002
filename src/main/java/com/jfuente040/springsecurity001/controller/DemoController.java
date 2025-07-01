package com.jfuente040.springsecurity001.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/demo")
public class DemoController {

    private final static Logger logger = LoggerFactory.getLogger(DemoController.class);

    public String demo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Datos del usuario autenticado: {}", auth.getPrincipal());
        logger.info("Datos de los Persmisos del usuario autenticado: {}", auth.getAuthorities());
        logger.info("User info: " + auth.getName() + " has role: " + auth.getAuthorities());
        logger.info("Is authenticated: " + auth.isAuthenticated());
        return "Hello, this is a demo endpoint!";
    }

}
