import java.util.concurrent.BlockingQueue;

public class Pharmacist implements Runnable {

    private BlockingQueue<PrescriptionRequest> prescriptionQueue;
    private MedicineInventory inventory;

    public Pharmacist(
            BlockingQueue<PrescriptionRequest> prescriptionQueue,
            MedicineInventory inventory
    ) {
        this.prescriptionQueue = prescriptionQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        try {
            while (true) {

                PrescriptionRequest prescription =
                        prescriptionQueue.take();

                if (prescription.stop) {
                    break;
                }

                System.out.println("PHARMACIST: Preparing medicine for "
                        + prescription.patientID);

                boolean success = inventory.dispense(
                        prescription.patientID,
                        prescription.medicine,
                        prescription.doses
                );
                if (success) {
                    System.out.println("PHARMACIST: Medicine successfully dispensed to "
                            + prescription.patientID);
                } else {
                    System.out.println("PHARMACIST: Could not dispense medicine to "
                            + prescription.patientID);
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}