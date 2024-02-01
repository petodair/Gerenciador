package br.com.gerenciadados.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.gerenciadados.model.Produto;

@Controller
public class HomeController {
	
	//REDIRECIONAR AO HOME
	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home/index");
		
		//ENVIAR UM OBJETO PARA O VIEW
		mv.addObject("produto", new Produto());
		return mv;
	}
	
	

}
