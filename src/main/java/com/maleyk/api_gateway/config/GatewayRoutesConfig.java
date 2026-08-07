package com.maleyk.api_gateway.config;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouterFunction<ServerResponse> flowManagerRoutes() {
        return GatewayRouterFunctions.route("flow-manager-route")
                .route(
                        RequestPredicates.path("/api/files").or(RequestPredicates.path("/api/files/**")),
                        http()
                )
                .filter(UserLoginHeaderFilter.addUserLoginHeader())
                .filter(lb("flow-manager"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> subscriptionServiceRoutes() {
        return GatewayRouterFunctions.route("subscription-service-route")
                .route(RequestPredicates.path("/api/subscriptions/**"), http())
                .filter(UserLoginHeaderFilter.addUserLoginHeader())
                .filter(lb("subscription-service"))
                .build();
    }
}