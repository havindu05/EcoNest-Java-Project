package com.econest.controller;

import com.econest.dao.ClientDAO;
import com.econest.model.Client;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ClientController {
    private ClientDAO clientDAO = new ClientDAO();

    public void loadClientsToTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Client> list = clientDAO.getAllClients();

        for (int i = 0; i < list.size(); i++) {
            Client client = list.get(i);
            model.addRow(new Object[]{
                    client.getId(), client.getName(), client.getClientType(), client.getPhone(), client.getEmail(), client.getAddress()
            });
        }
    }

    public boolean saveClient(String name, String type, String phone, String email, String address) {
        Client client = new Client(0, name, type, phone, email, address);
        return clientDAO.addClient(client);
    }

    public boolean removeClient(int id) {
        return clientDAO.deleteClient(id);
    }
}