package com.example.SpringAICode;

import com.pgvector.PGvector;
import org.postgresql.PGConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class PgVectorConfig {

    @Bean
    public Boolean registerPgVectorTypes(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PGvector.addVectorType(connection);
        }
        return true;
    }
}