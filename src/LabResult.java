public class LabResult {


    String patientID;
    String result;
    boolean stop;

    public LabResult(String patientID, String result) {
        this.patientID = patientID;
        this.result = result;
        this.stop = false;
    }

    private LabResult() {
        this.stop = true;
    }

    public static LabResult stopResult() {
        return new LabResult();
    }
}

