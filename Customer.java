
public class Customer {
    String name;
    String date;
    String order;

    public Customer(String name, String date, String order) {
        this.name = name;
        this.date = date;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
    public static void displayCustomer(Customer customer){
        System.out.println(customer.getName()+" "+customer.getDate()+" "+customer.getOrder());
    }
}
