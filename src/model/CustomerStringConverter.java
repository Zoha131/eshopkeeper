package model;

import javafx.util.StringConverter;

import java.util.List;

public class CustomerStringConverter extends StringConverter<Customer> {

    List<Customer> data;

    public CustomerStringConverter(List<Customer> data) {
        this.data = data;
    }

    @Override
    public String toString(Customer object) {
        return object.getName();
    }

    @Override
    public Customer fromString(String string) {
        Customer customer = null;

        for(Customer item: data){
            if(item.getName().equals(string)) customer = item;
        }

        return customer;
    }
}
