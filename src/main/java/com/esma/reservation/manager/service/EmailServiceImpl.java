package com.esma.reservation.manager.service;

import com.esma.reservation.manager.model.entity.Reservation;
import com.esma.reservation.manager.model.entity.ReservationGuest;
import com.esma.reservation.manager.model.entity.ReservationGuestDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${reservation.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void sendReservationFormLink(String toEmail, String reservationNumber) {
        try {
            log.info("Attempting to send reservation form link to: {}", toEmail);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(toEmail);
            message.setSubject("Complete Your Reservation - " + reservationNumber);
            message.setText(buildReservationFormEmailBody(reservationNumber));

            mailSender.send(message);
            log.info("Reservation form link email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send reservation form link to: {}. Error: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send reservation form link email", e);
        }
    }

    @Override
    public void sendConfirmationEmail(String toEmail, String reservationNumber) {
        try {
            log.info("Attempting to send confirmation email to: {}", toEmail);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(toEmail);
            message.setSubject("Reservation Confirmed - " + reservationNumber);
            message.setText(buildConfirmationEmailBody(reservationNumber));

            mailSender.send(message);
            log.info("Confirmation email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send confirmation email to: {}. Error: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send confirmation email", e);
        }
    }

    @Override
    public void sendDetailedReservationEmail(Reservation reservation) {
        try {
            log.info("Attempting to send detailed reservation email to: {}", reservation.getCustomer().getEmail());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(reservation.getCustomer().getEmail());
            helper.setSubject("Reservation Update - " + reservation.getReservationNumber());

            String htmlContent = buildDetailedReservationEmailHtml(reservation);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Detailed reservation email sent successfully to: {}", reservation.getCustomer().getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send detailed reservation email: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send detailed reservation email", e);
        }
    }

    private String buildReservationFormEmailBody(String reservationNumber) {
        String formLink = String.format("%s/?requestId=%s", frontendUrl, reservationNumber);

        return String.format(
            "Dear Guest,%n%n" +
            "Thank you for your reservation.%n%n" +
            "To complete your check‑in details, please click the link below:%n" +
            "%s%n%n" +
            "If you did not request this reservation, please ignore this email.%n%n" +
            "Best regards,%n" +
            "Reservation Team",
            formLink
        );
    }

    private String buildConfirmationEmailBody(String reservationNumber) {
        return String.format(
            "Dear Customer,%n%n" +
            "Your reservation %s has been confirmed!%n%n" +
            "Thank you for choosing our service.%n%n" +
            "Best regards,%n" +
            "Reservation Team",
            reservationNumber
        );
    }

    private String buildDetailedReservationEmailHtml(Reservation reservation) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f4f4f4; margin: 0; padding: 0; }");
        html.append(".container { max-width: 600px; margin: 20px auto; background-color: #ffffff; }");
        html.append(".header { background: linear-gradient(135deg, #079f62 0%, #05b574 100%); color: green; padding: 30px 20px; text-align: center; }");
        html.append(".header h1 { margin: 0; font-size: 28px; }");
        html.append(".content { padding: 30px 20px; }");
        html.append(".greeting { font-size: 16px; margin-bottom: 20px; }");
        html.append(".info-box { background-color: #f9f9f9; padding: 20px; margin: 20px 0; border-left: 4px solid #079f62; border-radius: 4px; }");
        html.append(".info-box h3 { margin-top: 0; color: #079f62; }");
        html.append(".info-row { margin: 10px 0; }");
        html.append(".info-label { font-weight: bold; color: #555; }");
        html.append(".guest-card { background-color: #ffffff; padding: 15px; margin: 15px 0; border: 2px solid #e0e0e0; border-radius: 8px; }");
        html.append(".guest-card h4 { margin-top: 0; color: #079f62; border-bottom: 2px solid #079f62; padding-bottom: 8px; }");
        html.append(".footer { background-color: #f4f4f4; text-align: center; padding: 20px; color: #777; font-size: 14px; border-top: 1px solid #ddd; }");
        html.append(".status-badge { display: inline-block; padding: 5px 15px; background-color: #4CAF50; color: white; border-radius: 20px; font-size: 14px; }");
        html.append("</style>");
        html.append("</head><body>");

        html.append("<div class='container'>");

        // Header
        html.append("<div class='header'>");
        html.append("<h1> Reservation Confirmation</h1>");
        html.append("</div>");

        // Content
        html.append("<div class='content'>");

        // Greeting
        html.append("<div class='greeting'>");
        html.append("<p>Dear <strong>").append(reservation.getCustomer().getFirstName())
            .append(" ").append(reservation.getCustomer().getLastName()).append("</strong>,</p>");
        html.append("<p>Your reservation has been successfully updated. Please find the details below.</p>");
        html.append("</div>");

        // Reservation Information Box
        html.append("<div class='info-box'>");
        html.append("<h3>📋 Reservation Information</h3>");

        html.append("<div class='info-row'>");
        html.append("<span class='info-label'>Reservation Number:</span> ");
        html.append("<strong>").append(reservation.getReservationNumber()).append("</strong>");
        html.append("</div>");

        html.append("<div class='info-row'>");
        html.append("<span class='info-label'>Status:</span> ");
        html.append("<span class='status-badge'>").append(reservation.getStatus()).append("</span>");
        html.append("</div>");

        if (reservation.getBookedDate() != null) {
            html.append("<div class='info-row'>");
            html.append("<span class='info-label'>Check-in Date:</span> ");
            html.append(reservation.getBookedDate().format(DATE_FORMATTER));
            html.append("</div>");
        }

        html.append("<div class='info-row'>");
        html.append("<span class='info-label'>Total Number of Guests:</span> ");
        html.append(reservation.getReservationGuests().size());
        html.append("</div>");

        html.append("</div>");

        // Guest Details
        html.append("<h3 style='color: #079f62; margin-top: 30px;'>👥 Guest Information</h3>");

        int guestNumber = 1;
        for (ReservationGuest guest : reservation.getReservationGuests()) {
            html.append("<div class='guest-card'>");
            html.append("<h4>Guest ").append(guestNumber++).append("</h4>");

            html.append("<div class='info-row'>");
            html.append("<span class='info-label'>Full Name:</span> ");
            html.append(guest.getFirstName()).append(" ").append(guest.getLastName());
            html.append("</div>");

            if (guest.getGender() != null && !guest.getGender().isEmpty()) {
                html.append("<div class='info-row'>");
                html.append("<span class='info-label'>Gender:</span> ");
                html.append(guest.getGender());
                html.append("</div>");
            }

            if (guest.getAge() != null) {
                html.append("<div class='info-row'>");
                html.append("<span class='info-label'>Age:</span> ");
                html.append(guest.getAge());
                html.append("</div>");
            }

            if (guest.getRoom() != null) {
                html.append("<div class='info-row'>");
                html.append("<span class='info-label'>Room:</span> ");
                html.append("Room ").append(guest.getRoom().getRoomNumber())
                    .append(" - ").append(guest.getRoom().getRoomType());
                html.append("</div>");
            }

            // Guest Details
            ReservationGuestDetail detail = guest.getReservationGuestDetail();
            if (detail != null) {
                if (detail.getExpectedArrival() != null) {
                    html.append("<div class='info-row'>");
                    html.append("<span class='info-label'>Expected Arrival Time:</span> ");
                    html.append(detail.getExpectedArrival().format(TIME_FORMATTER));
                    html.append("</div>");
                }

                if (detail.getRoomPreferences() != null && !detail.getRoomPreferences().isEmpty()) {
                    html.append("<div class='info-row'>");
                    html.append("<span class='info-label'>Room Preferences:</span> ");
                    html.append(detail.getRoomPreferences());
                    html.append("</div>");
                }

                if (detail.getAllergies() != null && !detail.getAllergies().isEmpty()) {
                    html.append("<div class='info-row'>");
                    html.append("<span class='info-label'>Allergies:</span> ");
                    html.append(String.join(", ", detail.getAllergies()));
                    html.append("</div>");
                }

                if (detail.getExtraNeeds() != null && !detail.getExtraNeeds().isEmpty()) {
                    html.append("<div class='info-row'>");
                    html.append("<span class='info-label'>Extra Needs:</span> ");
                    html.append(detail.getExtraNeeds());
                    html.append("</div>");
                }

                if (detail.getSpecialRequests() != null && !detail.getSpecialRequests().isEmpty()) {
                    html.append("<div class='info-row'>");
                    html.append("<span class='info-label'>Special Requests:</span> ");
                    html.append(detail.getSpecialRequests());
                    html.append("</div>");
                }
            }

            html.append("</div>");
        }

        // Footer message
        html.append("<div style='margin-top: 30px; padding: 20px; background-color: #fff9e6; border-radius: 4px; border-left: 4px solid #ffc107;'>");
        html.append("<p style='margin: 0;'>💡 <strong>Note:</strong> If you have any questions or need to make changes, please contact us.</p>");
        html.append("</div>");

        html.append("</div>");

        // Footer
        html.append("<div class='footer'>");
        html.append("<p>Thank you for choosing us! 🙏</p>");
        html.append("<p style='font-size: 12px; color: #999;'>This email was sent automatically.</p>");
        html.append("</div>");

        html.append("</div>");
        html.append("</body></html>");

        return html.toString();
    }
}
