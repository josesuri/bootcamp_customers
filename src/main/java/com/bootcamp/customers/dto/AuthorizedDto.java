package com.bootcamp.customers.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Clase de transferencia de datos para la lista
 * de titulares de las cuentas empresariales
 */
@Data
public class AuthorizedDto {
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
}
