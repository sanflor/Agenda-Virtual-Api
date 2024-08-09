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


@AllArgsConstructor
@Service
public class ContactService {

    private final Logger logger = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;
    private final ModelMapper mapper;

    public Iterable<Contact> findAll(){
        logger.info("Buscando todos los registros");
        Iterable<Contact> contacts = contactRepository.findAll();
        logger.debug("Registros encontrados: {}", contacts);
        return contacts;
    }

    public Contact findById(Integer id){
        logger.info("Buscando registro con el ID: {}", id);
        Contact contact = contactRepository.findById(id).orElseThrow(() -> {
            logger.error("No se encontró registro con ID: {}", id);
            return new ResourceNotFoundException();
        });
        logger.debug("Registro encontrado: {}", contact);
        return contact;
    }

    public Contact create(ContactDTO contactDTO) {
        logger.info("Creando un nuevo registro...");
        logger.debug("Datos recibidos para nuevo registro: {}", contactDTO);
        Contact contact = mapper.map(contactDTO, Contact.class );
        contact.setCreatedAt(LocalDateTime.now());
        Contact savedContact = contactRepository.save(contact);
        logger.debug("Registro creado con éxito: {}", savedContact);
        return savedContact;
    }

    public Contact update(Integer id, ContactDTO contactDTO) {
        logger.info("Actualizando registro con ID: {}", id);
        logger.debug("Datos recibidos para actualizar registro: {}", contactDTO);
        Contact contactFromDB = findById(id);
        mapper.map(contactDTO, contactFromDB );
        Contact updatedContact = contactRepository.save(contactFromDB);
        logger.debug("Contacto actualizado con éxito: {}", updatedContact);
        return updatedContact;
    }

    public void delete ( Integer id) {
        logger.info("Eliminando contacto con ID: {}", id);
        Contact contactFromDB = findById(id);
        contactRepository.delete(contactFromDB);
        logger.info("Contacto eliminado con exito.");

    }



}
