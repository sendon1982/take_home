package com.takehome.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.takehome.model.TripInfo;

public interface TripSearchDao {

    List<TripInfo> findTripInfosByIdAndDate(Set<String> medallions, LocalDate pickupDate);

}
