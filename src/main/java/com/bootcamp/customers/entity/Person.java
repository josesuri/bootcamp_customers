package com.bootcamp.customers.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Clase de entidad para clientes personales
 */
@Data
@AllArgsConstructor
@Document(collection="person")
public class Person {
	@Id
	private String id;
	@NotEmpty
	private String name;
	@NotEmpty
	private String lastName;
	@NotEmpty
	private String dni;
	@NotEmpty
	private String email;
	@NotEmpty
	private String telephone;
	
	private String typeCustomer;
	
	private String typeProfile;
}
