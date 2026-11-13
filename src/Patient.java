public class Patient {

    private String patientID;
    private String name;
    private String vitalsigns;
    private String testresults;
    private String diagnosis;
    private String prescription;
    private int requestedDoses;

    public Patient(String patientID, String name) {
        this.patientID = patientID;
        this.name = name;
    }

    public String getPatientId() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public synchronized void setvitalSigns(String vitalsigns) {
        this.vitalsigns = vitalsigns;
    }

    public synchronized String getvitalsigns() {
        return vitalsigns;
    }

    public synchronized void settestresults(String testresults) {
        this.testresults = testresults;
    }

    public synchronized String gettestresults() {
        return testresults;
    }

    public synchronized void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public synchronized String getDiagnosis() {
        return diagnosis;
    }

    public synchronized void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public synchronized String getPrescription() {
        return prescription;
    }
    public synchronized void setRequestedDoses(int requestedDoses) {
        this.requestedDoses = requestedDoses;
    }

    public synchronized int getRequestedDoses() {
        return requestedDoses;
    }
}

