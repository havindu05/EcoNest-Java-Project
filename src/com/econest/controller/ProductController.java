package com.econest.controller;

import com.econest.dao.ProductDAO;
import com.econest.model.Product;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductController {
    private ProductDAO productDAO = new ProductDAO();

    public void loadProductsToTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Product> list = productDAO.getAllProducts();

        for (int i = 0; i < list.size(); i++) {
            Product product = list.get(i);
            model.addRow(new Object[]{
                    product.getId(), product.getName(), product.getCategory(), product.getPrice(), product.getStockQty(), product.getReorderLevel()
            });
        }
    }

    public boolean saveProduct(String name, String category, double price, int qty, int reorderLevel) {
        Product product = new Product(0, name, category, price, qty, reorderLevel);
        return productDAO.addProduct(product);
    }

    public boolean removeProduct(int id) {
        return productDAO.deleteProduct(id);
    }
}