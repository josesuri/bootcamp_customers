package com.bootcamp.customers.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Clase de transferencia de datos para la solicitud
 * de los clientes personales
 */
@Data
public class PersonRequestDto {
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
