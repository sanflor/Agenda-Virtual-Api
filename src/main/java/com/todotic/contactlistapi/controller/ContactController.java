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
    public Iterable<Contact> list(){
        logger.info("Solicitud para listar todos los registros");
        Iterable<Contact> contacts = contactService.findAll();
        logger.debug("Registros listados: {}", contacts);
        return contacts;
    }

    @GetMapping("{id}")
    public Contact get (@PathVariable Integer id){
        logger.info("Solicitud para obtener registro con ID: {}", id);
        Contact contact = contactService.findById(id);
        logger.debug("Registro obtenido: {}", contact);
        return contact;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/new")
    public Contact create(@Validated @RequestBody ContactDTO contactDTO) {
        logger.info("Solicitud para crear nuevo registro");
        logger.debug("Datos recibidos para nuevo registro: {}", contactDTO);
        Contact createdContact = contactService.create(contactDTO);
        logger.debug("Nuevo registro creado: {}", createdContact);
        return createdContact;
    }

    @PutMapping("/update/{id}")
    public Contact update(@Validated @PathVariable Integer id, @RequestBody ContactDTO contactDTO) {
        logger.info("Solicitud para actualizar registro con ID: {}", id);
        logger.debug("Datos recibidos para actualizar registro: {}", contactDTO);
        Contact updatedContact = contactService.update(id, contactDTO);
        logger.debug("Registro actualizado: {}", updatedContact);
        return updatedContact;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void delete (@PathVariable Integer id) {
        logger.info("Solicitud para eliminar registro con ID: {}", id);
        contactService.delete(id);
        logger.info("Registro eliminado con éxito");

    }





}
