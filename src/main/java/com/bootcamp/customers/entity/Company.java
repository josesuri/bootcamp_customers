package com.bootcamp.customers.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bootcamp.customers.dto.AuthorizedDto;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Clase de entidad para clientes empresariales
 */
@Data
@AllArgsConstructor
@Document(collection="company")
public class Company {
	@Id
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
