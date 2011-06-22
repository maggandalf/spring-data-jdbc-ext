package org.springframework.data.jdbc.ejp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jdbc.ejp.domain.Customer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public class EjpCustomerDao implements CustomerDao {

	@Autowired
	private EjpJdbcTemplate template;
	
	public Customer findCustomerById(Long id) {
		List<Customer> customers = this.template.queryForObject(Customer.class, "where :id = ?", BeanPropertyRowMapper.newInstance(Customer.class), id);
	
		if(customers.size() == 1) {
			return customers.get(0);
		} else {
			throw new IncorrectResultSizeDataAccessException(1, customers.size());
		}
	
	}

	public void saveCustomer(Customer customer) {
		this.template.saveObject(customer);
	}

	public void deleteCustomer(Customer customer) {
		this.template.deleteObject(customer);
	}

	public int countCustomers() {
		String query = "SELECT COUNT(*) FROM Customer";
		return this.template.queryForInt(query);
				
	}

	public List<Customer> findAllCustomersOrderedByName() {
		return this.template.queryForObject(Customer.class, "ORDER BY first_name",BeanPropertyRowMapper.newInstance(Customer.class));
	}
	
	public List<Customer> findCustomerByExample(Customer customer) {
		return this.template.queryForObject(customer, BeanPropertyRowMapper.newInstance(Customer.class));
	}

	public void addBatch(List<Customer> customers) {
		this.template.batchUpdate(customers);
	}

	public List<Customer> findAllCustomers() {
		return this.template.queryForObject(Customer.class, BeanPropertyRowMapper.newInstance(Customer.class));
	}

}
