package model;

import javafx.util.StringConverter;

import java.util.List;

public class ProductStringConverter extends StringConverter<Product> {

    List<Product> data;

    public ProductStringConverter(List<Product> data) {
        this.data = data;
    }

    @Override
    public String toString(Product product) {
        return product.getName()+" ("+product.getCode()+")";
    }

    @Override
    public Product fromString(String string) {
        Product product = null;

        for(Product item: data){
            String data = item.getName()+" ("+item.getCode()+")";
            if(data.equals(string)) product = item;
        }

        return product;
    }
}
