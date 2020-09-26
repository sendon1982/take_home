package com.takehome.service;

import java.time.LocalDate;
import java.util.Set;

import com.takehome.gen.gen.model.TripSearchResponse;

public interface TripSearchService {

    TripSearchResponse findTripInfosByIdAndDate(Set<String> ids, LocalDate pickupDate);
}
