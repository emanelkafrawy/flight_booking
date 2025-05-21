public class Booking {
    private String username;
    private int flightID;
    private String passport;
    private int flightPrice;
    public Booking(String username, int flightID, int flightPrice, String passport) {
        this.username = username;
        this.flightID = flightID;
        this.passport = passport;
        this.flightPrice = flightPrice;
    }

    public String getUsername() { return username; }
    public int getFlightID() { return flightID; }
    public String getPassport() { return passport; }
    public int getFLightPrice() { return flightPrice; }


    public String toFileString() {
        return username + "," + flightID + "," + flightPrice + "," + passport;
    }

    public static Booking fromFileString(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length < 3) return null;
        return new Booking(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);
    }

    @Override
    public String toString() {
        return "(Flight ID:" + flightID + ", Passport: " + passport + ", price: " + flightPrice + ", username: " + username + ")";
    }
}
