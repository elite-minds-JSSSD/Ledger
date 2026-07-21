package com.techwing.ledger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Forwards all unknown routes to index.html so Angular's client-side router
 * can handle them (SPA fallback).
 */
@Controller
public class SpaController {

    @RequestMapping(value = {
            "/",
            "/login",
            "/register",
            "/app",
            "/app/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
