package com.todotic.contactlistapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todotic.contactlistapi.dto.ContactDTO;
import com.todotic.contactlistapi.entity.Contact;
import com.todotic.contactlistapi.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ContactController.class)
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ContactController(contactService)).build();
    }

    @Test


    public void testGetContact() throws Exception {
        Integer contactId = 1;
        Contact contact = new Contact();
        contact.setId(contactId);
        when(contactService.findById(contactId)).thenReturn(contact);

        mockMvc.perform(get("/api/contacts/" + contactId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contactId));
    }

    @Test
    public void testCreateContact() throws Exception {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("New Contact");
        contactDTO.setEmail("new@example.com");
        Contact contact = new Contact();
        contact.setId(1);
        contact.setName(contactDTO.getName());
        contact.setEmail(contactDTO.getEmail());

        when(contactService.create(contactDTO)).thenReturn(contact);

        mockMvc.perform(post("/api/contacts/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contactDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Contact"));
    }

    @Test
    public void testUpdateContact() throws Exception {
        Integer contactId = 1;
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("Updated Contact");
        contactDTO.setEmail("update@example.com");
        Contact updatedContact = new Contact();
        updatedContact.setId(contactId);
        updatedContact.setName(contactDTO.getName());
        updatedContact.setEmail(contactDTO.getEmail());

        when(contactService.update(contactId, contactDTO)).thenReturn(updatedContact);

        mockMvc.perform(put("/api/contacts/update/" + contactId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contactDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Contact"));
    }

    @Test
    public void testDeleteContact() throws Exception {
        Integer contactId = 1;

        doNothing().when(contactService).delete(contactId);

        mockMvc.perform(delete("/api/contacts/delete/" + contactId))
                .andExpect(status().isNoContent());
    }



}
