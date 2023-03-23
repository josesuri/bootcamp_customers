package com.bootcamp.customers.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.customers.clients.AccountsRestClient;
import com.bootcamp.customers.clients.CreditsRestClient;
import com.bootcamp.customers.dto.Message;
import com.bootcamp.customers.dto.PersonRequestDto;
import com.bootcamp.customers.dto.PersonResponseDto;
import com.bootcamp.customers.entity.Person;
import com.bootcamp.customers.repository.PersonRepository;
import com.bootcamp.customers.service.PersonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Clase de implementación para la interfaz PersonService
 */
@Service
public class PersonServiceImpl implements PersonService{
	
	@Autowired
	AccountsRestClient accountsRestClient;
	
	@Autowired
	CreditsRestClient creditsRestClient;

	@Autowired
    private PersonRepository personRepository;

	/**
	 * Método que devuelve todos los clientes de tipo personal dentro el repositorio.
	 * @return Flux<Person>
	 */
	@Override
	public Flux<Person> getAll() {
		return personRepository.findAll();
	}

	/**
	 * Devuelve todos los clientes de tipo personal dentro el repositorio segun el id del cliente.
	 * @return Mono<Person>
	 */
	@Override
	public Mono<Person> getPersonById(String personId) {
		return personRepository.findById(personId);
	}

	/**
	 * Crea un cliente de tipo personal dentro del repositorio con los datos enviados en el body.
	 * @param personRequestDto
	 * @return Mono<Person>
	 */
	@Override
	public Mono<Person> createPerson(PersonRequestDto personRequestDto) {
		Person person = new Person(null,personRequestDto.getName(),personRequestDto.getLastName(),personRequestDto.getDni()
				,personRequestDto.getEmail(),personRequestDto.getTelephone(), "PERSON", "PERSONAL");
		return personRepository.save(person);
	}

	/**
	 * Actualiza un cliente de tipo personal dentro del repositorio segun los datos enviados en el body.
	 * @param personRequestDto
	 * @return
	 */
	@Override
	public Mono<Person> updatePerson(PersonRequestDto personRequestDto) {
		return personRepository.findById(personRequestDto.getId())
                .flatMap(uPerson -> {
                	uPerson.setName(personRequestDto.getName());
                	uPerson.setLastName(personRequestDto.getLastName());
                	uPerson.setDni(personRequestDto.getDni());
                	uPerson.setEmail(personRequestDto.getEmail());
                	uPerson.setTelephone(personRequestDto.getTelephone());
                	uPerson.setTypeCustomer("PERSON");
                	uPerson.setTypeProfile(personRequestDto.getTypeProfile());
                    return personRepository.save(uPerson);
        });
	}

	/**
	 * Elimina al cliente de tipo empresarial dentro del repositorio segun el id del cliente.
	 * @param personId
	 * @return
	 */
	@Override
	public Mono<Message> deletePerson(String personId) {
		Message message = new Message("person does not exist");
		return personRepository.findById(personId)
                .flatMap(dPerson -> {
                	message.setMessage("Person deleted successfully");
                	return personRepository.deleteById(dPerson.getId()).thenReturn(message);
        }).defaultIfEmpty(message);
	}

	@Override
	public Mono<PersonResponseDto> requestProfileVip(PersonRequestDto personRequestDto) {
		return personRepository.findById(personRequestDto.getId()).flatMap(uPerson -> {
			return accountsRestClient.getAllAccountXCustomerId(uPerson.getId())
					.filter(c -> c.getDescripTypeAccount().equals("AHORRO"))
					.filter(c -> c.getAmount() > 500).next()
					.flatMap(x -> {
						return creditsRestClient.getAllCreditCardXCustomerId(uPerson.getId()).next().flatMap(b -> {
							uPerson.setTypeProfile("VIP");
							return personRepository.save(uPerson).flatMap(z -> {
								return Mono.just(new PersonResponseDto(uPerson, "Successful profile request (VIP)"));
							});	
						}).defaultIfEmpty(new PersonResponseDto(uPerson, "Customer does not have a credit card"));
					}).defaultIfEmpty(new PersonResponseDto(uPerson, "Client does not have a minimum amount of 500 in a savings account"));
        }).defaultIfEmpty(new PersonResponseDto(null, "Client does not exist"));
	}

}
