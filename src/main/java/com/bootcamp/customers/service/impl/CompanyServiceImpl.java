package com.bootcamp.customers.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.customers.clients.AccountsRestClient;
import com.bootcamp.customers.clients.CreditsRestClient;
import com.bootcamp.customers.dto.CompanyRequestDto;
import com.bootcamp.customers.dto.CompanyResponseDto;
import com.bootcamp.customers.dto.Message;
import com.bootcamp.customers.entity.Company;
import com.bootcamp.customers.repository.CompanyRepository;
import com.bootcamp.customers.service.CompanyService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Clase de implementación para la interfaz CompanyService
 */
@Service
public class CompanyServiceImpl implements CompanyService{
	
	@Autowired
	AccountsRestClient accountsRestClient;
	
	@Autowired
	CreditsRestClient creditsRestClient;

	@Autowired
    private CompanyRepository companyRepository;

	/**
	 * Método que devuelve todos los clientes de tipo empresarial dentro el repositorio.
	 * @return Flux<Company>
	 */
	@Override
	public Flux<Company> getAll() {
		return companyRepository.findAll();
	}

	/**
	 * Devuelve todos los clientes de tipo empresarial dentro el repositorio segun el id del cliente.
	 * @return Mono<Company>
	 */
	@Override
	public Mono<Company> getCompanyById(String companyId) {
		return companyRepository.findById(companyId);
	}

	/**
	 * Crea un cliente de tipo empresarial dentro del repositorio con los datos enviados en el body.
	 * @param companyRequestDto
	 * @return Mono<Company>
	 */
	@Override
	public Mono<Company> createCompany(CompanyRequestDto companyRequestDto) {
		Company company = new Company(null, companyRequestDto.getBusinessName(), companyRequestDto.getRuc(), companyRequestDto.getEmail()
				, companyRequestDto.getTelephone() , companyRequestDto.getHeadlines(), companyRequestDto.getSignatories(),"COMPANY","BUSINESS");
		return companyRepository.save(company);
	}

	/**
	 * Actualiza un cliente de tipo empresarial dentro del repositorio segun los datos enviados en el body.
	 * @param companyRequestDto
	 * @return Mono<Company>
	 */
	@Override
	public Mono<Company> updateCompany(CompanyRequestDto companyRequestDto) {
		return companyRepository.findById(companyRequestDto.getId())
                .flatMap(uCompany -> {
                	uCompany.setBusinessName(companyRequestDto.getBusinessName());
                	uCompany.setRuc(companyRequestDto.getRuc());
                	uCompany.setEmail(companyRequestDto.getEmail());
                	uCompany.setTelephone(companyRequestDto.getTelephone());
                	uCompany.setHeadlines(companyRequestDto.getHeadlines());
                	uCompany.setSignatories(companyRequestDto.getSignatories());
                	uCompany.setTypeCustomer("COMPANY");
                	uCompany.setTypeProfile(companyRequestDto.getTypeProfile());
                     return companyRepository.save(uCompany);
        });
	}

	/**
	 * Elimina al cliente de tipo empresarial dentro del repositorio segun el id del cliente.
	 * @param companyId
	 * @return Mono<Message>
	 */
	@Override
	public Mono<Message> deleteCompany(String companyId) {
		Message message = new Message("Company does not exist");
		return companyRepository.findById(companyId)
                .flatMap(dCompany -> {
                	message.setMessage("Company deleted successfully");
                	return companyRepository.deleteById(dCompany.getId()).thenReturn(message);
        }).defaultIfEmpty(message);
	}
	
	@Override
	public Mono<CompanyResponseDto> requestProfilePyme(CompanyRequestDto companyRequestDto) {
		return companyRepository.findById(companyRequestDto.getId()).flatMap(uCompany -> {
			return accountsRestClient.getAllAccountXCustomerId(uCompany.getId())
					.filter(c -> c.getDescripTypeAccount().equals("C_CORRIENTE")).next()
					.flatMap(x -> {
						return creditsRestClient.getAllCreditCardXCustomerId(uCompany.getId()).next().flatMap(b -> {
							uCompany.setTypeProfile("PYME");
							return companyRepository.save(uCompany).flatMap(z -> {
								return Mono.just(new CompanyResponseDto(uCompany, "Successful profile request (PYME)"));
							});	
						}).defaultIfEmpty(new CompanyResponseDto(uCompany, "Customer does not have a credit card"));
					}).defaultIfEmpty(new CompanyResponseDto(uCompany, "Client does not have a checking account"));
        }).defaultIfEmpty(new CompanyResponseDto(null, "Client does not exist"));
	}

}
