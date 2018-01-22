package com.zoha131.eshopkeeper.edit;

import com.zoha131.eshopkeeper.data_helper.DataHelper;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import com.zoha131.eshopkeeper.model.Model;
import com.zoha131.eshopkeeper.model.Product;

public class EditUtil {


    public static <T> void SelectAllCol(TableView<T> tableView) {
        TableView.TableViewSelectionModel<T> tsm = tableView.getSelectionModel();
        TableColumn<T, ?> id = tableView.getColumns().get(0);
        tsm.setCellSelectionEnabled(true);
        tsm.setSelectionMode(SelectionMode.MULTIPLE);

        //select the whole row when first time clicked the id column
        tableView.setOnMousePressed(event -> {
            if (tsm.getSelectedCells().size() == 1) {
                TablePosition<Product, Integer> pos = tsm.getSelectedCells().get(0);
                if (pos.getColumn() == 0) tsm.select(pos.getRow());
            }

        });

        //select the whole row when double clicked the id column
        id.setOnEditStart(event -> {
            if (tsm.getSelectedCells().size() == 1) {
                TablePosition<Product, Integer> pos = tsm.getSelectedCells().get(0);
                if (pos.getColumn() == 0) tsm.select(pos.getRow());
            }
        });
    }


    public static <T extends Model> void updateString(TableColumn.CellEditEvent<T, String> event, String table) {
        T data = event.getRowValue();
        boolean update = DataHelper.updateData(table, event.getTableColumn().getId(), event.getNewValue(), data.getId());
        System.out.println("Data edited: " + update);
        if (!update) {
            event.getTableView().getItems().set(event.getTablePosition().getRow(), data);
        }
    }

    public static <T extends Model> void updateDouble(TableColumn.CellEditEvent<T, Double> event, String table) {
        T data = event.getRowValue();
        boolean update = DataHelper.updateData(table, event.getTableColumn().getId(), event.getNewValue(), data.getId());
        System.out.println("Data edited: " + update);
        if (!update) {
            event.getTableView().getItems().set(event.getTablePosition().getRow(), data);
        }
    }

    public static <T extends Model> void updateInt(TableColumn.CellEditEvent<T, Integer> event, String table) {
        T data = event.getRowValue();
        boolean update = DataHelper.updateData(table, event.getTableColumn().getId(), event.getNewValue(), data.getId());
        System.out.println("Data edited: " + update);
        if (!update) {
            event.getTableView().getItems().set(event.getTablePosition().getRow(), data);
        }
    }

}
