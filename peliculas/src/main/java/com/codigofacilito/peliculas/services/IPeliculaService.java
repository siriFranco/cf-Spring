package com.codigofacilito.peliculas.services;

import java.util.List;

import com.codigofacilito.peliculas.entities.Pelicula;

public interface IPeliculaService {

	public void save(Pelicula pelicula);
	public Pelicula findById(Long id);
	public List<Pelicula> findAll();
	public void delete(Long id);
	
	
	
	
}
