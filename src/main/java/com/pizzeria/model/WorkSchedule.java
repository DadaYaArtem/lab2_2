package com.pizzeria.model;

import com.pizzeria.model.users.Employee;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс расписания работы
 */
public class WorkSchedule {
    private Map<LocalDate, Map<Employee, Shift>> schedule;

    public WorkSchedule() {
        this.schedule = new HashMap<>();
    }

    public void addShift(LocalDate date, Employee employee, Shift shift) {
        schedule.computeIfAbsent(date, k -> new HashMap<>()).put(employee, shift);
        System.out.println("Добавлена смена для " + employee.getFullName() +
            " на " + date + ": " + shift.getStartTime() + " - " + shift.getEndTime());
    }

    public Shift getShift(LocalDate date, Employee employee) {
        Map<Employee, Shift> daySchedule = schedule.get(date);
        return daySchedule != null ? daySchedule.get(employee) : null;
    }

    public Map<Employee, Shift> getDaySchedule(LocalDate date) {
        return schedule.getOrDefault(date, new HashMap<>());
    }

    public boolean isEmployeeScheduled(LocalDate date, Employee employee) {
        return getShift(date, employee) != null;
    }

    public int getWorkingHoursForMonth(Employee employee, int month, int year) {
        int totalHours = 0;
        for (Map.Entry<LocalDate, Map<Employee, Shift>> entry : schedule.entrySet()) {
            LocalDate date = entry.getKey();
            if (date.getMonthValue() == month && date.getYear() == year) {
                Shift shift = entry.getValue().get(employee);
                if (shift != null) {
                    totalHours += shift.getDuration();
                }
            }
        }
        return totalHours;
    }
}
