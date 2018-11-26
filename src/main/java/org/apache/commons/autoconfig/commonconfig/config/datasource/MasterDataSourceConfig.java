package org.apache.commons.autoconfig.commonconfig.config.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@MapperScan(basePackages = MasterDataSourceConfig.MASTER_PACKAGE,sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {

    private Logger logger = LoggerFactory.getLogger(MasterDataSourceConfig.class);

    static final String MASTER_PACKAGE = "com.jcl.pbcms.mapper";
    private static final String MASTER_MAPPER_LOCAL = "classpath:mybatis/mapper/*.xml";

    @Autowired
    private WallFilter wallFilter;

    @Bean(name = "masterDataSource")
    @Primary
    @ConfigurationProperties(prefix = "druid.datasource")
    public DataSource masterDruidDataSource(){
        DruidDataSource datasource = new DruidDataSource();
        // filter
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(wallFilter);
        datasource.setProxyFilters(filters);
        return datasource;
    }


    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(){
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(masterDruidDataSource());

        try {
            sessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
            sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MASTER_MAPPER_LOCAL));
            return sessionFactoryBean.getObject();
        } catch (Exception e) {
            logger.error("配置主库的SqlSessionFactory失败，error:{}",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager(){
        return new DataSourceTransactionManager(masterDruidDataSource());
    }

    @Bean(name = "wallFilter")
    @DependsOn("wallConfig")
    public WallFilter wallFilter(WallConfig wallConfig){
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig);
        return wallFilter;
    }

    @Bean(name = "wallConfig")
    public WallConfig wallConfig(){
        WallConfig wallConfig = new WallConfig();
        wallConfig.setMultiStatementAllow(true);
        wallConfig.setNoneBaseStatementAllow(true);
        return wallConfig;
    }
}