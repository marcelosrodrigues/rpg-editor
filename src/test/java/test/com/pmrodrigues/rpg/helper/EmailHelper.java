package test.com.pmrodrigues.rpg.helper;

import java.util.Arrays;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

@Component
public class EmailHelper {
	
	private GreenMail greenmail = new GreenMail(ServerSetupTest.SMTP);

	public void start() {
		greenmail.start();
	}
	
	public void stop() {
		greenmail.stop();
	}
	
	public void reset() {
		greenmail.reset();
	}
	
	public List<MimeMessage> messagesSent() {		
		return Arrays.asList(greenmail.getReceivedMessages());
	}
	
	public boolean waitForIncomingEmail(int milliseconds, int emailCount) {
		return greenmail.waitForIncomingEmail(milliseconds, emailCount);
	}
	
	
}
