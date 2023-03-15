

import warehouse.WarehouseInterface;
import java.io.*;
import java.sql.*;


public class Main {
    static Date date = new Date(System.currentTimeMillis());
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        System.out.println(date);
        WarehouseInterface warehouseInterface = new WarehouseInterface();
        warehouseInterface.setVisible(true);
    }
}
