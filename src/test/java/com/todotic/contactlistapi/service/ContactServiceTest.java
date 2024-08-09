package com.todotic.contactlistapi.service;

import com.todotic.contactlistapi.entity.Contact;
import com.todotic.contactlistapi.exception.ResourceNotFoundException;
import com.todotic.contactlistapi.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ContactService contactService;

    @Test
    public void testFindByIdWhenContactExists() {
        Integer id = 1;
        Contact contact = new Contact();
        contact.setId(id);
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        Contact found = contactService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testFindByIdWhenContactDoesNotExists() {
        Integer id = 1;
        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            contactService.findById(id);
        });
    }
}
