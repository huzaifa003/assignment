

import warehouse.WarehouseInterface;
import java.io.*;
import java.sql.*;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        WarehouseInterface warehouseInterface = new WarehouseInterface();
        warehouseInterface.setVisible(true);
    }
}
