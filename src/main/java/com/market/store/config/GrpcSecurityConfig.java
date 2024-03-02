package com.market.store.config;

import com.market.store.pkg.auth.AccessToken;
import com.market.store.pkg.auth.AccessTokenProvider;
import com.market.store.proto.ProductGrpc;
import com.market.store.proto.StoreGrpc;
import io.grpc.channelz.v1.ChannelzGrpc;
import io.grpc.health.v1.HealthGrpc;
import io.grpc.reflection.v1alpha.ServerReflectionGrpc;
import java.util.ArrayList;
import java.util.List;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.check.AccessPredicate;
import net.devh.boot.grpc.server.security.check.AccessPredicateVoter;
import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource;
import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class GrpcSecurityConfig {
  @Bean
  GrpcSecurityMetadataSource grpcSecurityMetadataSource() {
    // Default rule
    final ManualGrpcSecurityMetadataSource source = new ManualGrpcSecurityMetadataSource();
    source.setDefault(AccessPredicate.authenticated());

    // Domain services
    source.set(StoreGrpc.getListStoreMethod(), AccessPredicate.permitAll());
    source.set(StoreGrpc.getGetStoreMethod(), AccessPredicate.permitAll());
    source.set(ProductGrpc.getListProductMethod(), AccessPredicate.permitAll());
    source.set(ProductGrpc.getGetProductMethod(), AccessPredicate.permitAll());

    // Common services
    source.set(HealthGrpc.getServiceDescriptor(), AccessPredicate.permitAll());
    source.set(ServerReflectionGrpc.getServiceDescriptor(), AccessPredicate.permitAll());
    source.set(ChannelzGrpc.getServiceDescriptor(), AccessPredicate.permitAll());

    return source;
  }

  @Bean
  AccessDecisionManager grpcAccessDecisionManager() {
    final List<AccessDecisionVoter<?>> voters = new ArrayList<>();
    voters.add(new AccessPredicateVoter());
    return new UnanimousBased(voters);
  }

  @Bean
  AuthenticationManager grpcAuthenticationManager() {
    final List<AuthenticationProvider> providers = new ArrayList<>();
    providers.add(new AccessTokenProvider());
    return new ProviderManager(providers);
  }

  @Bean
  GrpcAuthenticationReader grpcAuthenticationReader() {
    return new BearerAuthenticationReader(accessToken -> new AccessToken(accessToken, null));
  }
}
