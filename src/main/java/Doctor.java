import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person {
    private String specialty;
    private List<Appointment> appointments = new ArrayList<>();

    public Doctor(String id, String name, String specialty) {
        super(id, name);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void addAppointment(Appointment app) {
        appointments.add(app);
    }

    public void removeAppointment(Appointment app) {
        appointments.remove(app);
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public boolean isAvailable(LocalDate date, LocalTime time) {
        for (Appointment app : appointments) {
            if (app.getDate().equals(date) && app.getTime().equals(time)) {  // Asume no solapamiento exacto; ajusta para rangos
                return false;
            }
        }
        return true;
    }

    @Override
    public void displayInfo() {
        System.out.println("Doctor: " + getName() + ", ID: " + getId() + ", Especialidad: " + specialty);
    }
}