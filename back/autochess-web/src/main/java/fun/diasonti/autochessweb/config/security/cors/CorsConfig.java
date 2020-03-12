package fun.diasonti.autochessweb.config.security.cors;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    private final String[] allowedOrigins;

    public CorsConfig(@Value("${cors.allowed-origins}") String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public CorsFilter corsFilter() {
        final CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowedOrigins(ImmutableList.copyOf(allowedOrigins));
        config.setAllowCredentials(true);
        final CorsFilter filter = new CorsFilter(request -> config);
        filter.setCorsProcessor(new DefaultCorsProcessor());
        return filter;
    }

}
