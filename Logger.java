package proj1;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Logger {

    /**
     * Writes a string to the log file
     * @param s String to write to log
     */
    public static void log(String s) {
        try (FileWriter fileWriter = new FileWriter(new File(System.getProperty("user.dir") + "/log.txt"), true)) {
            fileWriter.write("[" + LocalDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS) + "]\t" + s + "\n");
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Failed to write logs.");
        }
    }   

    /**
     * Writes the results to the log file
     * @param algorithm Name of the algorithm used
     * @param chromaticNumber The chromatic number computed by the algorithm
     * @param time The time (in ms) it took to run the algorithm
     */
    public static void logResults(String algorithm, String inputfile, int chromaticNumber, Double time) {
        try (FileWriter fileWriter = new FileWriter(new File(System.getProperty("user.dir") + "/log.txt"), true)) {
            fileWriter.write("[" + LocalDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS) + "]\t" + algorithm + "\t" + inputfile + "\t" + chromaticNumber + "\t" + time + "\n");
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Failed to write logs.");
        }
    }   

    /**
     * Clears the log file
     */
    public static void clearLog() {
        try (FileWriter fileWriter = new FileWriter(new File(System.getProperty("user.dir") + "/log.txt"))) {
            fileWriter.write("");
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Failed to write logs.");
        }
    }   


}
