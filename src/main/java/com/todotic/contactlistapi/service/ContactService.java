package com.todotic.contactlistapi.service;

import com.todotic.contactlistapi.dto.ContactDTO;
import com.todotic.contactlistapi.entity.Contact;
import com.todotic.contactlistapi.exception.ResourceNotFoundException;
import com.todotic.contactlistapi.repository.ContactRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;


@AllArgsConstructor
@Service
public class ContactService {

    private final Logger logger = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;
    private final ModelMapper mapper;

    public Iterable<Contact> findAll() {
        logger.info("Iniciando búsqueda de todos los contactos");
        Iterable<Contact> contacts = contactRepository.findAll();
        if (!contacts.iterator().hasNext()) {
            logger.info("No se encontraron contactos en la base de datos");
            return new ArrayList<>();
        }
        logger.info("Contactos encontrados: {}", contacts);
        return contacts;
    }

    public Contact findById(Integer id) {
        logger.info("Iniciando búsqueda de contacto con ID: {}", id);
        Contact contact = contactRepository.findById(id).orElseThrow(() -> {
            logger.error("No se encontró contacto con ID: {}", id);
            return new ResourceNotFoundException();
        });
        logger.info("Contacto encontrado: {}", contact);
        return contact;
    }

    public Contact create(ContactDTO contactDTO) {
        logger.info("Iniciando creación de un nuevo contacto.");
        logger.debug("Datos del contacto a crear: {}", contactDTO);
        Contact contact = mapper.map(contactDTO, Contact.class);
        contact.setCreatedAt(LocalDateTime.now());
        Contact savedContact = contactRepository.save(contact);
        logger.info("Contacto creado con éxito: {}", savedContact);
        return savedContact;
    }

    public Contact update(Integer id, ContactDTO contactDTO) {
        logger.info("Iniciando actualización de contacto con ID: {}", id);
        logger.debug("Datos para actualizar contacto: {}", contactDTO);
        Contact contactFromDB = findById(id);
        mapper.map(contactDTO, contactFromDB);
        Contact updatedContact = contactRepository.save(contactFromDB);
        logger.debug("Contacto actualizado con éxito: {}", updatedContact);
        return updatedContact;
    }

    public void delete(Integer id) {
        logger.info("Iniciando eliminación de contacto con ID: {}", id);
        Contact contact = contactRepository.findById(id).orElseThrow(() -> {
            logger.error("No se encontró contacto con ID: {}", id);
            return new ResourceNotFoundException();
        });
        contactRepository.delete(contact);
        logger.info("Contacto eliminado con éxito: {}", contact);
    }


}
