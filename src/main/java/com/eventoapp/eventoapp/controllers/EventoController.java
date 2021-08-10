package com.eventoapp.eventoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;
import com.eventoapp.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.eventoapp.repository.EventoRepository;

@Controller
public class EventoController {
	
	@Autowired
	private EventoRepository er;
	
	@Autowired
	private ConvidadoRepository cr;
	
	@RequestMapping(value="/cadastrarEvento",method=RequestMethod.GET)
	public String form() {
		 return "evento/formEvento";
	}
	
	@RequestMapping(value="/cadastrarEvento",method=RequestMethod.POST)
	public String form(Evento evento) {
		
		er.save(evento); 
		
		 return "redirect:/cadastrarEvento";
	}
	
	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> eventos = er.findAll();
		
		mv.addObject("eventos",eventos);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method =RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("id") Integer id) {
		 Evento evento = er.findById(id);
		 
		 ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		 mv.addObject("evento",evento);
		 
		 Iterable<Convidado> convidados = cr.findByEvento(evento);
		 mv.addObject("convidados",convidados);
		 
		 return mv;
	}
	@RequestMapping(value = "/{id}", method =RequestMethod.POST)
	public String detalhesEventoPost(@PathVariable("id") Integer id,Convidado convidado) {
		 
		 Evento evento = er.findById(id);
		 
		 convidado.setEvento(evento);
		 cr.save(convidado);
		 
		
		return "redirect:/{id}";
	}
}
