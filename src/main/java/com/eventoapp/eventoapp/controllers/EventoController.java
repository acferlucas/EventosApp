package com.eventoapp.eventoapp.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		Integer codigoInt = evento.getId();
		String id = "" + codigoInt;
		
		System.out.println(id);
		 return "redirect:/"+id;
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
	
	@RequestMapping("/deletarEvento")
	public String deletarEvento(Integer id) {
		
		Evento evento = er.findById(id);
		
		er.delete(evento);
		
		return "redirect:/eventos";
	}
	
	
	
	@RequestMapping(value = "/{id}", method =RequestMethod.POST)
	public String detalhesEventoPost(@PathVariable("id") Integer id,@Valid Convidado convidado,BindingResult result, RedirectAttributes attributes) {
		 if(result.hasErrors()) {
			 attributes.addFlashAttribute("msg", "Verifique os Campos");
			 return "redirect:/{id}";
		 }
		 Evento evento = er.findById(id);
		 
		 convidado.setEvento(evento);
		 cr.save(convidado);
		 
		
		return "redirect:/{id}";
	}
	
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(String rg) {
		
		Convidado convidado = cr.findByRg(rg);
		
		cr.delete(convidado);
		Evento evento = convidado.getEvento();
		Integer codigoInt = evento.getId();
		String id = "" + codigoInt;
		
		return "redirect:/"+id;
	}
}
