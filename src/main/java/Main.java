import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Administrator admin = new Administrator("admin", "1234"); // Cambia contraseña si quieres
        FileManager fileManager = new FileManager();
        ClinicManager manager = new ClinicManager(admin, fileManager);

        Scanner scanner = new Scanner(System.in);
        boolean authenticated = false;

        // Autenticación
        while (!authenticated) {
            System.out.println("Ingrese ID de administrador:");
            String id = scanner.nextLine();
            System.out.println("Ingrese contraseña:");
            String password = scanner.nextLine();
            try {
                authenticated = manager.authenticateAdmin(id, password);
                if (!authenticated) {
                    System.out.println("Credenciales inválidas, intenta de nuevo");
                }
            } catch (Exception e) {
                System.out.println("Error en autenticación: " + e.getMessage());
            }
        }

        // Menú principal
        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Registrar Doctor");
            System.out.println("2. Registrar Paciente");
            System.out.println("3. Crear Cita");
            System.out.println("4. Visualizar Citas");
            System.out.println("5. Modificar o Cancelar Cita");
            System.out.println("6. Salir");
            String option = scanner.nextLine();

            if (option.equals("1")) {
                try {
                    System.out.println("ID:");
                    String id = scanner.nextLine();
                    System.out.println("Nombre:");
                    String name = scanner.nextLine();
                    System.out.println("Especialidad:");
                    String specialty = scanner.nextLine();
                    Doctor newDoc = new Doctor(id, name, specialty);
                    manager.addDoctor(newDoc);
                    System.out.println("Doctor registrado con éxito");
                } catch (Exception e) {
                    System.out.println("Error al registrar doctor: " + e.getMessage());
                }
            } else if (option.equals("2")) {
                try {
                    System.out.println("ID:");
                    String id = scanner.nextLine();
                    System.out.println("Nombre:");
                    String name = scanner.nextLine();
                    Patient newPat = new Patient(id, name);
                    manager.addPatient(newPat);
                    System.out.println("Paciente registrado con éxito");
                } catch (Exception e) {
                    System.out.println("Error al registrar paciente: " + e.getMessage());
                }
            } else if (option.equals("3")) {
                try {
                    // Mostrar pacientes
                    System.out.println("Pacientes disponibles:");
                    for (Patient pat : manager.getPatients()) {
                        pat.displayInfo();
                    }
                    System.out.println("ID de paciente:");
                    String patId = scanner.nextLine();
                    Patient patient = null;
                    for (Patient p : manager.getPatients()) {
                        if (p.getId().equals(patId)) {
                            patient = p;
                            break;
                        }
                    }

                    // Mostrar doctores
                    System.out.println("Doctores disponibles:");
                    for (Doctor doc : manager.getDoctors()) {
                        doc.displayInfo();
                    }
                    System.out.println("ID de doctor:");
                    String docId = scanner.nextLine();
                    Doctor doctor = null;
                    for (Doctor d : manager.getDoctors()) {
                        if (d.getId().equals(docId)) {
                            doctor = d;
                            break;
                        }
                    }

                    if (doctor != null && patient != null) {
                        System.out.println("Fecha (YYYY-MM-DD):");
                        LocalDate date = LocalDate.parse(scanner.nextLine());
                        System.out.println("Hora (HH:MM):");
                        LocalTime time = LocalTime.parse(scanner.nextLine());
                        System.out.println("Motivo:");
                        String reason = scanner.nextLine();

                        Appointment newApp = new Appointment(date, time, reason, doctor, patient);
                        if (manager.scheduleAppointment(newApp)) {
                            System.out.println("Cita programada con éxito");
                        } else {
                            System.out.println("Error: Doctor no disponible");
                        }
                    } else {
                        System.out.println("Doctor o paciente no encontrado");
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de fecha/hora inválido: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error al crear cita: " + e.getMessage());
                }
            } else if (option.equals("4")) {
                try {
                    System.out.println("Visualizar por (doctor/paciente):");
                    String criterio = scanner.nextLine();
                    if (criterio.equals("doctor")) {
                        System.out.println("ID de doctor:");
                        String docId = scanner.nextLine();
                        List<Appointment> apps = manager.viewAppointmentsByDoctor(docId);
                        for (Appointment app : apps) {
                            app.displayDetails();
                            System.out.println("---");
                        }
                    } else if (criterio.equals("paciente")) {
                        System.out.println("ID de paciente:");
                        String patId = scanner.nextLine();
                        List<Appointment> apps = manager.viewAppointmentsByPatient(patId);
                        for (Appointment app : apps) {
                            app.displayDetails();
                            System.out.println("---");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error al visualizar citas: " + e.getMessage());
                }
            } else if (option.equals("5")) {
                try {
                    System.out.println("ID de cita:");
                    String appId = scanner.nextLine();
                    Appointment app = manager.findAppointmentById(appId);
                    if (app != null) {
                        app.displayDetails();
                        System.out.println("Acción (modificar/cancelar):");
                        String action = scanner.nextLine();
                        if (action.equals("modificar")) {
                            System.out.println("Nueva fecha (YYYY-MM-DD):");
                            LocalDate newDate = LocalDate.parse(scanner.nextLine());
                            System.out.println("Nueva hora (HH:MM):");
                            LocalTime newTime = LocalTime.parse(scanner.nextLine());
                            System.out.println("Nuevo motivo:");
                            String newReason = scanner.nextLine();

                            // Cancelar cita actual para verificar disponibilidad
                            manager.cancelAppointment(appId);

                            // Crear nueva cita con datos actualizados
                            Appointment updatedApp = new Appointment(appId, newDate, newTime, newReason, app.getDoctor(), app.getPatient());
                            if (manager.scheduleAppointment(updatedApp)) {
                                System.out.println("Cita modificada con éxito");
                            } else {
                                // Revertir: reprogramar cita original si falla
                                manager.scheduleAppointment(app);
                                System.out.println("Error: Doctor no disponible");
                            }
                        } else if (action.equals("cancelar")) {
                            manager.cancelAppointment(appId);
                            System.out.println("Cita cancelada con éxito");
                        }
                    } else {
                        System.out.println("Cita no encontrada");
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de fecha/hora inválido: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error al modificar/cancelar cita: " + e.getMessage());
                }
            } else if (option.equals("6")) {
                manager.saveData();
                System.out.println("Datos guardados, saliendo...");
                break;
            }
        }
        scanner.close();
    }
}