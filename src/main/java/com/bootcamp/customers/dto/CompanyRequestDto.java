package com.bootcamp.customers.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Clase de transferencia de datos para la solicitud
 * de los clientes empresariales
 */
@Data
public class CompanyRequestDto {
	private String id;
	@NotEmpty
	private String businessName;
	@NotEmpty
	private String ruc;
	@NotEmpty
	private String email;
	@NotEmpty
	private String telephone;
	@NotEmpty
	private List<AuthorizedDto> headlines;
	private List<AuthorizedDto> signatories;
	private String typeCustomer;
	private String typeProfile;
}
