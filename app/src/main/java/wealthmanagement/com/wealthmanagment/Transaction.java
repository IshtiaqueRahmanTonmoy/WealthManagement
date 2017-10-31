package wealthmanagement.com.wealthmanagment;

/**
 * Created by TONMOYPC on 10/30/2017.
 */
public class Transaction {
    String category,date,price,description,payment_method;

    public Transaction(String category, String date, String price){
        this.category = category;
        this.date = date;
        this.price = price;
    }

    public Transaction(String category, String date, String price, String description){
        this.category = category;
        this.date = date;
        this.price = price;
        this.description = description;

    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public Transaction(String category, String date, String price, String description, String payment_method){
        this.category = category;
        this.date = date;
        this.price = price;
        this.description = description;
        this.payment_method = payment_method;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
