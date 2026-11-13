
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    // Wait until all tasks in a pool have finished
    public static void waitForPool(ExecutorService pool)
            throws InterruptedException {

        while (!pool.awaitTermination(1, TimeUnit.MINUTES)) {
            System.out.println("Still waiting for workers...");
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        // Shared patient records
        Patientrecords repository = new Patientrecords();

        // Register 100 patients
        for (int i = 1; i <= 100; i++) {

            String id = String.format("PATIENT%03d", i);

            repository.addPatient(
                    new Patient(id, "Patient " + i)
            );
        }

        System.out.println("100 patients successfully registered.");

        // Create shared queues
        BlockingQueue<LabRequest> requestQueue =
                new ArrayBlockingQueue<>(20);

        BlockingQueue<LabResult> resultQueue =
                new ArrayBlockingQueue<>(20);

        BlockingQueue<PrescriptionRequest> prescriptionQueue =
                new ArrayBlockingQueue<>(20);

        // Shared medicine inventory
        MedicineInventory inventory = new MedicineInventory();

        // Create worker pools  System.out.print("Enter patient ID: ");
        ExecutorService nursePool =
                Executors.newFixedThreadPool(10);

        ExecutorService technicianPool =
                Executors.newFixedThreadPool(3);

        ExecutorService doctorPool =
                Executors.newFixedThreadPool(5);

        ExecutorService pharmacistPool =
                Executors.newFixedThreadPool(2);

        // Start pharmacists
        for (int i = 1; i <= 2; i++) {
            pharmacistPool.execute(
                    new Pharmacist(prescriptionQueue, inventory)
            );
        }

        // Start doctors
        for (int i = 1; i <= 5; i++) {
            doctorPool.execute(
                    new Doctor(repository, resultQueue, prescriptionQueue)
            );
        }

        // Start laboratory technicians
        for (int i = 1; i <= 3; i++) {
            technicianPool.execute(
                    new LaboratoryTechnician(requestQueue, resultQueue)
            );
        }

        // Accept patient IDs
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many patients do you want to process? ");

        int numberOfPatients =
                Integer.parseInt(scanner.nextLine().trim());

        if (numberOfPatients < 1 || numberOfPatients > 100) {
            System.out.println("Enter a number between 1 and 100.");
            return;
        }

        List<String> processedPatientIDs = new ArrayList<>();

        int entered = 0;

        // Collect patient IDs and their requested doses
        while (entered < numberOfPatients) {

            System.out.print("Enter patient ID (e.g. PATIENT001 to PATIENT100): ");
            String id = scanner.nextLine()
                    .trim()
                    .toUpperCase()
                    .replaceAll("\\s+", "");

            if (repository.getPatient(id) != null) {

                if (processedPatientIDs.contains(id)) {
                    System.out.println("Patient already entered!");
                    continue;
                }

                System.out.print("Enter number of doses for " + id + ": ");

                int doses = Integer.parseInt(scanner.nextLine().trim());

                if (doses < 1) {
                    System.out.println("Doses must be at least 1!");
                    continue;
                }

                Patient patient = repository.getPatient(id);
                patient.setRequestedDoses(doses);

                processedPatientIDs.add(id);
                entered++;

            } else {
                System.out.println("Patient not found! Try again.");
            }
        }

        System.out.println("\nAll patients registered for processing!");
        System.out.println("Starting hospital operations...\n");

        // Submit all nurse tasks after collecting IDs
        for (String id : processedPatientIDs) {
            nursePool.execute(new Nurse(repository, requestQueue, id));
        }

        // Wait for all nurses to finish
        nursePool.shutdown();
        waitForPool(nursePool);

        // Stop the three technicians
        for (int i = 1; i <= 3; i++) {
            requestQueue.put(LabRequest.stopRequest());
        }

        technicianPool.shutdown();
        waitForPool(technicianPool);

        // Stop the five doctors
        for (int i = 1; i <= 5; i++) {
            resultQueue.put(LabResult.stopResult());
        }

        doctorPool.shutdown();
        waitForPool(doctorPool);

        // Stop the two pharmacists
        for (int i = 1; i <= 2; i++) {
            prescriptionQueue.put(
                    PrescriptionRequest.stopRequest()
            );
        }

        pharmacistPool.shutdown();
        waitForPool(pharmacistPool);

        // Display final patient records
        System.out.println("\n========== PATIENT REPORTS ==========");

        for (String id : processedPatientIDs) {

            Patient patient = repository.getPatient(id);

            System.out.println("\nPatient ID: " + id);
            System.out.println("Name: " + patient.getName());
            System.out.println("Vital Signs: " + patient.getvitalsigns());
            System.out.println("Test Results: " + patient.gettestresults());
            System.out.println("Diagnosis: " + patient.getDiagnosis());
            System.out.println("Prescription: " + patient.getPrescription());
            System.out.println("Requested Doses: " + patient.getRequestedDoses());

            System.out.println("-------------------------------------");
        }

        System.out.println("\nHospital operations completed.");

        System.out.println(
                "Remaining medicine doses: " + inventory.getDoses()
        );

        scanner.close();
    }
}
