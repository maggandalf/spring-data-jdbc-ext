package org.springframework.data.jdbc.ejp;

import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import ejp.Database;
import ejp.Result;

public interface EjpJdbcOperations {

	String getDatabaseName();
	
	Database newDatabase();
	
	JdbcOperations getJdbcOperations();
	
	int deleteObject(Object object);
	
	int deleteObject(Object object, String externalClauses, Object... externalClausesParameters);
	
	<T> List<T> exeuteQuery(final String sql, final RowMapper<T> rowMapper);
	
	int executeUpdate(String sql);
	
	int executeUpdate(String sql, List keys);
	
	void loadAssociations(Object object);
	
	<T>List<T> parameterizedQuery(String sql, RowMapper<T> rowMapper, Object ... parameters);
	
	int parameterizedUpdate(String sql, List keys, Object ... parameters);
	
	int parameterizedUpdate(String sql, Object ... parameters);
	
	<T> List<T> queryForObject(Class<T> cs, RowMapper<T> rowMapper); 
	
	<T> List<T> queryForObject(Class<T> cs, String externalClauses, RowMapper<T> rowMapper, Object... externalClausesParameters);
	
	<T> List<T> queryForObject(T object, RowMapper<T> rowMapper);
	
	<T> List<T> queryForObject(T object, String externalClauses, RowMapper<T> rowMapper, Object... externalClausesParameters);
	
	int queryForInt(String sql);
	
	int saveObject(Object object);
	
	int saveObject(Object object, String externalClauses, Object... externalClausesParameters);
	
	<T> int[] batchUpdate(List<T> objectList);
	
}
