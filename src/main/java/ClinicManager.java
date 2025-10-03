import java.util.ArrayList;
import java.util.List;

public class ClinicManager {
    private List<Doctor> doctors = new ArrayList<>();
    private List<Patient> patients = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();
    private Administrator admin;
    private FileManager fileManager;

    public ClinicManager(Administrator admin, FileManager fileManager) {
        this.admin = admin;
        this.fileManager = fileManager;
        loadData();
    }

    public boolean authenticateAdmin(String id, String password) {
        return admin.authenticate(id, password);
    }

    public void addDoctor(Doctor doc) {
        try {
            if (!doctors.contains(doc)) {
                doctors.add(doc);
                fileManager.saveDoctors(doctors);
            } else {
                System.out.println("ID de doctor ya existe");
            }
        } catch (Exception e) {
            System.out.println("Error al agregar doctor: " + e.getMessage());
        }
    }

    public void addPatient(Patient pat) {
        try {
            if (!patients.contains(pat)) {
                patients.add(pat);
                fileManager.savePatients(patients);
            } else {
                System.out.println("ID de paciente ya existe");
            }
        } catch (Exception e) {
            System.out.println("Error al agregar paciente: " + e.getMessage());
        }
    }

    public boolean scheduleAppointment(Appointment app) {
        try {
            if (app.getDoctor().isAvailable(app.getDate(), app.getTime())) {
                appointments.add(app);
                app.getDoctor().addAppointment(app);
                app.getPatient().addAppointment(app);
                fileManager.saveAppointments(appointments, doctors, patients);
                return true;
            } else {
                System.out.println("Doctor no disponible en esa fecha y hora");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error al programar cita: " + e.getMessage());
            return false;
        }
    }

    public void cancelAppointment(String appId) {
        try {
            Appointment appToRemove = null;
            for (Appointment app : appointments) {
                if (app.getId().equals(appId)) {
                    appToRemove = app;
                    break;
                }
            }
            if (appToRemove != null) {
                appointments.remove(appToRemove);
                appToRemove.getDoctor().removeAppointment(appToRemove);
                appToRemove.getPatient().removeAppointment(appToRemove);
                fileManager.saveAppointments(appointments, doctors, patients);
            } else {
                System.out.println("Cita no encontrada");
            }
        } catch (Exception e) {
            System.out.println("Error al cancelar cita: " + e.getMessage());
        }
    }

    public List<Appointment> viewAppointmentsByDoctor(String docId) {
        try {
            for (Doctor doc : doctors) {
                if (doc.getId().equals(docId)) {
                    return doc.getAppointments();
                }
            }
            System.out.println("Doctor no encontrado");
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Error al visualizar citas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Appointment> viewAppointmentsByPatient(String patId) {
        try {
            for (Patient pat : patients) {
                if (pat.getId().equals(patId)) {
                    return pat.getAppointments();
                }
            }
            System.out.println("Paciente no encontrado");
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Error al visualizar citas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveData() {
        try {
            fileManager.saveDoctors(doctors);
            fileManager.savePatients(patients);
            fileManager.saveAppointments(appointments, doctors, patients);
        } catch (Exception e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }

    public void loadData() {
        try {
            doctors = fileManager.loadDoctors();
            patients = fileManager.loadPatients();
            appointments = fileManager.loadAppointments(doctors, patients);
        } catch (Exception e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }

    // Getters para listas (para selección en menú)
    public List<Doctor> getDoctors() {
        return new ArrayList<>(doctors);
    }

    public List<Patient> getPatients() {
        return new ArrayList<>(patients);
    }

    public Appointment findAppointmentById(String id) {
        for (Appointment app : appointments) {
            if (app.getId().equals(id)) {
                return app;
            }
        }
        return null;
    }
}