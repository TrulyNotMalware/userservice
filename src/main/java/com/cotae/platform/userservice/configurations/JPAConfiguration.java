package com.cotae.platform.userservice.configurations;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = "com.cotae.platform.userservice.dao",
//        transactionManagerRef = "jpaTransactionManager"
//)
//@PropertySource("classpath:application.yaml")
//@RequiredArgsConstructor
//public class JPAConfiguration{
//    @Value("${spring.datasource.driver-class-name}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String userName;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Value("${spring.jpa.hibernate.ddl-auto}")
//    private String ddlAuto;
//
//    private final Environment environment;
//
//    @Primary
//    @Bean
//    public DataSource datasource(){
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(this.driverClassName);
//        dataSource.setUrl(this.url);
//        dataSource.setUsername(this.userName);
//        dataSource.setPassword(this.password);
//        return dataSource;
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource){
//        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setPackagesToScan("com.cotae.platform.userservice");
//        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        bean.setJpaVendorAdapter(vendorAdapter);
//        bean.setJpaProperties(additionalProperties());
//        return bean;
//    }
//
//    Properties additionalProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
//        String dialect = environment.getProperty("spring.jpa.database-platform");
//        if (dialect != null)
//            properties.setProperty("hibernate.dialect", dialect);
//        return properties;
//    }
//
//    @Bean
//    public PlatformTransactionManager jpaTransactionManager(DataSource datasource){
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory(datasource).getObject());
//        return transactionManager;
//    }
//}

public class JPAConfiguration{

}