package org.apache.commons.autoconfig.commonconfig.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = AuthDataSourceConfig.CUSTOM_PACKAGE,sqlSessionFactoryRef = "authSqlSessionFactory")
public class AuthDataSourceConfig {

    private Logger logger = LoggerFactory.getLogger(AuthDataSourceConfig.class);
    static final String CUSTOM_PACKAGE = "com.jcl.pbcms.authmapper";
    private static final String CUSTOM_MAPPER_LOCAL = "classpath:mybatis/authmapper/*.xml";

    @Bean(name = "authDataSource")
    @ConfigurationProperties(prefix = "auth.datasource")
    public DataSource authDruidDataSource(){
        return new DruidDataSource();
    }

    @Bean(name = "authSqlSessionFactory")
    public SqlSessionFactory authSqlSessionFactory(){
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(authDruidDataSource());

        try {
            sessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
            sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(CUSTOM_MAPPER_LOCAL));
            return sessionFactoryBean.getObject();
        } catch (Exception e) {
            logger.error("配置从库的SqlSessionFactory失败，error:{}",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Bean(name = "authTransactionManager")
    public DataSourceTransactionManager authTransactionManager(){
        return new DataSourceTransactionManager(authDruidDataSource());
    }
}
