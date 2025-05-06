package com.company.route.service;

import org.springframework.stereotype.Service;

import com.company.common.database.entity.Route;
import com.company.route.repository.RouteRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class RouteService {

	private final RouteRepository routeRepository;
	
	/**
     * 모든 Route 목록 조회
     * @return Flux<Route> - 비동기 스트림
     */
    public Flux<Route> findAllRoutes() {
        return routeRepository.findAll();
    }
}
