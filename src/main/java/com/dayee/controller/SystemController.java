package com.dayee.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dayee.model.Systems;
import com.dayee.service.SystemService;

@Controller
@RequestMapping("/system")
public class SystemController extends BaseController {

	@Resource
	private SystemService systemService;
	
	@RequestMapping("index")
	public String index(Integer sysId,Model model) throws Exception{
		List<Systems> sysList = 	systemService.query();
		Systems systems = null;
	    if(sysId==null){
	    	systems = sysList.get(0);
	    }else{
	    	for (Systems s:sysList) {
				if(s.getId().equals(sysId)){
					systems = s;
					break;
				}
			}
	    }
		model.addAttribute("sysList",sysList);
		model.addAttribute("systems",systems);
		return "index";
	}
	
}
