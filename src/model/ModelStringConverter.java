package model;

import javafx.util.StringConverter;

import java.util.List;

public class ModelStringConverter<T extends Model> extends StringConverter<T> {

    List<T> data;

    public ModelStringConverter(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString(T object) {
        return object.getName();
    }

    @Override
    public T fromString(String string) {

        for(T item: data){
            if(item.getName().equals(string)) {
                return item;
            }
        }
        return null;
    }
}
