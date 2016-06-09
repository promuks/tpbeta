package net.atpco.fm.health.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
	
	@RequestMapping(value = "/api/fmbootstrap/health", method = RequestMethod.GET, produces=MediaType.TEXT_HTML_VALUE)
    public @ResponseBody String getFaresResultStatus(HttpServletResponse httpServletResponse) {
		return "<b>FM Bootstrap applicaion is up and running!</b>";
    }
}
