package org.springframework.data.jdbc.ejp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import ejp.Database;
import ejp.DatabaseException;

public class EjpJdbcTemplate implements EjpJdbcOperations {

	private String databaseName;
	private JdbcTemplate jdbcTemplate;
	
	public EjpJdbcTemplate(DataSource dataSource) {
		this(null, new JdbcTemplate(dataSource));
	}

	public EjpJdbcTemplate(String databaseName, DataSource dataSource) {
		this(databaseName, new JdbcTemplate(dataSource));
	}
	
	public EjpJdbcTemplate(String databaseName, JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.databaseName = databaseName;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}

	public JdbcOperations getJdbcOperations() {
		return this.jdbcTemplate;
	}

	public int deleteObject(final Object object) {
		int rowsAffected = jdbcTemplate.execute(new ConnectionCallback<Integer>() {

			public Integer doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				Database database = newDatabase(connection);
				try {
					return database.deleteObject(object);
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
			}
			
		});
		return rowsAffected;
	}

	
	
	public int deleteObject(final Object object, final String externalClauses,
			final Object... externalClausesParameters) {
		int rowsAffected = jdbcTemplate.execute(new ConnectionCallback<Integer>() {

			public Integer doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				Database database = newDatabase(connection);
				try {
					return database.deleteObject(object, externalClauses, externalClausesParameters);
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
			}
			
		});
		return rowsAffected;
	}

	public <T> List<T> exeuteQuery(final String sql, final RowMapper<T> rowMapper) {
		
		List<T> results = jdbcTemplate.execute(new ConnectionCallback<List<T>>() {

			public List<T> doInConnection(Connection con) throws SQLException,
					DataAccessException {
				RowMapperResultSetExtractor<T> extractor = 
					new RowMapperResultSetExtractor<T>(rowMapper);
			
				Database database = newDatabase(con);
				try {
					ResultSet resultSet = database.executeQuery(sql).getResultSet();
					List<T> list = extractor.extractData(resultSet);
					JdbcUtils.closeResultSet(resultSet);
					return list;
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
				
			}
		});
		
		return results;
	}

	public int executeUpdate(final String sql) {
		int rowsAffected = jdbcTemplate.execute(new ConnectionCallback<Integer>() {

			public Integer doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				Database database = newDatabase(connection);
				try {
					return database.executeUpdate(sql);
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
			}
			
		});
		return rowsAffected;
	}

	public int executeUpdate(final String sql, final List keys) {
		int rowsAffected = jdbcTemplate.execute(new ConnectionCallback<Integer>() {

			public Integer doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				Database database = newDatabase(connection);
				try {
					return database.executeUpdate(sql, keys);
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
			}
			
		});
		return rowsAffected;
	}

	public void loadAssociations(final Object object) {
		
		jdbcTemplate.execute(new ConnectionCallback<Class<Void>>() {

			public Class<Void> doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				Database database = newDatabase(connection);
				try {
					database.loadAssociations(object);
					return Void.TYPE;
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
			}
			
		});
		
	}

	public <T>List<T> parameterizedQuery(final String sql, final RowMapper<T> rowMapper, final Object... parameters) {

		List<T> results = jdbcTemplate.execute(new ConnectionCallback<List<T>>() {

			public List<T> doInConnection(Connection con) throws SQLException,
					DataAccessException {
				RowMapperResultSetExtractor<T> extractor = 
					new RowMapperResultSetExtractor<T>(rowMapper);
			
				Database database = newDatabase(con);
				try {
					ResultSet resultSet = database.parameterizedQuery(sql, parameters).getResultSet();
					List<T> list = extractor.extractData(resultSet);
					JdbcUtils.closeResultSet(resultSet);
					return list;
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
				
			}
		});
		
		return results;
		
	}

	public int parameterizedUpdate(final String sql, final List keys, final Object... parameters) {
		int rowsAffected = jdbcTemplate.execute(new ConnectionCallback<Integer>() {

			public Integer doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				Database database = newDatabase(connection);
				try {
					return database.parameterizedUpdate(sql, keys, parameters);
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
			}
			
		});
		return rowsAffected;
	}

	public int parameterizedUpdate(final String sql, final Object... parameters) {
		int rowsAffected = jdbcTemplate.execute(new ConnectionCallback<Integer>() {

			public Integer doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				Database database = newDatabase(connection);
				try {
					return database.parameterizedUpdate(sql, parameters);
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
			}
			
		});
		return rowsAffected;
	}

	public <T> List<T> queryForObject(final Class<T> cs, final RowMapper<T> rowMapper) {
		List<T> results = jdbcTemplate.execute(new ConnectionCallback<List<T>>() {

			public List<T> doInConnection(Connection con) throws SQLException,
					DataAccessException {
				RowMapperResultSetExtractor<T> extractor = 
					new RowMapperResultSetExtractor<T>(rowMapper);
				
				Database database = newDatabase(con);
				try {
					ResultSet resultSet = database.queryObject(cs).getResultSet();
					List<T> list = extractor.extractData(resultSet);
					
					
					JdbcUtils.closeResultSet(resultSet);
					return list;
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
				
			}
		});
		
		return results;
	}

	public <T> List<T> queryForObject(final Class<T> cs, final String externalClauses, final RowMapper<T> rowMapper,
			final Object... externalClausesParameters) {
		List<T> results = jdbcTemplate.execute(new ConnectionCallback<List<T>>() {

			public List<T> doInConnection(Connection con) throws SQLException,
					DataAccessException {
				RowMapperResultSetExtractor<T> extractor = 
					new RowMapperResultSetExtractor<T>(rowMapper);
			
				Database database = newDatabase(con);
				try {
					ResultSet resultSet = database.queryObject(cs, externalClauses, externalClausesParameters).getResultSet();
					List<T> list = extractor.extractData(resultSet);
					JdbcUtils.closeResultSet(resultSet);
					return list;
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
				
			}
		});
		
		return results;
	}

	public <T> List<T> queryForObject(final T object, final RowMapper<T> rowMapper) {
		List<T> results = jdbcTemplate.execute(new ConnectionCallback<List<T>>() {

			public List<T> doInConnection(Connection con) throws SQLException,
					DataAccessException {
				RowMapperResultSetExtractor<T> extractor = 
					new RowMapperResultSetExtractor<T>(rowMapper);
			
				Database database = newDatabase(con);
				try {
					ResultSet resultSet = database.queryObject(object).getResultSet();
					List<T> list = extractor.extractData(resultSet);
					JdbcUtils.closeResultSet(resultSet);
					return list;
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
				
			}
		});
		
		return results;
	}

	public <T> List<T> queryForObject(final T object, final String externalClauses, final RowMapper<T> rowMapper,
			final Object... externalClausesParameters) {
		List<T> results = jdbcTemplate.execute(new ConnectionCallback<List<T>>() {

			public List<T> doInConnection(Connection con) throws SQLException,
					DataAccessException {
				RowMapperResultSetExtractor<T> extractor = 
					new RowMapperResultSetExtractor<T>(rowMapper);
			
				Database database = newDatabase(con);
				try {
					ResultSet resultSet = database.queryObject(object, externalClauses, externalClausesParameters).getResultSet();
					List<T> list = extractor.extractData(resultSet);
					JdbcUtils.closeResultSet(resultSet);
					return list;
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
				
			}
		});
		
		return results;
	}

	public int saveObject(final Object object) {
		int rowsAffected = jdbcTemplate.execute(new ConnectionCallback<Integer>() {

			public Integer doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				Database database = newDatabase(connection);
				try {
					return database.saveObject(object);
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
			}
			
		});
		return rowsAffected;
	}

	public int saveObject(final Object object, final String externalClauses,
			final Object... externalClausesParameters) {
		int rowsAffected = jdbcTemplate.execute(new ConnectionCallback<Integer>() {

			public Integer doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				Database database = newDatabase(connection);
				try {
					return database.saveObject(object, externalClauses, externalClausesParameters);
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
			}
			
		});
		return rowsAffected;
	}

	
	public <T> int[] batchUpdate(final List<T> objectList) {
		
		int[] affectedRows = jdbcTemplate.execute(new ConnectionCallback<int[]>() {

			public int[] doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				Database database = newDatabase(connection);
				try {
					int[] affectedRows = new int[objectList.size()];
					database.beginBatch();
					for(int i=0;i<objectList.size();i++) {
						affectedRows[i] = database.saveObject(objectList.get(i));						
					}
					database.executeBatch();
					database.endBatch();
					return affectedRows;
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
			}
			
		});
		
		return affectedRows;
		
	}
	
	public int queryForInt(final String sql) {
		int results = jdbcTemplate.execute(new ConnectionCallback<Integer>() {

			public Integer doInConnection(Connection con) throws SQLException,
					DataAccessException {

			
				Database database = newDatabase(con);
				try {
					ResultSet resultSet = database.executeQuery(sql).getResultSet();
					
					if(resultSet.next()) {
						RowMapper<Integer> singleColumnRowMapper = getSingleColumnRowMapper(Integer.class);
						return singleColumnRowMapper.mapRow(resultSet, 0);
					} else {
						throw new IncorrectResultSizeDataAccessException(1);
					}
					
				} catch (DatabaseException e) {
					throw new SQLException(e.getMessage(), e.getCause());
				} finally {
					closeDatabase(database);
				}
				
			}
		});
		
		return results;
	}
	
	
	public Database newDatabase() {
		return new Database(databaseName, DataSourceUtils.getConnection(this.jdbcTemplate.getDataSource()));
	}
	
	protected <T> RowMapper<T> getSingleColumnRowMapper(Class<T> requiredType) {
        return new SingleColumnRowMapper<T>(requiredType);
	}
	
	private void closeDatabase(Database database) throws SQLException {
		try {
			database.close();
		} catch (DatabaseException e) {
			throw new SQLException(e.getMessage(), e.getCause());
		}
	}
	
	private Database newDatabase(Connection connection) {
		return new Database(databaseName, connection);
	}
	
}
