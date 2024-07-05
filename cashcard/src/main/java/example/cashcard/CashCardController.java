package example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.net.URI;

// The Controller gets injected into Spring Web
// This tells Spring that this class is a Component of type
// RestController and capable of handling HTTP requests.
@RestController
// Indicates which address requests must have to access this Controller
@RequestMapping("/cashcards")
class CashCardController {

    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    // @PathVariable -> comes from request URL
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {

        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);

        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    // @RequestBody -> comes from request body (json) -> Spring Web will deserialize the data provided into a CashCard for us.
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb) {
        CashCard savedCashCard = cashCardRepository.save(newCashCardRequest);
        URI locationOfNewCashCard = ucb.path("cashcards/{id}").buildAndExpand(savedCashCard.id()).toUri();
        return ResponseEntity.created(locationOfNewCashCard).build();
    }
}
