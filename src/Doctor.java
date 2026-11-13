
import java.util.concurrent.BlockingQueue;

public class Doctor implements Runnable {

    private Patientrecords repository;
    private BlockingQueue<LabResult> resultQueue;
    private BlockingQueue<PrescriptionRequest> prescriptionQueue;

    public Doctor(
            Patientrecords repository,
            BlockingQueue<LabResult> resultQueue,
            BlockingQueue<PrescriptionRequest> prescriptionQueue
    ) {
        this.repository = repository;
        this.resultQueue = resultQueue;
        this.prescriptionQueue = prescriptionQueue;
    }

    @Override
    public void run() {

        try {
            while (true) {

                LabResult result = resultQueue.take();

                if (result.stop) {
                    break;
                }
                System.out.println("DOCTOR: Reviewing lab results for "
                        + result.patientID);

                Patient patient =
                        repository.getPatient(result.patientID);

                if (patient != null) {

                    patient.settestresults(result.result);
                    patient.setDiagnosis("Patient examined");
                    patient.setPrescription("Medicine A");

                    System.out.println("DOCTOR: Diagnosis recorded for "
                            + result.patientID);

                    System.out.println("DOCTOR: Sending prescription for "
                            + result.patientID);

                    prescriptionQueue.put(
                            new PrescriptionRequest(
                                    result.patientID,
                                    "Medicine A",
                                    patient.getRequestedDoses()
                            )

                    );


                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}