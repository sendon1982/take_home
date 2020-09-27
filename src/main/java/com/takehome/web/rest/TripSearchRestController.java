package com.takehome.web.rest;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.takehome.gen.api.TripApi;
import com.takehome.gen.model.TripSearchRequest;
import com.takehome.gen.model.TripSearchResponse;
import com.takehome.service.TripSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

@Slf4j
@RestController
public class TripSearchRestController implements TripApi {

    private final NativeWebRequest webRequest;

    private final TripSearchService tripSearchService;

    public TripSearchRestController(NativeWebRequest webRequest, TripSearchService tripSearchService) {
        this.webRequest = webRequest;
        this.tripSearchService = tripSearchService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(webRequest);
    }

    @Override
    public ResponseEntity<TripSearchResponse> searchTrip(@Valid TripSearchRequest request) {
        log.info("Search trip for request [{}]", request);

        Set<String> medallionSet = new TreeSet<>(request.getMedallions());
        TripSearchResponse searchResponse = tripSearchService.findTripInfosByIdAndDate(medallionSet, request.getPickupDate());

        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }

}
