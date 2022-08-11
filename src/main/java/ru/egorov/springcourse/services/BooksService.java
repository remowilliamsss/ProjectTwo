package ru.egorov.springcourse.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.springcourse.models.Book;
import ru.egorov.springcourse.models.Person;
import ru.egorov.springcourse.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book findById(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);

        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);

        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Optional<Person> getBookOwner(int id) {
        Book foundBook = booksRepository.findById(id).get();

        return Optional.ofNullable(foundBook.getOwner());
    }

    @Transactional
    public void release(int id) {
        Book foundBook = booksRepository.findById(id).get();
        foundBook.setOwner(null);

        booksRepository.save(foundBook);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Book foundBook = booksRepository.findById(id).get();
        foundBook.setOwner(selectedPerson);

        booksRepository.save(foundBook);
    }
}
