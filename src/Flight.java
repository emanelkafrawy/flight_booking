public class Flight {
    private String name;
    private String from;
    private String to;
    private int id;
    private int price;


    public Flight(String name, int id, String from, String to, int price) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.id = id;
        this.price = price;
    }

    public String getName() { return name; }
    public int getId() { return id; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public int getPrice() { return price; }

    public String toFileString() {
        return name + "," + id + "," + from + "," + to + "," + price;
    }

    public static Flight fromFileString(String line) {
        String[] parts = line.split(",", 5);
        if (parts.length < 3) return null;
        return new Flight(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3], Integer.parseInt(parts[4]));
    }

    @Override
    public String toString() {
        return name + ",ID:" + id + ", (" + from + " → " + to + ")" + " Price: " + price;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
