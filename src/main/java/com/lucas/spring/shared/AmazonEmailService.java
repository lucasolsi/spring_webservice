package com.lucas.spring.shared;

import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.lucas.spring.shared.dto.UserDto;

@Service
public class AmazonEmailService {

	final String FROM = "lucasolsi@gmail.com";

	final String SUBJECT = "Email Confirmation";

	final String PASSWORD_RESET_SUBJECT = "Password reset request";

	final String HTMLBODY = "<h1>Please confirm your email address!</h1>"
			+ "Click on the following link to confirm your email:"
			// In order to deploy to AWS, uncomment the line bellow and comment the next
			// line with localhost
			//+ "<a href='http://ec2-54-161-139-204.compute-1.amazonaws.com:8080/verification-service/email-verification.html?token=$tokenValue'>"
			+ "<a href='http://localhost:8080/verification-service/email-verification.html?token=$tokenValue'>"
			+ "Click here" + "</a><br/><br/>" + "<br/>" + "This is the last step to confirm your email." + "<br/><br/>"
			+ "Thanks for your cooperation.";

	final String TEXTBODY = "Please confirm your email address." + "Open the following URL to confirm your email:"
	// In order to deploy to AWS, uncomment the line bellow and comment the next
	// line with localhost
			//+ "http://ec2-54-161-139-204.compute-1.amazonaws.com:8080/verification-service/email-verification.html?token=$tokenValue"
			+ "http://localhost:8080/verification-service/email-verification.html?token=$tokenValue"
			+ "This is the last step to confirm your email." + "Thanks for your cooperation.";

	final String PASSWORD_RESET_HTMLBODY = "<h1>Password reset</h1>" + "<p>Hello, $firstName."
			+ "<p>Your or someone has requested to reset your password. If you don't recognize this request, please ignore it."
			+ "If you requested this, click the link below to set a new password: "
			+ "<a href='http://localhost:8080/verification-service/password-reset.html?token=$tokenValue'>"
			+ " Click this link to Reset Password </a><br/><br/>" + "Thank you for using our system!";

	final String PASSWORD_RESET_TEXTBODY = "Password reset" + "Hello, $firstName."
			+ "Your or someone has requested to reset your password. If you don't recognize this request, please ignore it."
			+ "If you requested this, open the link below to set a new password: "
			+ " http://localhost:8080/verification-service/password-reset.html?token=$tokenValue"
			+ "Thank you for using our system!";

	public void verifyEmail(UserDto userDto) {
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.build();

		String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
		String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());

		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(userDto.getEmail()))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
								.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);

		client.sendEmail(request);

		System.out.println("Email sent!");
	}

	public boolean sendPasswordResetRequest(String firstName, String email, String token) {
		boolean returnValue = false;

		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.build();

		String htmlBodyWithToken = PASSWORD_RESET_HTMLBODY.replace("$tokenValue", token);
		htmlBodyWithToken = htmlBodyWithToken.replace("$firstName", firstName);

		String textBodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
		textBodyWithToken = textBodyWithToken.replace("$firstName", firstName);

		SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(email))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
								.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content().withCharset("UTF-8").withData(PASSWORD_RESET_SUBJECT)))
				.withSource(FROM);

		SendEmailResult result = client.sendEmail(request);
		if (result != null && (result.getMessageId() != null && !result.getMessageId().isEmpty())) {
			returnValue = true;
		}

		return returnValue;
	};
}
