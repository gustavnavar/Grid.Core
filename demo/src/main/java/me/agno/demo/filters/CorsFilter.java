package me.agno.demo.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.stream.Collectors;

import me.agno.demo.filters.utils.RequestWrapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StreamUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends OncePerRequestFilter {
    
	@Value("${application.origin}")
    private String origin;
	
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) {
		
		try {
			LoggerFactory.getLogger(CorsFilter.class).debug(String.format("CORS Filtering PATH: %s, METHOD: %s", req.getRequestURI(), req.getMethod()));

			try {
				var parameters =req.getParameterMap();
				if(parameters != null && ! parameters.isEmpty()) {
					String params = parameters.keySet().stream()
							.map(key -> key + "=" + String.join(", ", parameters.get(key)))
							.collect(Collectors.joining("; ", "{", "}"));
					LoggerFactory.getLogger(CorsFilter.class).debug("\tParameters: " + params);
				}

				//Wrap the request
				RequestWrapper wrapper = new RequestWrapper((HttpServletRequest) req);

				//Get the input stream from the wrapper and convert it into byte array
				byte[] body = StreamUtils.copyToByteArray(wrapper.getInputStream());

				if(body.length > 0) {
					LoggerFactory.getLogger(CorsFilter.class).debug("\tBody: " + new String(body));
				}
				else {
					LoggerFactory.getLogger(CorsFilter.class).debug("\tBody: null");
				}

				// use wrapper as request
				req = wrapper;
			} catch (Exception e) {
	        	// TODO Auto-generated catch block
				e.printStackTrace();
	        }
			
	        res.setHeader("Access-Control-Allow-Origin", origin);
	        res.setHeader("Access-Control-Allow-Credentials", "true");
	        res.setHeader("Access-Control-Allow-Methods",
	                "ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL");
	        res.setHeader("Access-Control-Max-Age", "3600");
	        res.setHeader("Access-Control-Allow-Headers",
	                "Access-Control-Allow-Headers, Origin, X-Requested-With, content-type, Content-Type, Accept, Key, Authorization, x-requested-with, Content-Range, Content-Disposition, Content-Description");

	        if ("OPTIONS".equals(req.getMethod())) {
	        	res.setStatus(HttpServletResponse.SC_OK);
	        } else { 
	        	chain.doFilter(req, res);
	        }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
