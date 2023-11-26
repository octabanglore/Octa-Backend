package com.octa.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.octa.transaction.platform.AtomikosJtaPlatform;

import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class,DataSourceAutoConfiguration.class})
public class OctaTransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OctaTransactionServiceApplication.class, args);
	}
	
	
	@Bean(name = "userTransaction")
	UserTransaction userTransaction() throws Throwable {
		UserTransactionImp userTransactionImp = new UserTransactionImp();
		userTransactionImp.setTransactionTimeout(20000);
		return userTransactionImp;
	}

	@Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
	TransactionManager atomikosTransactionManager() throws Throwable {
		UserTransactionManager userTransactionManager = new UserTransactionManager();
		userTransactionManager.setForceShutdown(false);
		userTransactionManager.setTransactionTimeout(20000);
		AtomikosJtaPlatform.transactionManager = userTransactionManager;
		return userTransactionManager;
	}

	@Bean(name = "transactionManager")
	@DependsOn({ "userTransaction", "atomikosTransactionManager" })
	PlatformTransactionManager transactionManager() throws Throwable {
		UserTransaction userTransaction = userTransaction();
		AtomikosJtaPlatform.transaction = userTransaction;
		TransactionManager atomikosTransactionManager = atomikosTransactionManager();
		atomikosTransactionManager.setTransactionTimeout(20000);
		return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
	}

}
