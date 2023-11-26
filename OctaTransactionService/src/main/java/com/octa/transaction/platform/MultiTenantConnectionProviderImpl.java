package com.octa.transaction.platform;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.atomikos.datasource.ResourceException;
import com.octa.transaction.platform.TenantAwareRequestContext.TenantScopeInfo;

public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {
	
	private static final long serialVersionUID = -7518484240867383353L;

	private static final Logger logger = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class);
	public static Map<String, DataSource> dataSources = new HashMap<>();
	

	@Override
	public boolean isUnwrappableAs(Class<?> unwrapType) {
		
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		
		return null;
	}

	@Override
	public Connection getAnyConnection() throws SQLException {
		logger.info("getting default connection");
		return getConnection(TenantAwareRequestContext.DEFAULT_MASTER_CONTEXT.getDatabaseURL());
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		if(!connection.isClosed()) {
			connection.close();
		}
	}

	@Override
	public Connection getConnection(String tenantIdentifierSchema) throws SQLException {
		TenantScopeInfo requestContext = TenantAwareRequestContext.getCurrentTenantScope();
		if(null == requestContext) {
			throw new IllegalStateException("TenantScopeInfo is null {}");
		}
		RetryTemplate retry = 	createRetryTemplate();
		return 	retry.execute(new RetryCallback<Connection, SQLException>() {
			@Override
			public Connection doWithRetry(RetryContext context) throws SQLException {
				Connection conn = dataSources.get(requestContext.getDatabaseURL()).getConnection();
				try(Statement stmt = conn.createStatement()){
					logger.info("USE {}",tenantIdentifierSchema);
					stmt.execute("SET search_path TO \"" + tenantIdentifierSchema + "\"");
				}catch (SQLException | ResourceException sqle) {
					throw sqle;
				}
				return conn;
			}

		});

	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		if(!connection.isClosed()) {
			connection.close();
		}
		
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}
	
	private  RetryTemplate createRetryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();

		// Configure retry behavior
		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(1000L); // 1 second delay between retries
		retryTemplate.setBackOffPolicy(backOffPolicy);

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(3); // Retry a maximum of 3 times
		retryTemplate.setRetryPolicy(retryPolicy);

		return retryTemplate;
	}

}
