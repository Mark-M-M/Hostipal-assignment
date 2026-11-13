import java.util.HashMap;
import java.util.Map;


    public class Patientrecords {

        private Map<String, Patient> patients = new HashMap<>();

        public synchronized void addPatient(Patient patient) {
            patients.put(patient.getPatientId(), patient);
        }

        public synchronized Patient getPatient(String patientID) {
            return patients.get(patientID);
        }
    }

