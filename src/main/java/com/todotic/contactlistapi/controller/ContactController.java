package com.todotic.contactlistapi.controller;

import com.todotic.contactlistapi.dto.ContactDTO;
import com.todotic.contactlistapi.entity.Contact;
import com.todotic.contactlistapi.service.ContactService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/contacts")
@RestController
public class ContactController {


    private final Logger logger = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;


    @GetMapping()
    public Iterable<Contact> list() {
        logger.info("Recibida solicitud para listar todos los contactos.");
        Iterable<Contact> contacts = contactService.findAll();
        logger.debug("Contactos listados: {}", contacts);
        return contacts;
    }

    @GetMapping("{id}")
    public Contact get(@PathVariable Integer id) {
        logger.info("Recibida solicitud para obtener contacto con ID: {}", id);
        Contact contact = contactService.findById(id);
        logger.debug("Contacto obtenido: {}", contact);
        return contact;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/new")
    public Contact create(@Validated @RequestBody ContactDTO contactDTO) {
        logger.info("Recibida solicitud para crear un nuevo contacto.");
        logger.debug("Datos para nuevo contacto: Email: {}, Name: {}", contactDTO.getEmail(), contactDTO.getName());
        Contact createdContact = contactService.create(contactDTO);
        logger.info("Nuevo contacto creado: {}", createdContact);
        return createdContact;
    }

    @PutMapping("/update/{id}")
    public Contact update(@Validated @PathVariable Integer id, @RequestBody ContactDTO contactDTO) {
        logger.info("Recibida solicitud para actualizar contacto con ID: {}", id);
        logger.debug("Datos para actualizar contacto: Email: {}, Name: {}", contactDTO.getEmail(), contactDTO.getName());
        Contact updatedContact = contactService.update(id, contactDTO);
        logger.info("Contacto actualizado: {}", updatedContact);
        return updatedContact;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        logger.info("Recibida solicitud para eliminar contacto con ID: {}", id);
        contactService.delete(id);
        logger.info("Contacto eliminado con éxito.");

    }


}
