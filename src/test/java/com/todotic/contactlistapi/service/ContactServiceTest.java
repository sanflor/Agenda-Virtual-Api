package com.todotic.contactlistapi.service;

import com.todotic.contactlistapi.dto.ContactDTO;
import com.todotic.contactlistapi.entity.Contact;
import com.todotic.contactlistapi.exception.ResourceNotFoundException;
import com.todotic.contactlistapi.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
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
        Integer id = 2;
        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            contactService.findById(id);
        });
    }

    @Test
    public void testFindAllNoContacts() {
        when(contactRepository.findAll()).thenReturn(new ArrayList<>());
        Iterable<Contact> contacts = contactService.findAll();
        assertFalse(contacts.iterator().hasNext());
    }

    @Test
    public void testFindAllWithContacts() {
        List<Contact> mockContacts = Arrays.asList(new Contact(), new Contact());
        when(contactRepository.findAll()).thenReturn(mockContacts);
        Iterable<Contact> contacts = contactService.findAll();
        assertTrue(contacts.iterator().hasNext());
    }

    @Test
    public void testCreateContact() {
        ContactDTO dto = new ContactDTO();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        Contact contact = new Contact();
        when(modelMapper.map(dto, Contact.class)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);

        Contact created = contactService.create(dto);
        assertNotNull(created);
        verify(contactRepository).save(contact);
    }

    @Test
    public void testUpdateContactNotFound() {
        Integer id = 999;
        ContactDTO dto = new ContactDTO();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contactService.update(id, dto));
    }

    @Test
    public void testDeleteContactNotFound() {
        Integer id = 999;
        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contactService.delete(id));
    }

}
