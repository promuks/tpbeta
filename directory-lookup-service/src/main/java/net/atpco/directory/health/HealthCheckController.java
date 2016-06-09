package net.atpco.directory.health;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
	
	@RequestMapping(value = "/api/fmDirectoryService/health", method = RequestMethod.GET, produces=MediaType.TEXT_HTML_VALUE)
    public @ResponseBody String getFaresResultStatus(HttpServletResponse httpServletResponse) {
		return "<b>Directory Lookup applicaion is up and running!</b>";
    }
}
