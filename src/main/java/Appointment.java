import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Appointment {
    private String id;
    private LocalDate date;
    private LocalTime time;
    private String reason;
    private Doctor doctor;
    private Patient patient;

    public Appointment(LocalDate date, LocalTime time, String reason, Doctor doctor, Patient patient) {
        this.id = UUID.randomUUID().toString();  // ID único
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Appointment(String id, LocalDate date, LocalTime time, String reason, Doctor doctor, Patient patient) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.doctor = doctor;
        this.patient = patient;
    }

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void displayDetails() {
        System.out.println("Cita ID: " + id + ", Fecha: " + date + ", Hora: " + time + ", Motivo: " + reason);
        doctor.displayInfo();
        patient.displayInfo();
    }
}