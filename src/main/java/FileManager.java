import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String DB_DIR = "db/";
    private static final String DOCTORS_FILE = DB_DIR + "doctors.csv";
    private static final String PATIENTS_FILE = DB_DIR + "patients.csv";
    private static final String APPOINTMENTS_FILE = DB_DIR + "appointments.csv";

    public FileManager() {
        // Crear carpeta db si no existe
        File dbDir = new File(DB_DIR);
        if (!dbDir.exists()) {
            dbDir.mkdir();
        }
        // Crear archivos si no existen
        createFileIfNotExists(DOCTORS_FILE);
        createFileIfNotExists(PATIENTS_FILE);
        createFileIfNotExists(APPOINTMENTS_FILE);
    }

    private void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear archivo: " + filePath + " - " + e.getMessage());
            }
        }
    }

    public void saveDoctors(List<Doctor> doctors) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOCTORS_FILE))) {
            for (Doctor doc : doctors) {
                writer.write(doc.getId() + "," + doc.getName() + "," + doc.getSpecialty());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar doctores: " + e.getMessage());
        }
    }

    public List<Doctor> loadDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTORS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    doctors.add(new Doctor(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar doctores: " + e.getMessage());
        }
        return doctors;
    }

    public void savePatients(List<Patient> patients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATIENTS_FILE))) {
            for (Patient pat : patients) {
                writer.write(pat.getId() + "," + pat.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar pacientes: " + e.getMessage());
        }
    }

    public List<Patient> loadPatients() {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    patients.add(new Patient(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar pacientes: " + e.getMessage());
        }
        return patients;
    }

    public void saveAppointments(List<Appointment> appointments, List<Doctor> doctors, List<Patient> patients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE))) {
            for (Appointment app : appointments) {
                writer.write(app.getId() + "," + app.getDate() + "," + app.getTime() + "," + app.getReason() + "," + app.getDoctor().getId() + "," + app.getPatient().getId());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar citas: " + e.getMessage());
        }
    }

    public List<Appointment> loadAppointments(List<Doctor> doctors, List<Patient> patients) {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String id = parts[0];
                    LocalDate date = LocalDate.parse(parts[1]);
                    LocalTime time = LocalTime.parse(parts[2]);
                    String reason = parts[3];
                    String docId = parts[4];
                    String patId = parts[5];

                    Doctor doctor = findDoctorById(doctors, docId);
                    Patient patient = findPatientById(patients, patId);

                    if (doctor != null && patient != null) {
                        Appointment app = new Appointment(id, date, time, reason, doctor, patient);
                        appointments.add(app);
                        doctor.addAppointment(app);
                        patient.addAppointment(app);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar citas: " + e.getMessage());
        }
        return appointments;
    }

    private Doctor findDoctorById(List<Doctor> doctors, String id) {
        for (Doctor doc : doctors) {
            if (doc.getId().equals(id)) {
                return doc;
            }
        }
        return null;
    }

    private Patient findPatientById(List<Patient> patients, String id) {
        for (Patient pat : patients) {
            if (pat.getId().equals(id)) {
                return pat;
            }
        }
        return null;
    }
}