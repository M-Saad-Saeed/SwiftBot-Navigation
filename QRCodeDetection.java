
import java.awt.image.BufferedImage;



public class QRCodeDetection extends SwiftBotNavigation {
    private static final int attempts = 3; // max number of tries
    private static final long duration = 5000; // time duration for one try

    public void detectQRCode() {
        boolean qrFound = false;

        for (int attempt = 1; attempt <= attempts && !qrFound; attempt++) {
            System.out.println("Attempt " + attempt + " of " + attempts + ": scanning for QR code.");
            long deadline = System.currentTimeMillis() + duration;

            // Scan until the deadline is reached.
            while (System.currentTimeMillis() < deadline) {
                try {
                    BufferedImage img = swiftBot.getQRImage();
                    decodedMessage = swiftBot.decodeQRImage(img);

                    if (decodedMessage != null && !decodedMessage.isEmpty()) {
                        System.out.println("SUCCESS: QR code found on attempt " + attempt);
                        System.out.println("Decoded message: " + decodedMessage);
                        qrFound = true;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("ERROR: Exception occurred during QR scan on attempt " + attempt);
                    e.printStackTrace();
                    break; // Exit inner loop on error
                }
            }

            if (!qrFound) {
                System.out.println("No QR code found on attempt " + attempt);
            }
        }

        if (!qrFound) {
            System.out.println("No QR code found after " + attempts + " attempts.");
            System.exit(5);
        }
    }
}
