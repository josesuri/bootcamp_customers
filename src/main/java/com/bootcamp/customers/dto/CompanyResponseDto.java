package com.bootcamp.customers.dto;

import com.bootcamp.customers.entity.Company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase de transferencia de datos para la respuesta
 * de los clientes empresariales
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyResponseDto{
	private Company company;
	private String message;
}
