
package org.example.bikeprontabackend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiagnosticoBanco {

    @Bean
    CommandLineRunner verificarBanco(DataSource dataSource) {
        return args -> {
            System.out.println("===== DIAGNOSTICO DO BANCO =====");

            try (Connection conexao = dataSource.getConnection();
                 Statement stmt = conexao.createStatement()) {

                String[] consultas = {
                        "SELECT current_database() AS resultado",
                        "SELECT current_user AS resultado",
                        "SELECT current_schema() AS resultado",
                        "SELECT current_setting('search_path') AS resultado",
                        "SELECT to_regclass('public.bicicletas') AS resultado",
                        "SELECT COUNT(*) AS resultado FROM public.bicicletas"
                };

                for (String sql : consultas) {
                    System.out.println("SQL: " + sql);

                    try (ResultSet rs = stmt.executeQuery(sql)) {
                        if (rs.next()) {
                            System.out.println("RESULTADO: "
                                    + rs.getString("resultado"));
                        }
                    } catch (Exception e) {
                        System.out.println("ERRO: " + e.getMessage());
                    }
                }
            }

            System.out.println("===== FIM DO DIAGNOSTICO =====");
        };
    }
}
