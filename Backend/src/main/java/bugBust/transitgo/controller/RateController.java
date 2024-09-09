package bugBust.transitgo.controller;

import bugBust.transitgo.exception.RateNotFoundException;
import bugBust.transitgo.exception.UnauthorizedException;
import bugBust.transitgo.model.BusMgt;
import bugBust.transitgo.model.RateReviews;
import bugBust.transitgo.repository.BusMgtRepository;
import bugBust.transitgo.repository.RateRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = {"http://localhost:3000" , "http://localhost:8081"})
public class RateController {
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private  BusMgtRepository busMgtRepository;


    //have to add @valid before @RequestBody to valid while posting data
//    @PostMapping("/rate")
//    RateReviews newRateReviews(@Valid @RequestBody RateReviews newRateReviews){
//        return rateRepository.save(newRateReviews);
//    }

    @RequestMapping("/rate/bus")
    public ResponseEntity<RateReviews> createRateReview(@RequestBody Map<String, Object> rateReviewData){
        Map<String, Object> busesData = (Map<String, Object>) rateReviewData.get("buses");
        int busId = (int) busesData.get("busId");

        BusMgt bus = busMgtRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        RateReviews rateReviews = new RateReviews();
        rateReviews.setBus(bus);
        rateReviews.setRate(rateReviewData.get("rate").toString());
        rateReviews.setReview(rateReviewData.get("review").toString());
        rateReviews.setUsername(rateReviewData.get("username").toString());

        RateReviews savedReview = rateRepository.save(rateReviews);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }

    @GetMapping("/rates")
    List<RateReviews> getAllReviews(){

       return rateRepository.findAll();
    }

    @GetMapping("/rates/{busId}")
    List<RateReviews> getAllReviewsFromBusId(@PathVariable  int busId){
        return rateRepository.findByBusId(busId);
    }

    @GetMapping("/rate/{id}") //to edit specific rate with id
    RateReviews geRateReviewsById(@PathVariable Long id){
        String currentUser = getCurrentUSer();
        return rateRepository.findById(id)
                .map(rateReviews -> {
                    if (!rateReviews.getUsername().equals(currentUser)) {
                        throw new UnauthorizedException("You are not authorized to view this review");
                    }
                    return rateReviews;
                }).orElseThrow(() -> new RateNotFoundException(id));
    }

    //have to add @valid before @RequestBody to valid while edit
    @PutMapping("/rate/{id}")  //edit rates
    RateReviews updateRateReviews(@Valid @RequestBody RateReviews newRateReviews, @PathVariable Long id) {
        String currentUser = getCurrentUSer();
        return rateRepository.findById(id)
                .map(rateReviews -> {
                    if(!rateReviews.getUsername().equals(currentUser)){
                        throw new UnauthorizedException("You are not authorized to update this review");
                    }
                    rateReviews.setRate(newRateReviews.getRate());
                    rateReviews.setReview(newRateReviews.getReview());
                    rateReviews.setCreatedAt(LocalDateTime.now());  // Update the createdAt timestamp
                    return rateRepository.save(rateReviews);
                }).orElseThrow(() -> new RateNotFoundException(id));
    }

    @DeleteMapping("/rate/{id}") //delete user with specific id
    String deleteRateReview(@PathVariable Long id){
        String currentUser = getCurrentUSer();
        return rateRepository.findById(id)
                .map(rateReviews -> {
                    if (!rateReviews.getUsername().equals(currentUser)){
                        throw new UnauthorizedException("You are not authorized to delete this review");
                    }
                    rateRepository.deleteById(id);
                    return "User with id " + id + " has been deleted successfully.";
                }).orElseThrow(() -> new RateNotFoundException(id));
        }



    // to give a detailed error message for valid -notnull-400 bad request
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error)  -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    private String getCurrentUSer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
