package com.takehome.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.takehome.model.TripInfo;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;

import static com.takehome.dao.jooq.domain.Tables.CAB_TRIP_DATA;

public class TripSearchDaoImpl implements TripSearchDao {

    private final DSLContext context;

    public TripSearchDaoImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public List<TripInfo> findTripInfosByIdAndDate(Set<String> medallions, LocalDate pickupDate) {
        Result<Record2<String, LocalDateTime>> results =
            context.select(CAB_TRIP_DATA.MEDALLION, CAB_TRIP_DATA.PICKUP_DATETIME)
                   .from(CAB_TRIP_DATA)
                   .where(CAB_TRIP_DATA.MEDALLION.in(medallions).and(CAB_TRIP_DATA.PICKUP_DATETIME.cast(LocalDate.class).eq(pickupDate)))
                   .fetch();

        List<TripInfo> tripInfoList = new ArrayList<>();

        for (Record2<String, LocalDateTime> record : results) {
            TripInfo tripInfo = new TripInfo();
            tripInfo.setMedallion(record.get(CAB_TRIP_DATA.MEDALLION));
            tripInfo.setPickupDate(record.get(CAB_TRIP_DATA.PICKUP_DATETIME).toLocalDate());

            tripInfoList.add(tripInfo);
        }

        return tripInfoList;
    }
}
