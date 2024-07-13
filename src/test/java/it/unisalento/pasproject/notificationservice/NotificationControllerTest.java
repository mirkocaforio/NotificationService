package it.unisalento.pasproject.notificationservice;

import it.unisalento.pasproject.notificationservice.controller.NotificationController;
import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import it.unisalento.pasproject.notificationservice.dto.EmailNotificationDTO;
import it.unisalento.pasproject.notificationservice.dto.PopupNotificationDTO;
import it.unisalento.pasproject.notificationservice.exceptions.NotificationNotFoundException;
import it.unisalento.pasproject.notificationservice.repositories.EmailNotificationRepository;
import it.unisalento.pasproject.notificationservice.repositories.PopupNotificationRepository;
import it.unisalento.pasproject.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc()
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailNotificationRepository emailNotificationRepository;

    @MockBean
    private PopupNotificationRepository popupNotificationRepository;

    @MockBean
    private NotificationService notificationService;

    @Test
    @WithMockUser(roles = "UTENTE", username = "valid@example.com")
    void getAllEmailNotificationsReturnsNonEmptyListWhenNotificationsExist() throws Exception {
        EmailNotification emailNotification1 = new EmailNotification();
        emailNotification1.setId("1");
        emailNotification1.setEmail("user1@example.com");
        emailNotification1.setSubject("Subject1");
        emailNotification1.setMessage("Message1");

        List<EmailNotification> notifications = List.of(emailNotification1);
        given(emailNotificationRepository.findAll()).willReturn(notifications);

        EmailNotificationDTO notificationDTO1 = new EmailNotificationDTO();
        notificationDTO1.setId(emailNotification1.getId());
        notificationDTO1.setEmail(emailNotification1.getEmail());
        notificationDTO1.setSubject(emailNotification1.getSubject());
        notificationDTO1.setMessage(emailNotification1.getMessage());

        given(notificationService.getNotificationDTO(emailNotification1)).willReturn(notificationDTO1);

        mockMvc.perform(get("/api/notification/email/find/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationsList", hasSize(1)))
                .andExpect(jsonPath("$.notificationsList[0].email", is("user1@example.com")))
                .andExpect(jsonPath("$.notificationsList[0].subject", is("Subject1")))
                .andExpect(jsonPath("$.notificationsList[0].message", is("Message1")));
    }

    @Test
    @WithMockUser(roles = "UTENTE")
    void getAllEmailNotificationsReturnsEmptyListWhenNoNotificationsExist() throws Exception {
        given(emailNotificationRepository.findAll()).willReturn(List.of());

        mockMvc.perform(get("/api/notification/email/find/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.notificationsList", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "UTENTE")
    void getAllPopupNotificationsReturnsNonEmptyListWhenNotificationsExist() throws Exception {
        PopupNotification popupNotification1 = new PopupNotification();
        popupNotification1.setId("1");
        popupNotification1.setEmail("user1@example.com");
        popupNotification1.setSubject("Subject1");
        popupNotification1.setMessage("Message1");


        List<PopupNotification> notifications = List.of(popupNotification1);
        given(popupNotificationRepository.findAll()).willReturn(notifications);

        PopupNotificationDTO notificationDTO1 = new PopupNotificationDTO();
        notificationDTO1.setId(popupNotification1.getId());
        notificationDTO1.setEmail(popupNotification1.getEmail());
        notificationDTO1.setSubject(popupNotification1.getSubject());
        notificationDTO1.setMessage(popupNotification1.getMessage());

        given(notificationService.getNotificationDTO(popupNotification1)).willReturn(notificationDTO1);

        mockMvc.perform(get("/api/notification/popup/find/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.notificationsList", hasSize(1)))
                .andExpect(jsonPath("$.notificationsList[0].email", is("user1@example.com")))
                .andExpect(jsonPath("$.notificationsList[0].subject", is("Subject1")))
                .andExpect(jsonPath("$.notificationsList[0].message", is("Message1")));
    }

    @Test
    @WithMockUser(roles = "UTENTE")
    void getAllPopupNotificationsReturnsEmptyListWhenNoNotificationsExist() throws Exception {
        given(emailNotificationRepository.findAll()).willReturn(List.of());

        mockMvc.perform(get("/api/notification/popup/find/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.notificationsList", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getEmailNotificationsByFilterReturnsNonEmptyListForValidCriteria() throws Exception {
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setId("1");
        emailNotification.setEmail("user@example.com");
        emailNotification.setSubject("Test Subject");
        emailNotification.setMessage("Test Message");
        List<EmailNotification> notifications = List.of(emailNotification);

        given(notificationService.findEmailNotifications("user@example.com", "Test Subject", null, null)).willReturn(notifications);

        EmailNotificationDTO notificationDTO = new EmailNotificationDTO();
        notificationDTO.setId(emailNotification.getId());
        notificationDTO.setEmail(emailNotification.getEmail());
        notificationDTO.setSubject(emailNotification.getSubject());
        notificationDTO.setMessage(emailNotification.getMessage());
        given(notificationService.getNotificationDTO(any(EmailNotification.class))).willReturn(notificationDTO);

        mockMvc.perform(get("/api/notification/email/find")
                        .param("email", "user@example.com")
                        .param("subject", "Test Subject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationsList", hasSize(1)))
                .andExpect(jsonPath("$.notificationsList[0].email", is("user@example.com")))
                .andExpect(jsonPath("$.notificationsList[0].subject", is("Test Subject")))
                .andExpect(jsonPath("$.notificationsList[0].message", is("Test Message")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getEmailNotificationsByFilterReturnsEmptyListForInvalidCriteria() throws Exception {
        given(notificationService.findEmailNotifications("nonexistent@example.com", "Invalid Subject", null, null)).willReturn(List.of());

        mockMvc.perform(get("/api/notification/email/find")
                        .param("email", "nonexistent@example.com")
                        .param("subject", "Invalid Subject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationsList", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getPopupNotificationsByFilterReturnsNonEmptyListForValidCriteria() throws Exception {
        PopupNotification popupNotification = new PopupNotification();
        popupNotification.setId("1");
        popupNotification.setEmail("user@example.com");
        popupNotification.setSubject("Test Subject");
        popupNotification.setMessage("Test Message");
        List<PopupNotification> notifications = List.of(popupNotification);

        given(notificationService.findPopupNotifications("user@example.com", "Test Subject", null, null, true)).willReturn(notifications);

        PopupNotificationDTO notificationDTO = new PopupNotificationDTO();
        notificationDTO.setId(popupNotification.getId());
        notificationDTO.setEmail(popupNotification.getEmail());
        notificationDTO.setSubject(popupNotification.getSubject());
        notificationDTO.setMessage(popupNotification.getMessage());
        given(notificationService.getNotificationDTO(any(PopupNotification.class))).willReturn(notificationDTO);

        mockMvc.perform(get("/api/notification/popup/find")
                        .param("email", "user@example.com")
                        .param("subject", "Test Subject")
                        .param("read", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationsList", hasSize(1)))
                .andExpect(jsonPath("$.notificationsList[0].email", is("user@example.com")))
                .andExpect(jsonPath("$.notificationsList[0].subject", is("Test Subject")))
                .andExpect(jsonPath("$.notificationsList[0].message", is("Test Message")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getPopupNotificationsByFilterReturnsEmptyListForInvalidCriteria() throws Exception {
        given(notificationService.findPopupNotifications("nonexistent@example.com", "Invalid Subject", null, null, false)).willReturn(List.of());

        mockMvc.perform(get("/api/notification/popup/find")
                        .param("email", "nonexistent@example.com")
                        .param("subject", "Invalid Subject")
                        .param("read", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationsList", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateReadStatusForExistingNotificationChangesReadFlag() throws Exception {
        PopupNotificationDTO notificationDTO = new PopupNotificationDTO();
        notificationDTO.setId("1");
        notificationDTO.setRead(true);

        given(notificationService.updateReadStatus(notificationDTO.getId(), notificationDTO.isRead())).willReturn(notificationDTO);

        mockMvc.perform(put("/api/notification/update/read/1/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.read", is(true)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateReadStatusForNonExistingNotificationThrowsNotificationNotFoundException() throws Exception {
        given(notificationService.updateReadStatus("nonexistent", false))
                .willThrow(new NotificationNotFoundException("No notification found with id: nonexistent"));

        mockMvc.perform(put("/api/notification/update/read/nonexistent/false"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateAllReadStatusToTrueUpdatesAllNotifications() throws Exception {
        doNothing().when(notificationService).updateAllReadStatus(true);

        mockMvc.perform(put("/api/notification/update/read/all/true"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateAllReadStatusToFalseUpdatesAllNotifications() throws Exception {
        doNothing().when(notificationService).updateAllReadStatus(false);

        mockMvc.perform(put("/api/notification/update/read/all/false"))
                .andExpect(status().isOk());
    }
}
