import java.io.*;
import java.util.*;
public class FlightService {
    FlightService() {

    }
    private static final String FLIGHTS_FILE = "flights.txt";
    private static final String FILE_PATH = new Files(FLIGHTS_FILE).readFilePath();
    private static final Random random = new Random();
    public static boolean addFlight(String name, String from, String to, int price) {
        List<Flight> flights = loadFlights();
        int id = generateRandomId();
        flights.add(new Flight(name, id, from, to, price));
        saveFlights(flights);
        System.out.println("✅ Flight added successfully");
        return true;
    }

    public static List<Flight> listFlights() {
        List<Flight> flights = loadFlights();

        if (flights.isEmpty()) {
            System.out.println("No flights available.");
            return new ArrayList<>();
        }

        System.out.println("\nAvailable Flights:");
        for (int i = 0; i < flights.size(); i++) {
            System.out.println((i + 1) + ". " + flights.get(i));
        }
        return flights;
    }

    public static Flight getFlight(int index) {
        List<Flight> flights = loadFlights();
        if (index >= 0 && index < flights.size()) {
            return flights.get(index);
        }
        return null;
    }

    private static List<Flight> loadFlights() {
        List<Flight> flights = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Flight flight = Flight.fromFileString(line);
                if (flight != null) {
                    flights.add(flight);
                }
            }
        } catch (IOException e) {}
        return flights;
    }

    private static void saveFlights(List<Flight> flights) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Flight flight : flights) {
                writer.write(flight.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save flights.");
        }
    }

    public boolean editFlight(int index, String newName, String newFrom, String newTo, int newPrice) {
        List<Flight> flights = loadFlights();
        if (index >= 0 && index < flights.size()) {
            Flight flight = flights.get(index);
            flight.setName(newName);
            flight.setPrice(newPrice);
            flight.setFrom(newFrom);
            flight.setTo(newTo);
            saveFlights(flights);
            return true;
        }
        return false;
    }

    public static int generateRandomId() {
        return 100000 + random.nextInt(900000); // Generates number between 100000 and 999999
    }
}
