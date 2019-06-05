package de.shnyder.moose.customer.domain.model;

import org.springframework.data.repository.CrudRepository;

/**
 * CustomerDataRepository
 */
public interface CustomerDataRepository extends CrudRepository<BaseCustomerDataDAO, Long> {

	BaseCustomerDataDAO findByCustomername(String customername);
	
}