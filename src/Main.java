import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Airline Booking System");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose:");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter username: ");
                    String username = input.nextLine().trim();

                    System.out.print("Enter password: ");
                    String password = input.nextLine().trim();
                    User user = AuthService.login(username, password);
                    if (user != null) {
                        if (user.isAdmin()) {
                            handleAdmin();
                        } else {
                            handleUser(user);
                        }
                    }
                    break;
                case "2":
                    System.out.print("Enter username: ");
                    username = input.nextLine().trim();

                    System.out.print("Enter password: ");
                    password = input.nextLine().trim();

                    System.out.print("Are you an admin? (yes/no): ");
                    String isAdmin = input.nextLine();

                    System.out.print("Enter your card balance: ");
                    String cardBalance = input.nextLine();
                    AuthService.register(username, password, isAdmin, Integer.parseInt(cardBalance));
                    break;
                case "3":
                    System.out.println("GoodBye, see you again");
                    return;
                default:
                    System.out.println("❌ Invalid choice..");
            }
        }
    }

    private static void handleAdmin() {
        while (true) {
            System.out.println("\n Admin Dashboard:");
            System.out.println("1. Add Flight");
            System.out.println("2. View All Bookings");
            System.out.println("3. Cancel Any Booking");
            System.out.println("4. View Flights");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    // System.out.print("Enter flight name: ");
                    // String name = input.nextLine().trim();

                    // System.out.print("From: ");
                    // String from = input.nextLine().trim();

                    // System.out.print("To: ");
                    // String to = input.nextLine().trim();

                    FlightService.addFlight("name", "1:00", "2:00", 60);
                    break;
                case "2":
                    BookingService.viewAllBookings();
                    break;
                case "3":
                    System.out.print("enter the flight ID you need to cancel: ");
                    int flightIndex = Integer.parseInt(input.nextLine()) - 1;
                    BookingService.cancelAnyBooking(flightIndex);
                    break;
                case "4":
                    FlightService.listFlights();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }

    private static void handleUser(User user) {
        while (true) {
            System.out.println("\n User Dashboard:");
            System.out.println("1. View Flights");
            System.out.println("2. Book a Flight");
            System.out.println("3. View My Bookings");
            System.out.println("4. Cancel My Booking");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    FlightService.listFlights();
                    break;
                case "2":
                    FlightService.listFlights();
                    System.out.print("Select flight number to book: ");
                    int flightIndex = Integer.parseInt(input.nextLine()) - 1;
                    System.out.print("Enter passport number: ");
                    String passport = input.nextLine().trim();

                    BookingService.bookFlight(user, flightIndex, passport);
                    break;
                case "3":
                    BookingService.viewUserBookings(user);
                    break;
                case "4":
                    System.out.print("Enter flight ID you need to cancel: ");
                    int cancelIndex = Integer.parseInt(input.nextLine()) - 1;
                    BookingService.cancelUserBooking(user, cancelIndex);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
}
