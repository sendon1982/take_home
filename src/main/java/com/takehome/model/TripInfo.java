package com.takehome.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TripInfo {

    private String medallion;

    private LocalDate pickupDate;
}
