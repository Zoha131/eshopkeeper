package com.zoha131.eshopkeeper.converter;

import com.zoha131.eshopkeeper.model.Customer;
import javafx.util.StringConverter;

import java.util.List;

public class CustomerConverter extends StringConverter<Customer> {
    List<Customer> data;

    public CustomerConverter(List<Customer> data) {
        this.data = data;
    }

    public void setData(List<Customer> data) {
        this.data = data;
    }

    @Override
    public String toString(Customer item) {

        String finder;

        if(item.getStore() == null){
            finder = item.getName();
        }
        else {
            finder = item.getStore();
        }

        return  finder;
    }

    @Override
    public Customer fromString(String string) {
        String finder;
        for(Customer item: data){

            if(item.getStore() == null) finder = item.getName();
            else finder = item.getStore();

            if(finder.equals(string)) return item;
        }

        return null;
    }
}
