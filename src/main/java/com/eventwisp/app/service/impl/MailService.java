package com.eventwisp.app.service.impl;

import com.eventwisp.app.dto.response.EventCreateResponse;
import com.eventwisp.app.dto.response.general.UpdateResponse;
import com.eventwisp.app.entity.Event;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    //Register admin email
    public void registerAdminEmail(String to,String name) {
        String subject="Admin Account Registered Successfully";

        String receiver=to;

        String message="Dear "+name+" ,\n\n You are now an admin of Eventwisp. \n\n Best regards, \n Eventwisp team";

        sendEmail(receiver,subject,message);
    }

    //delete notification
    public void deleteAdminEmail(String to,String name) {
        String subject="Admin Account Deactivation Confirmation";

        String receiver=to;

        String message="Dear "+name+" ,\n\n Your admin account on Eventwisp successfully deleted. \n\n Best regards, \n Eventwisp team";

        sendEmail(receiver,subject,message);
    }

    //Register a new manger
    public void registerManagerEmail(String to,String name) {
        String subject="Manager Account Registered Successfully";

        String receiver=to;

        String message="Dear "+name+" ,\n\n You are now a manager of Eventwisp. \n\n Best regards, \n Eventwisp team";

        sendEmail(receiver,subject,message);
    }

    //Delete manager email
    public void deleteManagerEmail(String to,String name) {
        String subject="Manager Account Deactivation Confirmation";

        String receiver=to;

        String message="Dear "+name+" ,\n\n Your manager account on Eventwisp successfully deleted. \n\n Best regards, \n Eventwisp team";

        sendEmail(receiver,subject,message);
    }

    //Register an organizer email
    public void registerOrganizerEmail(String to,String name){
        String subject="Organizer Account Registered Successfully";

        String receiver=to;

        String message="Dear "+name+" ,\n\n You are now an organizer of Eventwisp. \n\n Best regards, \n Eventwisp team";

        sendEmail(receiver,subject,message);
    }

    //Delete organizer email
    public void deleteOrganizerEmail(String to,String name){
        String subject="Organizer Account Deactivation Confirmation";

        String receiver=to;

        String message="Dear "+name+" ,\n\n Your organizer account on Eventwisp successfully deleted. \n\n Best regards, \n Eventwisp team";

        sendEmail(receiver,subject,message);
    }

    //Add event email
    public void addEventEmail(EventCreateResponse response){
        String subject="Event Request Received";

        //organizer email
        String receiver=response.getEvent().getOrganizer().getEmail();

        //organizer name
        String organizerName=response.getEvent().getOrganizer().getFirstName();

        String message="Dear "+organizerName+" ,\n\n Thank you for submitting your event for publication. We’ve successfully received your request and our team is currently reviewing it.\n" +
                "\n" +
                "You’ll receive a notification once your event has been approved and is ready to be hosted on our platform.\n" +
                "\n" +
                "If you have any questions in the meantime, feel free to contact our support team.  \n\n Cheers, \n Eventwisp team";

        sendEmail(receiver,subject,message);
    }

    // event status updated email
    public void eventStatusUpdateEmail(UpdateResponse<Event> response){
        String subject = "Event Status Notification";

        Event event = response.getUpdatedData();
        if (event == null || event.getOrganizer() == null) {
            return; // or log/throw as you prefer
        }

        String receiver = event.getOrganizer().getEmail();
        String organizerName = event.getOrganizer().getFirstName();
        String eventName = event.getEventName();

        String status = "";
        if (event.getEventStatus() != null && event.getEventStatus().getStatusName() != null) {
            status = event.getEventStatus().getStatusName();
        }

        StringBuilder message = new StringBuilder();
        message.append("Dear ").append(organizerName).append(",\n\n");

        switch (status.toLowerCase()) {
            case "approved":
                message.append("Good news! Your event \"").append(eventName).append("\" has been approved.\n")
                        .append("Start date: ").append(event.getStartingDate()).append("\n\n")
                        .append("You can now proceed with ticketing and promotion.\n\n")
                        .append("Cheers,\nEventwisp team");
                break;

            case "disapproved":
                message.append("We’re sorry—your event \"").append(eventName).append("\" was not approved at this time.\n");
                String reason = response.getMessage();
                if (reason != null && !reason.isBlank()) {
                    message.append("\nReason: ").append(reason).append("\n");
                }
                message.append("\nYou’re welcome to make the necessary updates and resubmit.\n\n")
                        .append("Best regards,\nEventwisp team");
                break;

            case "completed":
                message.append("Congratulations! Your event \"").append(eventName).append("\" has been successfully completed.\n\n")
                        .append("Thank you for hosting with Eventwisp — we hope it was a great success!\n")
                        .append("You can now review event performance and earnings from your dashboard.\n\n")
                        .append("Warm regards,\nEventwisp team");
                break;

            default:
                message.append("The status of your event \"").append(eventName).append("\" was updated to: ")
                        .append(status.isBlank() ? "Unknown" : status).append(".\n\n")
                        .append("Best regards,\nEventwisp team");
                break;
        }


        sendEmail(receiver, subject, message.toString());
    }

}
