package com.techprimers.stock.dbservice.resource;

import com.techprimers.stock.dbservice.model.Quote;
import com.techprimers.stock.dbservice.model.Quotes;
import com.techprimers.stock.dbservice.repository.QuotesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/db")
public class DbServiceResource {

    public DbServiceResource(QuotesRepository quotesRepository) {
        this.quotesRepository = quotesRepository;
    }

    private QuotesRepository quotesRepository;

    @GetMapping("/{username}")
    public List<String> getQuotes(@PathVariable("username") final String username) {

        return getQuotesByUsername(username);
    }

    private List<String> getQuotesByUsername(@PathVariable("username") String username) {
        return quotesRepository.findByUserName(username)
                .stream()
                .map(Quote::getQuote)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{username}")
    public List<Quote> delete(@PathVariable("username") String username) {

        List<Quote> quotes = quotesRepository.findByUserName(username);
        quotesRepository.deleteInBatch(quotes);

        return quotesRepository.findByUserName(username);
    }

    @PostMapping("/add")
    public List<String> add(@RequestBody final Quotes quotes) {

        quotes.getQuotes()
                .stream()
                .map(quote -> new Quote(quotes.getUserName(), quote))
                .forEach(quote -> quotesRepository.save(quote));

        return getQuotesByUsername(quotes.getUserName());
    }
}
