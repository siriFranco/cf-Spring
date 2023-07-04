package com.codigofacilito.peliculas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codigofacilito.peliculas.dao.IActorRepository;
import com.codigofacilito.peliculas.entities.Actor;


@Service
public class ActorService implements IActorService {

	@Autowired
	private IActorRepository repo;
	
	public List<Actor> findAll() {
		// TODO Auto-generated method stub
		return (List<Actor>) repo.findAll();
	}

	public List<Actor> findAllById(List<Long> ids) {
		return (List<Actor>) repo.findAllById(ids);
	}

}
