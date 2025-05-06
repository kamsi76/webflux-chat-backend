package com.company.route.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.company.common.database.entity.Route;

/**
 * 화면(Route)용 PostgreSQL R2DBC Repository
 */
public interface RouteRepository extends ReactiveCrudRepository<Route, Long> {
}
