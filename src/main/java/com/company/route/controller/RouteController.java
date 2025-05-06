package com.company.route.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.database.entity.Route;
import com.company.route.service.RouteService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class RouteController {

	private final RouteService routeService;

    /**
     * 모든 라우트 목록 조회 API
     */
    @GetMapping
    public Flux<Route> getAllRoutes() {
        return routeService.findAllRoutes();
    }
}
