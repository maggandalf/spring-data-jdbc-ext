package org.springframework.data.jdbc.ejp;

import java.util.List;

import org.springframework.data.jdbc.ejp.domain.Customer;


public interface CustomerDao {

	Customer findCustomerById(Long id);
	
	void saveCustomer(Customer customer);
	
	void deleteCustomer(Customer customer);
	
	int countCustomers();
	
	List<Customer> findCustomerByExample(Customer customer);
	
	void addBatch(List<Customer> customers);
	
	List<Customer> findAllCustomers();
	
	List<Customer> findAllCustomersOrderedByName();
}
