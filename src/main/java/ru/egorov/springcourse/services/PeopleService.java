package ru.egorov.springcourse.services;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.springcourse.models.Book;
import ru.egorov.springcourse.models.Person;
import ru.egorov.springcourse.repositories.PeopleRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);

        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);

        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);

        if (foundPerson.isPresent()) {
            Hibernate.initialize(foundPerson.get().getBooks());

            foundPerson.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());

                if (diffInMillies > 864000000)
                    book.setBookOverdue(true);
            });
            return foundPerson.get().getBooks();
        } else
            return Collections.emptyList();
    }
}
