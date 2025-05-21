import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AirlineBookingGUI extends JFrame {
    private AuthService authService;
    private FlightService flightService;
    private BookingService bookingService;
    private User currentUser;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public AirlineBookingGUI() {
        authService = new AuthService();
        flightService = new FlightService();
        bookingService = new BookingService();

        setTitle("Airline Booking System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPanel(), "login");
        mainPanel.add(registerPanel(), "register");
        mainPanel.add(userMenuPanel(), "userMenu");
        mainPanel.add(adminMenuPanel(), "adminMenu");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");

        setVisible(true);
    }

    private JPanel loginPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setBackground(Color.GRAY);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            User user = authService.login(username, password);
            if (user != null) {
                currentUser = user;
                boolean isAdmin = user.isAdmin();
                if (isAdmin) {
                    cardLayout.show(mainPanel, "adminMenu");
                } else {
                    cardLayout.show(mainPanel, "userMenu");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        });

        JButton goToRegisterBtn = new JButton("Register");
        goToRegisterBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        goToRegisterBtn.setBackground(Color.LIGHT_GRAY);
        goToRegisterBtn.setOpaque(true);
        goToRegisterBtn.setBorderPainted(false);
        goToRegisterBtn.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        panel.add(loginBtn);
        panel.add(goToRegisterBtn);

        return panel;
    }

    private JPanel registerPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField isAdminField = new JTextField();
        JTextField cardBalanceField = new JTextField();



        panel.add(new JLabel("Choose Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Choose Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Are you an admin? (yes/no):"));
        panel.add(isAdminField);

        panel.add(new JLabel("Enter Your Card Balance: "));
        panel.add(cardBalanceField);

        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        registerBtn.setBackground(Color.CYAN);
        registerBtn.setOpaque(true);
        registerBtn.setBorderPainted(false);
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String isAdmin = isAdminField.getText();
            String cardBalance = cardBalanceField.getText();
            if(!(username.isEmpty() || password.isEmpty() || isAdmin.isEmpty() || cardBalance.isEmpty())) {
                if (authService.register(username, password, isAdmin, Integer.parseInt(cardBalance))) {
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    cardLayout.show(mainPanel, "login");
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid data");
            }
        });

        panel.add(registerBtn);

        return panel;
    }

    private JPanel userMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1));

        JButton viewFlightsBtn = new JButton("View Flights");
        viewFlightsBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewFlightsBtn.setBackground(Color.yellow);
        viewFlightsBtn.setOpaque(true);
        viewFlightsBtn.setBorderPainted(false);
        viewFlightsBtn.addActionListener(e -> {
            int count= 0;
            List<Flight> flights = flightService.listFlights();
            StringBuilder sb = new StringBuilder("Available Flights:\n");
            for (Flight flight : flights) {
                sb.append(++count + "-" + flight.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        JButton bookFlightBtn = new JButton("Book Flight");
        bookFlightBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        bookFlightBtn.setBackground(Color.YELLOW);
        bookFlightBtn.setOpaque(true);
        bookFlightBtn.setBorderPainted(false);
        bookFlightBtn.addActionListener(e -> {
            String flightId = JOptionPane.showInputDialog("Enter Flight ID:");
            String passport = JOptionPane.showInputDialog("Enter Passport:");
            boolean booked = bookingService.bookFlight(currentUser, Integer.parseInt(flightId) - 1, passport);
            currentUser = authService.getUserDetails(currentUser);
            JOptionPane.showMessageDialog(this, booked ? "Booking successful!" : "Booking failed!");
        });

        JButton cancelBookingBtn = new JButton("Cancel My Booking");
        cancelBookingBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelBookingBtn.setBackground(Color.YELLOW);
        cancelBookingBtn.setOpaque(true);
        cancelBookingBtn.setBorderPainted(false);
        cancelBookingBtn.addActionListener(e -> {
            System.out.println("current user " + currentUser.getCardBalance());
            String bookingId = JOptionPane.showInputDialog("Enter Booking ID:");
            boolean canceled = bookingService.cancelUserBooking(currentUser, Integer.parseInt(bookingId) - 1);
            currentUser = authService.getUserDetails(currentUser);
            JOptionPane.showMessageDialog(this, canceled ? "Cancelled successfully!" : "Cancel failed!");
        });

        JButton viewBookingBtn = new JButton("View My Booking");
        viewBookingBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewBookingBtn.setBackground(Color.YELLOW);
        viewBookingBtn.setOpaque(true);
        viewBookingBtn.setBorderPainted(false);
        viewBookingBtn.addActionListener(e -> {
            int count = 0;
            List<Booking> bookings = bookingService.viewUserBookings(currentUser);
            StringBuilder sb = new StringBuilder("Your Bookings :\n");
            for (Booking book : bookings) {
                sb.append(++count + "-" + book.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.setBackground(Color.red);
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        panel.add(viewFlightsBtn);
        panel.add(bookFlightBtn);
        panel.add(viewBookingBtn);
        panel.add(cancelBookingBtn);
        panel.add(logoutBtn);

        return panel;
    }

    private JPanel adminMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1));

        JButton viewBookingsBtn = new JButton("View All Bookings");
        viewBookingsBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewBookingsBtn.setBackground(Color.YELLOW);
        viewBookingsBtn.setOpaque(true);
        viewBookingsBtn.setBorderPainted(false);
        viewBookingsBtn.addActionListener(e -> {
            int count = 0;
            List<Booking> bookings = bookingService.viewAllBookings();
            StringBuilder sb = new StringBuilder("All Bookings:\n");
            for (Booking booking : bookings) {
                sb.append(++count + "-" + booking.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        JButton viewFlightsBtn = new JButton("View Flights");
        viewFlightsBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewFlightsBtn.setBackground(Color.YELLOW);
        viewFlightsBtn.setOpaque(true);
        viewFlightsBtn.setBorderPainted(false);
        viewFlightsBtn.addActionListener(e -> {
            int count = 0;
            List<Flight> flights = flightService.listFlights();
            System.out.println(flights);
            StringBuilder sb = new StringBuilder("Available Flights:\n");
            // StringBuilder sb = new StringBuilder("<html><body style='font-family:sans-serif;'>");
            // sb.append("<h2 style='color:blue;'>Available Flights:</h2>");
            for (Flight flight : flights) {
                sb.append(++count + "-" + flight.toString()).append("\n");
            }
            // sb.append("</body></html>");
            JOptionPane.showMessageDialog(this, sb.toString());
        });
        
        JButton addFlightBtn = new JButton("Add Flight");
        addFlightBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        addFlightBtn.setBackground(Color.YELLOW);
        addFlightBtn.setOpaque(true);
        addFlightBtn.setBorderPainted(false);
        addFlightBtn.addActionListener(e -> {
            String flightName = JOptionPane.showInputDialog("Enter flight name: ");
            String flightFrom = JOptionPane.showInputDialog("From: ");
            String flightTo = JOptionPane.showInputDialog("To: ");
            String flightPrice = JOptionPane.showInputDialog("Price: ");
            if(!(flightName.isEmpty() || flightFrom.isEmpty() || flightTo.isEmpty() || flightPrice.isEmpty())) {
                boolean added = flightService.addFlight(flightName, flightFrom, flightTo, Integer.parseInt(flightPrice));
                JOptionPane.showMessageDialog(this, added ? "Added successfully!" : "Added failed!");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid data");
            }
        });

        JButton editFlightBtn = new JButton("Edit Flight");
        editFlightBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        editFlightBtn.setBackground(Color.YELLOW);
        editFlightBtn.setOpaque(true);
        editFlightBtn.setBorderPainted(false);
        editFlightBtn.addActionListener(e -> {
            String flightIdStr = JOptionPane.showInputDialog("Enter selected flight number:");
            String newName = JOptionPane.showInputDialog("Enter new flight name:");
            String newFrom = JOptionPane.showInputDialog("Enter new From date:");
            String newTo = JOptionPane.showInputDialog("Enter new To Date:");
            String newPrice = JOptionPane.showInputDialog("Enter new Price:");
            
            try {
                int flightId = Integer.parseInt(flightIdStr) - 1;
                boolean success = flightService.editFlight(flightId, newName, newFrom, newTo, Integer.parseInt(newPrice));
                JOptionPane.showMessageDialog(this, success ? "Flight updated successfully!" : "Invalid Flight ID!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.setBackground(Color.red);
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        panel.add(viewBookingsBtn);
        panel.add(viewFlightsBtn);
        panel.add(addFlightBtn);
        panel.add(editFlightBtn);
        panel.add(logoutBtn);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AirlineBookingGUI::new);
    }
}
