package io.aiur.oss.db.jdbc.repo;

import io.aiur.oss.db.jdbc.annotation.ExposeId;
import io.aiur.oss.db.jdbc.jdbc.convert.JdbcTypeConverter;
import io.aiur.oss.db.jdbc.jdbc.convert.ProjectionService;
import io.aiur.oss.db.jdbc.jdbc.convert.impl.EnumJdbcTypeConverter;
import io.aiur.oss.db.jdbc.jdbc.convert.impl.joda.converter.JodaJdbcTypeConverter;
import io.aiur.oss.db.jdbc.jdbc.convert.impl.postgres.PostgresJsonJdbcTypeConverter;
import io.aiur.oss.db.jdbc.jdbc.mapping.SqlCache;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import other.StreamUtils;

import javax.inject.Inject;
import java.util.Set;

/**
 * Created by dave on 12/15/15.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class JdbcRepoConfig extends RepositoryRestConfigurerAdapter {

    @Inject
    private PersistentEntities persistentEntities;

    @Value("${app.basePackage:io.aiur}")
    private String basePackage;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        super.configureRepositoryRestConfiguration(config);
        StreamUtils.stream(persistentEntities.getManagedTypes())
                .map(ti -> ti.getType())
                .forEach(config::exposeIdsFor);

        Set<Class<?>> exposed = new Reflections(basePackage).getTypesAnnotatedWith(ExposeId.class);
        exposed.forEach(config::exposeIdsFor);
    }

    @Bean
    @ConditionalOnMissingBean(type = "io.aiur.oss.db.jdbc.jdbc.mapping.SqlCache")
    public SqlCache jdbcSqlCache(){
        return new SqlCache();
    }

    @Bean
    @ConditionalOnMissingBean(type = "io.aiur.oss.db.jdbc.jdbc.convert.ProjectionService")
    public ProjectionService jdbcProjectionService(){
        return new ProjectionService();
    }

    @Bean
    public JdbcTypeConverter postgresJsonJdbcTypeConverter(){
        return new PostgresJsonJdbcTypeConverter();
    }

    @Bean
    public JdbcTypeConverter jodaJdbcTypeConverter(){
        return new JodaJdbcTypeConverter();
    }

    @Bean
    public JdbcTypeConverter enumJdbcTypeConverter(){
        return new EnumJdbcTypeConverter();
    }

}
