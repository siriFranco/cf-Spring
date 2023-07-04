package com.codigofacilito.peliculas.controllers;

import java.io.IOException;

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codigofacilito.peliculas.entities.Actor;
import com.codigofacilito.peliculas.entities.Pelicula;
import com.codigofacilito.peliculas.services.IActorService;
import com.codigofacilito.peliculas.services.IGeneroService;
import com.codigofacilito.peliculas.services.IPeliculaService;

import jakarta.validation.Valid;

@Controller
public class PeliculasController {
	private IPeliculaService service;
	private IGeneroService generoService;
	private IActorService actorService;

	public PeliculasController(IPeliculaService service, IGeneroService generoService, IActorService actorService) {
		this.service = service;
		this.generoService = generoService;
		this.actorService = actorService;
	}

	@GetMapping("/pelicula")
	public String crear(Model model) {
		Pelicula pelicula = new Pelicula();
		model.addAttribute("pelicula", pelicula);
		model.addAttribute("generos", generoService.findAll());
		model.addAttribute("titulo", "Nueva Pelicula");
		return "pelicula";
	}

	@GetMapping("/pelicula/{id}/")
	public String editar(@PathVariable(name = "id") Long id, Model model) {
		Pelicula pelicula = service.findById(id);
		String ids = "";
		
		for (Actor actor : pelicula.getProtagonistas()) {
			if("".equals(ids)) {
				ids = actor.getId().toString();
			} else {
				ids = ids + "," + actor.getId().toString();
			}
		}
		
		model.addAttribute("pelicula", pelicula);
		model.addAttribute("ids", ids);
		model.addAttribute("generos", generoService.findAll());
		model.addAttribute("actores", actorService.findAll());
		model.addAttribute("titulo", "Nueva Pelicula");
		return "pelicula";
	}

	@PostMapping("/pelicula")
	public String guardar(@Valid Pelicula pelicula, BindingResult br, @ModelAttribute(name="ids")String ids, 
			Model model, @RequestParam("archivo") MultipartFile imagen) {
		
		if(br.hasErrors()) {
			model.addAttribute("generos", generoService.findAll());
			model.addAttribute("actores", actorService.findAll());
			return "pelicula";
		}
		
		if(!imagen.isEmpty()) {
			String archivo = pelicula.getNombre() + getExtension(imagen.getOriginalFilename());
			pelicula.setImagen(archivo);
			try {
				archivoService.guardar(archivo, imagen.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			pelicula.setImagen("_default.jpg");
		}
		
		
		if (ids != null && !"".equals(ids)) {
			List<Long> idsProtagonistas = Arrays.stream(ids.split(",")).map(Long::parseLong)
						.collect(Collectors.toList());
			List<Actor> protagonistas = actorService.findAllById(idsProtagonistas);
			pelicula.setProtagonistas(protagonistas);
			
		}
		
		
		service.save(pelicula);
		return "redirect:home";
	}

	@GetMapping({ "/listado" })
	public String listado(Model model, @RequestParam(required = false) String msj, @RequestParam(required = false) String tipoMsj) {
		//model.addAttribute("peliculas", service.findAll());
		//model.addAttribute("msj", "Catalogo actualizado a 2023");
		//model.addAttribute("tipoMsj", "success");
		model.addAttribute("titulo", "Listado de Peliculas");
		model.addAttribute("peliculas", service.findAll());
		
		if("".equals(tipoMsj) && !"".equals(msj)) {
			model.addAttribute("msj", msj);
			model.addAttribute("tipoMsj", tipoMsj);
			
		}
		
		return "listado";
	}
	
	@GetMapping("/pelicula/{id}/delete")
	public String eliminar(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAtt) {
		
		service.delete(id);
		
		redirectAtt.addAttribute("msj", "La pelicula fue eliminada correctamente");
		redirectAtt.addAttribute("tipoMsj", "success");
		
		return "redirect:/listado";
	}


}
