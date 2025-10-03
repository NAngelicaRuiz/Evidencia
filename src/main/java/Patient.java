import java.util.ArrayList;
import java.util.List;

public class Patient extends Person {
    private List<Appointment> appointments = new ArrayList<>();

    public Patient(String id, String name) {
        super(id, name);
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

    @Override
    public void displayInfo() {
        System.out.println("Paciente: " + getName() + ", ID: " + getId());
    }
}