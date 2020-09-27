package com.takehome.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.takehome.dao.TripSearchDao;
import com.takehome.gen.model.TripSearchResponse;
import com.takehome.model.TripInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.any;

class TripSearchServiceImplTest {

    public static final LocalDate PICKUP_DATE = LocalDate.parse("2020-02-02");

    private TripSearchServiceImpl tripSearchService;

    @Mock
    private TripSearchDao tripSearchDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        tripSearchService = new TripSearchServiceImpl(tripSearchDao);
    }

    @Test
    void test_findTripInfosByIdAndDate_NotValid_Input() {
        TripSearchResponse searchResponse = tripSearchService.findTripInfosByIdAndDate(null, PICKUP_DATE);

        assertThat(searchResponse, notNullValue());
        assertThat(searchResponse.getData(), notNullValue());

        Mockito.verifyNoInteractions(tripSearchDao);
    }

    @Test
    void test_findTripInfosByIdAndDate_Dao_ReturnNull() {
        Mockito.when(tripSearchDao.findTripInfosByIdAndDate(anySet(), any(LocalDate.class))).thenReturn(null);

        Set<String> medallionSet = Set.of("123");

        TripSearchResponse searchResponse = tripSearchService.findTripInfosByIdAndDate(medallionSet, PICKUP_DATE);

        assertThat(searchResponse, notNullValue());
        assertThat(searchResponse.getData(), notNullValue());

        Mockito.verify(tripSearchDao).findTripInfosByIdAndDate(medallionSet, PICKUP_DATE);
        Mockito.verifyNoMoreInteractions(tripSearchDao);
    }

    @Test
    void test_findTripInfosByIdAndDate_Dao_ReturnCorrectValues() {
        Mockito.when(tripSearchDao.findTripInfosByIdAndDate(anySet(), any(LocalDate.class))).thenReturn(buildTripResponse());

        Set<String> medallionSet = Set.of("123a", "bcd");

        TripSearchResponse searchResponse = tripSearchService.findTripInfosByIdAndDate(medallionSet, PICKUP_DATE);

        assertThat(searchResponse, notNullValue());
        assertThat(searchResponse.getData(), notNullValue());


        assertThat(searchResponse.getData().getMedallionTrips(), allOf(
            hasItems(
                allOf(
                    hasProperty("medallion", equalTo("123a")),
                    hasProperty("tripCount", equalTo(2L))
                ),
                allOf(
                    hasProperty("medallion", equalTo("bcd")),
                    hasProperty("tripCount", equalTo(1L))
                )
            )
        ));

        Mockito.verify(tripSearchDao).findTripInfosByIdAndDate(medallionSet, PICKUP_DATE);
        Mockito.verifyNoMoreInteractions(tripSearchDao);
    }

    private List<TripInfo> buildTripResponse() {
        List<TripInfo> tripInfos = new ArrayList<>();

        tripInfos.add( buildTripInfo("123a", PICKUP_DATE));
        tripInfos.add( buildTripInfo("123a", PICKUP_DATE));
        tripInfos.add( buildTripInfo("bcd", PICKUP_DATE));
        tripInfos.add( buildTripInfo("xyx", PICKUP_DATE));

        return tripInfos;
    }

    private TripInfo buildTripInfo(String medallion, LocalDate pickupDate) {
        TripInfo tripInfo = new TripInfo();

        tripInfo.setMedallion(medallion);
        tripInfo.setPickupDate(pickupDate);

        return tripInfo;
    }

}