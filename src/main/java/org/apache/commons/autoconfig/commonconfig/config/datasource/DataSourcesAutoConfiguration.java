package org.apache.commons.autoconfig.commonconfig.config.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;


@Configuration
@ConditionalOnBean({MasterDataSourceConfig.class,CustomDataSourceConfig.class})
public class DataSourcesAutoConfiguration  implements TransactionManagementConfigurer{

    @Autowired
    @Qualifier("masterTransactionManager")
    private DataSourceTransactionManager masterTransactionManager;

    @Autowired
    @Qualifier("customTransactionManager")
    private DataSourceTransactionManager customTransactionManager;

    @Autowired
    @Qualifier("authTransactionManager")
    private DataSourceTransactionManager authTransactionManager;

    @Bean(name = "transactionManager")
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {

        PlatformTransactionManager platformTransactionManager = new  ChainedTransactionManager(masterTransactionManager,customTransactionManager,authTransactionManager);
        return platformTransactionManager;
    }

}
