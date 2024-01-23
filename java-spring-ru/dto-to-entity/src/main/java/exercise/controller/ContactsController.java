package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactRepository contactRepository;

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO save(@RequestBody ContactCreateDTO contactCreateDTO) {
        return toContactDTO(contactRepository.save(toContact(contactCreateDTO)));
    }

    private Contact toContact(ContactCreateDTO contactCreateDTO) {
        var contact = new Contact();
        contact.setPhone(contactCreateDTO.getPhone());
        contact.setFirstName(contactCreateDTO.getFirstName());
        contact.setLastName(contactCreateDTO.getLastName());
        return contact;
    }

    private ContactDTO toContactDTO(Contact contact) {
        var contactDTO = new ContactDTO();
        contactDTO.setId(contact.getId());
        contactDTO.setPhone(contact.getPhone());
        contactDTO.setFirstName(contact.getFirstName());
        contactDTO.setLastName(contact.getLastName());
        contactDTO.setUpdatedAt(contact.getUpdatedAt());
        contactDTO.setCreatedAt(contact.getCreatedAt());
        return contactDTO;
    }
}
