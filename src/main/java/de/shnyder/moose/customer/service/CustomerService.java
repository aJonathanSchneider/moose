package de.shnyder.moose.customer.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.shnyder.moose.MooseError;
import de.shnyder.moose.customer.domain.model.BaseCustomerDataDAO;
import de.shnyder.moose.customer.domain.model.CustomerDataRepository;

/**
 * CustomerService
 */
@Service
public class CustomerService {

	@Autowired
	private CustomerDataRepository customerDataRepository;

	public boolean isCustomerRegistered(String customername) {
		return customerDataRepository.findByCustomername(customername) != null;
	}

	@Transactional
	public BaseCustomerDataDAO registerCustomer(String customername) throws MooseError {
		if(this.isCustomerRegistered(customername)){
			throw new MooseError("tried to register a customer that already exists");
		}
		BaseCustomerDataDAO baseCustomerDataDAO = new BaseCustomerDataDAO();
		baseCustomerDataDAO.setCustomername(customername);
		customerDataRepository.save(baseCustomerDataDAO);
		return baseCustomerDataDAO;
	}

	public void deleteCustomerById(long id) throws MooseError {
		customerDataRepository.deleteById(id);
	}

	public Iterable<BaseCustomerDataDAO> listAllCustomers() throws MooseError {
		return customerDataRepository.findAll();
	}

	public BaseCustomerDataDAO getCustomerById(long id) throws MooseError {
		Optional<BaseCustomerDataDAO> optionalDAO = customerDataRepository.findById(id);
		if(optionalDAO.isPresent()) return optionalDAO.get();
		throw new MooseError("customer not found", MooseError.ERR_NOT_FOUND);
	}

	@Transactional
	public BaseCustomerDataDAO updateCustomer(BaseCustomerDataDAO dao) {
		BaseCustomerDataDAO repoDAO = customerDataRepository.findById(dao.getId()).get();
		repoDAO.setCustomername(dao.getCustomername());
		customerDataRepository.save(repoDAO);
		return repoDAO;
	}
}