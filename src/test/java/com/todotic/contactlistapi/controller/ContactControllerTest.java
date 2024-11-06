package com.todotic.contactlistapi.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todotic.contactlistapi.dto.ContactDTO;
import com.todotic.contactlistapi.entity.Contact;
import com.todotic.contactlistapi.service.ContactService;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ContactController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ContactControllerTest {
    @Autowired
    private ContactController contactController;

    @MockBean
    private ContactService contactService;

    /**
     * Test {@link ContactController#list()}.
     * <p>
     * Method under test: {@link ContactController#list()}
     */
    @Test
    @DisplayName("Test list()")
    void testList() throws Exception {
        // Arrange
        when(contactService.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/contacts");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(contactController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link ContactController#get(Integer)}.
     * <p>
     * Method under test: {@link ContactController#get(Integer)}
     */
    @Test
    @DisplayName("Test get(Integer)")
    void testGet() throws Exception {
        // Arrange
        Contact contact = new Contact();
        contact.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        contact.setEmail("jane.doe@example.org");
        contact.setId(1);
        contact.setName("Name");
        when(contactService.findById(Mockito.<Integer>any())).thenReturn(contact);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/contacts/{id}", 1);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(contactController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"createdAt\":[1970,1,1,0,0]}"));
    }

    /**
     * Test {@link ContactController#create(ContactDTO)}.
     * <ul>
     *   <li>Given {@code jane.doe@example.org}.</li>
     *   <li>Then status {@link StatusResultMatchers#isCreated()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ContactController#create(ContactDTO)}
     */
    @Test
    @DisplayName("Test create(ContactDTO); given 'jane.doe@example.org'; then status isCreated()")
    void testCreate_givenJaneDoeExampleOrg_thenStatusIsCreated() throws Exception {
        // Arrange
        Contact contact = new Contact();
        contact.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        contact.setEmail("jane.doe@example.org");
        contact.setId(1);
        contact.setName("Name");
        when(contactService.create(Mockito.<ContactDTO>any())).thenReturn(contact);

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setEmail("jane.doe@example.org");
        contactDTO.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(contactDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/contacts/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(contactController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"createdAt\":[1970,1,1,0,0]}"));
    }

    /**
     * Test {@link ContactController#create(ContactDTO)}.
     * <ul>
     *   <li>Given {@code U.U.U}.</li>
     *   <li>When {@link ContactDTO} (default constructor) Email is
     * {@code U.U.U}.</li>
     *   <li>Then status four hundred.</li>
     * </ul>
     * <p>
     * Method under test: {@link ContactController#create(ContactDTO)}
     */
    @Test
    @DisplayName("Test create(ContactDTO); given 'U.U.U'; when ContactDTO (default constructor) Email is 'U.U.U'; then status four hundred")
    void testCreate_givenUUU_whenContactDTOEmailIsUUU_thenStatusFourHundred() throws Exception {
        // Arrange
        Contact contact = new Contact();
        contact.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        contact.setEmail("jane.doe@example.org");
        contact.setId(1);
        contact.setName("Name");
        when(contactService.create(Mockito.<ContactDTO>any())).thenReturn(contact);

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setEmail("U.U.U");
        contactDTO.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(contactDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/contacts/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(contactController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Test {@link ContactController#update(Integer, ContactDTO)}.
     * <p>
     * Method under test: {@link ContactController#update(Integer, ContactDTO)}
     */
    @Test
    @DisplayName("Test update(Integer, ContactDTO)")
    void testUpdate() throws Exception {
        // Arrange
        Contact contact = new Contact();
        contact.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        contact.setEmail("jane.doe@example.org");
        contact.setId(1);
        contact.setName("Name");
        when(contactService.update(Mockito.<Integer>any(), Mockito.<ContactDTO>any())).thenReturn(contact);

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setEmail("jane.doe@example.org");
        contactDTO.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(contactDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/contacts/update/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(contactController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"createdAt\":[1970,1,1,0,0]}"));
    }

    /**
     * Test {@link ContactController#delete(Integer)}.
     * <p>
     * Method under test: {@link ContactController#delete(Integer)}
     */
    @Test
    @DisplayName("Test delete(Integer)")
    void testDelete() throws Exception {
        // Arrange
        doNothing().when(contactService).delete(Mockito.<Integer>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/contacts/delete/{id}", 1);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(contactController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
