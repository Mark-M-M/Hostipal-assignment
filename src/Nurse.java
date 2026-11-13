
import java.util.concurrent.BlockingQueue;

public class Nurse implements Runnable {

    private Patientrecords repository;
    private BlockingQueue<LabRequest> requestQueue;
    private String patientID;

    public Nurse(Patientrecords repository,
                 BlockingQueue<LabRequest> requestQueue,
                 String patientID) {

        this.repository = repository;
        this.requestQueue = requestQueue;
        this.patientID = patientID;
    }

    @Override
    public void run() {
        try {
            Patient patient = repository.getPatient(patientID);

            if (patient != null) {
                System.out.println("NURSE: Checking " + patientID);

                patient.setvitalSigns("BP: 120/80");

                System.out.println("NURSE: Vital signs recorded for " + patientID);

                System.out.println("NURSE: Sending lab request for " + patientID);

                requestQueue.put(new LabRequest(patientID, "Blood test"));
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}