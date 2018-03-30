package com.ruijie.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 
 * jpa Դ�����
 * 
 * @author wyx
 * 
 */
@Configuration
@EnableJpaRepositories("com.ruijie.repositories")
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        // 这里可以设置数据连接池
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://192.168.54.137:5432/jpa-test");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("abc123_");
//        return dataSource;
        
        DruidDataSource dataSource =  new DruidDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://192.168.54.137:5432/jpa-test");
        dataSource.setUsername("postgres");
        dataSource.setPassword("abc123_");
        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.POSTGRESQL);
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(true);
        return jpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(jpaVendorAdapter());
        factory.setPackagesToScan("com.ruijie.entity");
        return factory;
    }
}
