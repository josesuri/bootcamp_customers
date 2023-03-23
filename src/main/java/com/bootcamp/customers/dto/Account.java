package com.bootcamp.customers.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * Clase de transferencia de datos para el microservicio de cuentas
 */
@Data
@JsonInclude(Include.NON_NULL)
public class Account {
	private String id;
	private String customerId;
	private Integer typeAccount;
	private String descripTypeAccount;
	private Double amount;
	private Double maintenance;
	private Integer transaction;
	private Integer operationDay;
	private Date dateAccount;
	private String NumberAccount;
	private String typeCustomer;
}
