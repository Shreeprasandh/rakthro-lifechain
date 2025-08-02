import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class MailService {

    private static final String FROM_EMAIL = "rakthroservice@gmail.com"; // Replace this
    private static final String PASSWORD = "ofxufsekzfqktzvl";             // Replace with Gmail app password

    public static void sendConfirmationEmail(String toEmail, String donorName, String date, String time, String hospital) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Rakthro - Appointment Confirmed ");

            String msg = String.format(
                "Hello %s,\n\n" +
                "Your blood donation appointment is confirmed!\n\n" +
                "📅 Date: %s\n🕒 Time: %s\n🏥 Hospital: %s\n\n" +
                "Thank you for saving lives! \n\n– Rakthro Team",
                donorName, date, time, hospital
            );

            message.setText(msg);
            Transport.send(message);
            System.out.println("✅ Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println(" Email failed to send");
        }
    }
}
