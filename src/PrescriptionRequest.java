public class PrescriptionRequest {


    String patientID;
    String medicine;
    int doses;
    boolean stop;

    public PrescriptionRequest(
            String patientID, String medicine, int doses) {

        this.patientID = patientID;
        this.medicine = medicine;
        this.doses = doses;
        this.stop = false;
    }

    private PrescriptionRequest() {
        this.stop = true;
    }

    public static PrescriptionRequest stopRequest() {
        return new PrescriptionRequest();
    }
}

