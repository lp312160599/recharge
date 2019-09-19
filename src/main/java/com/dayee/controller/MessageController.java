package com.dayee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

	@RequestMapping("{uuid}/success")
	public String goSuccessTip(@PathVariable("uuid") String uuid,Model model){
		
		String message = (String) getSession().getAttribute(uuid+"_msg");
		String buttonText = (String) getSession().getAttribute(uuid+"_buttonText");
		String url   = (String) getSession().getAttribute(uuid+"_url");
		
		model.addAttribute("message", message);
		model.addAttribute("buttonText", buttonText);
		model.addAttribute("buttonUrl", url);
		
		return "tip/success";
	}
}
