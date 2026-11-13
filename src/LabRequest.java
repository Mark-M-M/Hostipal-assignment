
    public class LabRequest {

    String patientID;
    String testType;
    boolean stop;

    public LabRequest(String patientID, String testType) {
        this.patientID = patientID;
        this.testType = testType;
        this.stop = false;
    }

    private LabRequest() {
        this.stop = true;
    }

    public static LabRequest stopRequest() {
        return new LabRequest();
    }
}

