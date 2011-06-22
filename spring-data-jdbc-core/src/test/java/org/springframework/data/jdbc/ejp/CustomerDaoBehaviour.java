package org.springframework.data.jdbc.ejp;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.ejp.domain.Customer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations="classpath:ejp-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerDaoBehaviour {

	@Autowired
	private CustomerDao customerDao; 
	
	
	@Test
	public void shouldFindAllInsertedCustomers() {
		
		List<Customer> allCustomers = customerDao.findAllCustomers();
		assertThat(allCustomers.size(), is(2));
	}
	
	@Test
	public void shouldSaveNewCustomer() {
		Customer customer = new Customer();
		customer.setFirstName("Alex");
		customer.setLastName("Soto");
		
		customerDao.saveCustomer(customer);
		List<Customer> customers = customerDao.findAllCustomers();
		assertThat(customer.getId(), notNullValue());
		assertThat(customers.size(), is(3));
		
		deleteCustomers(customer);
		
	}
	
	@Test
	public void shouldDeleteACostumer() {
		
		Customer customer = new Customer();
		customer.setFirstName("Alex");
		customer.setLastName("Soto");
		
		customerDao.saveCustomer(customer);
		customerDao.deleteCustomer(customer);
		
		List<Customer> customers = customerDao.findAllCustomers();
		assertThat(customers.size(), is(2));
		
		
	}
	
	@Test
	public void shouldFindCustomersByExample() {
		Customer customer = new Customer();
		customer.setFirstName("Thomas");
		
		List<Customer> customers = customerDao.findCustomerByExample(customer);
		
		assertThat(customers.size(), is(1));
		Customer foundCustomer = customers.get(0);
		assertThat(foundCustomer.getLastName(), is("Risberg"));
		
	}
	
	@Test
	public void shouldFindCustomerById() {
		
		Customer customer = customerDao.findCustomerById(1L);
		assertThat(customer.getFirstName(), is("Mark"));
		
	}
	
	@Test
	public void shouldFindAllCustomersOrderedByName() {
		List<Customer> customers = customerDao.findAllCustomersOrderedByName();
		
		assertThat(customers.size(), is(2));
		assertThat(customers.get(0).getFirstName(), is("Mark"));
		assertThat(customers.get(1).getFirstName(), is("Thomas"));
		
	}
	
	@Test
	public void shouldCountNumberCustomers() {
		
		int numberCustomers = this.customerDao.countCustomers();
		assertThat(numberCustomers, is(2));
	}

	private void deleteCustomers(Customer...insertedCustomers) {
		for (Customer customer : insertedCustomers) {
			customerDao.deleteCustomer(customer);
		}
	}
	
}
