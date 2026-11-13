import java.util.concurrent.locks.ReentrantLock;

    public class MedicineInventory {
        private int doses = 50;
        private final ReentrantLock lock = new ReentrantLock();

        public boolean dispense(String patientID, String medicine,
                                int requestedDoses) {

            lock.lock();

            try {
                if (doses >= requestedDoses) {

                    doses -= requestedDoses;

                    System.out.println(
                            medicine + " dispensed to " + patientID
                                    + ". Remaining doses: " + doses
                    );

                    return true;

                } else {

                    System.out.println(
                            "go to another hospital we broke 😭 " + patientID
                    );

                    return false;
                }

            } finally {
                lock.unlock();
            }
        }

        public int getDoses() {
            lock.lock();

            try {
                return doses;
            } finally {
                lock.unlock();
            }
        }
    }

