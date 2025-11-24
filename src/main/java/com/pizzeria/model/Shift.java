package com.pizzeria.model;

import java.time.LocalTime;

/**
 * Класс смены
 */
public class Shift {
    private LocalTime startTime;
    private LocalTime endTime;
    private String type; // morning, afternoon, evening, night

    public Shift(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = determineShiftType();
    }

    public Shift(String type) {
        this.type = type;
        switch (type.toLowerCase()) {
            case "morning":
                this.startTime = LocalTime.of(8, 0);
                this.endTime = LocalTime.of(16, 0);
                break;
            case "afternoon":
                this.startTime = LocalTime.of(12, 0);
                this.endTime = LocalTime.of(20, 0);
                break;
            case "evening":
                this.startTime = LocalTime.of(16, 0);
                this.endTime = LocalTime.of(24, 0);
                break;
            default:
                this.startTime = LocalTime.of(9, 0);
                this.endTime = LocalTime.of(17, 0);
        }
    }

    private String determineShiftType() {
        int hour = startTime.getHour();
        if (hour < 12) return "morning";
        if (hour < 17) return "afternoon";
        return "evening";
    }

    public int getDuration() {
        return endTime.getHour() - startTime.getHour();
    }

    public boolean isNightShift() {
        return startTime.getHour() >= 22 || endTime.getHour() <= 6;
    }

    // Getters and Setters
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return String.format("%s (%s - %s)", type, startTime, endTime);
    }
}
