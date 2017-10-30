package wealthmanagement.com.wealthmanagment;

/**
 * Created by TONMOYPC on 10/30/2017.
 */
public class Transaction {
    String category,date,price;

    Transaction(String category,String date,String price){
        this.category = category;
        this.date = date;
        this.price = price;
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
