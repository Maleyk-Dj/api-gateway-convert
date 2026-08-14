package com.maleyk.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${services.flow-manager.name}")
    private String flowManagerServiceName;

    @Value("${services.flow-manager.path}")
    private String flowManagerPath;

    @Value("${services.subscription-service.name}")
    private String subscriptionServiceName;

    @Value("${services.subscription-service.path}")
    private String subscriptionServicePath;

    @Value("${services.flow-manager.route-id}")
    private String flowManagerRouteId;

    @Value("${services.subscription-service.route-id}")
    private String subscriptionServiceRouteId;


    @Bean
    public RouterFunction<ServerResponse> flowManagerRoutes() {
        return GatewayRouterFunctions.route(flowManagerRouteId)
                .route(
                        RequestPredicates.path(flowManagerPath).or(RequestPredicates.path(flowManagerPath + "/**")),
                        http()
                )
                .filter(UserLoginHeaderFilter.addUserLoginHeader())
                .filter(lb(flowManagerServiceName))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> subscriptionServiceRoutes() {
        return GatewayRouterFunctions.route(subscriptionServiceRouteId)
                .route(RequestPredicates.path(subscriptionServicePath + "/**"), http())
                .filter(UserLoginHeaderFilter.addUserLoginHeader())
                .filter(lb(subscriptionServiceName))
                .build();
    }
}