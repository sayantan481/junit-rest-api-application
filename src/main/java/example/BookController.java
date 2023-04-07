package example;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class BookController {


    @Autowired
    BookRepository bookRepository;

    @GetMapping(value = "/book")
    public List<Book> getAllBookRecords () {
        return bookRepository.findAll();
    }

    @GetMapping(value="/book/{bookId}")
    public Book getBookById(@PathVariable (value="bookId" ) Long bookId){
        return bookRepository.findById(bookId).get();
    }

    @PostMapping(value = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public Book createBookRecord(@RequestBody @Valid Book bookRecord){
        return bookRepository.save(bookRecord);
    }

    @PutMapping(value = "/book")
    public Book updateBookRecord(@RequestBody @Valid Book bookRecord) throws NotFoundException {
        if(bookRecord == null || bookRecord.getBookId()== null || (! bookRepository.findById(bookRecord.getBookId()).isPresent())){
            throw new NoSuchElementException("Book Record or ID must  be valid");
        }
        Book existingBook =bookRepository.findById(bookRecord.getBookId()).get();
        existingBook.setName(bookRecord.getName());
        existingBook.setSummary(bookRecord.getSummary());
        existingBook.setRating(bookRecord.getRating());
        return bookRepository.save(bookRecord);

    }

    @DeleteMapping(value = "book/{bookId}")
    public void deleteBookById(@PathVariable(value = "bookId") Long bookId) throws NotFoundException
    {
        if(!bookRepository.findById(bookId).isPresent()){
            throw new NotFoundException("Book with Id :"+ bookId +"does not exist");
        }
        bookRepository.deleteById(bookId);
    }
}
