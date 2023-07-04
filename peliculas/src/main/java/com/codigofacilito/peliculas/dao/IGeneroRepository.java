package com.codigofacilito.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.codigofacilito.peliculas.entities.Genero;

public interface IGeneroRepository extends CrudRepository<Genero, Long> {

	/* Implementacion anterior
	public void save(Genero genero);
	public Genero findById(Long id); */
	
	
	
	
}
