package one.digitalinnovation.personapi.service;


import one.digitalinnovation.personapi.dto.MessageResponseDTO;
import one.digitalinnovation.personapi.dto.PersonDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.PortUnreachableException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

        private PersonRepository personRepository;

        private final PersonMapper personMapper = PersonMapper.INSTANCE;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
        }

    @Autowired
    public MessageResponseDTO createPerson(PersonDTO personDTO) {
        Person personToSave = personMapper.toModel(personDTO);


        Person savedPerson = personRepository.save(personToSave);
        return createMessageResponse(savedPerson.getId());
    }

    private MessageResponseDTO createMessageResponse(Long id) {
        return MessageResponseDTO
                .builder()
                .message("Created person with ID")
                .build();
    }

    public List<PersonDTO> listAll() {
        List<Person> allPeople = personRepository.findAll();
        return allPeople.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Person person = VerifyIfExists(id);
                    return personMapper.toDTO(person);
        }

    private Person VerifyIfExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    public void delete(Long id) throws PersonNotFoundException {
        VerifyIfExists(id);
        personRepository.deleteById(id);
    }


    public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        this.findById(id);
        Person personToUpdate = personMapper.toModel(personDTO);

        Person savedPerson = personRepository.save(personToUpdate);

        return null;
    }
}



