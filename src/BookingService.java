import java.io.*;
import java.util.*;

public class BookingService {
    private static final String BOOKINGS_FILE = "bookings.txt";
    private static final String FILE_PATH = new Files(BOOKINGS_FILE).readFilePath();
    BookingService() {

    }
    public static boolean bookFlight(User user, int flightIndex, String passport) {
        Flight flight = FlightService.getFlight(flightIndex);
        if (flight == null) {
            System.out.println(" Invalid flight selection.");
            return false;
        }

        Booking booking = new Booking(user.getUsername(), flight.getId(), flight.getPrice(), passport);
        System.out.println(user.getCardBalance());
        System.out.println(flight.getPrice());
        int newUserBalance = user.getCardBalance() - flight.getPrice();
        user.updateUserBalance(user, newUserBalance);
        List<Booking> bookings = loadBookings();
        bookings.add(booking);
        saveBookings(bookings);

        System.out.println("✅ Booking successful!");
        return true;
    }

    public static List<Booking> viewUserBookings(User user) {
        List<Booking> bookings = loadBookings();
        List<Booking> userBooking = new ArrayList<>();
        System.out.println("\nYour Bookings:");
        int count = 0;
        System.out.println(user.getUsername()+ user);
        for (Booking book : bookings) {
            if (book.getUsername().equals(user.getUsername())) {
                ++count;
                System.out.println(++count + ". User: " + book.getUsername() + ", " + book);
                userBooking.add(book);
            }
        }
        if (count == 0) {
            System.out.println("You have no bookings.");
        }
        System.out.println("\n you have: " + count + " booking flights");
        return userBooking;
    }

    public static List<Booking> viewAllBookings() {
        List<Booking> bookings = loadBookings();
        System.out.println("\nAll Bookings:");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return new ArrayList<>();
        }
        int count = 0;
        for (Booking b : bookings) {
            System.out.println(++count + ". User: " + b.getUsername() + ", " + b);
        }
        System.out.println("\n booking flights count is:" + count);
        return bookings;
    }

    public static boolean cancelUserBooking(User user, int cancelIndex) {
        return modifyBookings(user, true, cancelIndex);
    }

    public static boolean cancelAnyBooking(int cancelIndex) {
        return modifyBookings(null, false, cancelIndex);
    }

    private static boolean modifyBookings(User user, boolean onlyOwn, int cancelIndex) {
        List<Booking> bookings = loadBookings();
        List<Integer> indicesToShow = new ArrayList<>();
        int refundPrice = 0;
        System.out.println(bookings);
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            if (!onlyOwn || b.getUsername().equals(user.getUsername())) {
                refundPrice  = b.getFLightPrice();
                indicesToShow.add(i);
            }
        }

        System.out.println(refundPrice);
        if (indicesToShow.isEmpty()) {
            System.out.println(" No bookings to cancel.");
            return false;
        }
        if (cancelIndex >= 0 && cancelIndex < indicesToShow.size()) {
            bookings.remove((int)indicesToShow.get(cancelIndex));
            if (user != null) {
                System.out.println(user.getCardBalance());
                user.updateUserBalance(user, user.getCardBalance() + refundPrice);
            }
            saveBookings(bookings);
            System.out.println("✅ Booking canceled successfully.");
            return true;
        } else {
            System.out.println(" Invalid selection.");
            return false;
        }
    }

    private static List<Booking> loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Booking booking = Booking.fromFileString(line);
                if (booking != null) {
                    bookings.add(booking);
                }
            }
        } catch (IOException e) {

        }
        return bookings;
    }

    private static void saveBookings(List<Booking> bookings) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Booking b : bookings) {
                writer.write(b.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(" Failed to save bookings.");
        }
    }
}
