package test.com.pmrodrigues.rpg.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmrodrigues.rpg.entities.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Slf4j
public class ConnectionUtilities {

	private final RestTemplate RESTTEMPLATE;

	private final EmailHelper EMAIL_HELPER = new EmailHelper();
	
	private final HttpHeaders HEADER = new HttpHeaders();
	
	private final String DEFAULT_URL = "http://localhost:8080/api/%s";

	private final Class CLAZZ;

	public ConnectionUtilities(Class clazz) {
		this.CLAZZ = clazz;
		HEADER.setContentType(MediaType.APPLICATION_JSON);
		EMAIL_HELPER.start();

		RESTTEMPLATE = new RestTemplate();
		RESTTEMPLATE.setRequestFactory(new HttpComponentsClientHttpRequestFactory());


		
	}

	public void cleanUpSMTPServer() {
		EMAIL_HELPER.reset();
	}
	
	public void stopSMTPServer() {
		EMAIL_HELPER.stop();
	}

	public boolean isEmailReceived(int milliseconds, int emailCount) {
		return EMAIL_HELPER.waitForIncomingEmail(milliseconds, emailCount);		
	}

	public List<MimeMessage> messagesSent() {
		return EMAIL_HELPER.messagesSent();
	}

	@SneakyThrows
	@SuppressWarnings("rawtypes")
	public ResponseEntity post(String url, Object value ) {
		
		log.info("try to insert {} on request to {}/{}", value , DEFAULT_URL, url);
		
		String request = "";
		if( value instanceof String ) {
			request = (String)value;
		}else {

			return RESTTEMPLATE.exchange(String.format(DEFAULT_URL, url),
					HttpMethod.POST,
					new HttpEntity<>(value, HEADER),
					this.CLAZZ);
		}
		
		return this.exchange(url , request, HttpMethod.POST);
		
	}
	
	@SneakyThrows
	@SuppressWarnings("rawtypes")
	public ResponseEntity put(String url, Object value ) {
		
		log.info("try to update {} on request to {}/{}", value , DEFAULT_URL, url);
		
		String request = "";
		if( value instanceof String ) {
			request = (String)value;
		}else {
			var mapper = new ObjectMapper();
			request =   mapper.writeValueAsString(value);
		}
		
		return this.exchange(url, request, HttpMethod.PUT);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ResponseEntity exchange(String url, String request , HttpMethod httpMethod ) {
		return RESTTEMPLATE.exchange(String.format(DEFAULT_URL, url), 
				httpMethod, 
				new HttpEntity<String>(request, HEADER), 
				this.CLAZZ); 
	}

	@SuppressWarnings({ "rawtypes"})
	public ResponseEntity get(String url) {
		return this.exchange(url, "", HttpMethod.GET);
	}


	public ConnectionUtilities auth(User user) {	    
		HEADER.setBasicAuth(user.getEmail(), user.getCleanPassword());
		return this;
	}
	
}
