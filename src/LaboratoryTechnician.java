import java.util.concurrent.BlockingQueue;


    public class LaboratoryTechnician implements Runnable {

        private BlockingQueue<LabRequest> requestQueue;
        private BlockingQueue<LabResult> resultQueue;

        public LaboratoryTechnician(
                BlockingQueue<LabRequest> requestQueue,
                BlockingQueue<LabResult> resultQueue
        ) {
            this.requestQueue = requestQueue;
            this.resultQueue = resultQueue;
        }

        @Override
        public void run() {

            try {
                while (true) {

                    LabRequest request = requestQueue.take();

                    if (request.stop) {
                        break;
                    }

                    System.out.println(
                            "Technician processing " + request.patientID
                    );

                    LabResult result = new LabResult(
                            request.patientID,
                            "Blood test normal"
                    );

                    System.out.println("LAB: Test completed for " + request.patientID);

                    resultQueue.put(result);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

