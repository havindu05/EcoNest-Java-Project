package com.econest.controller;

import com.econest.dao.AgentDAO;
import com.econest.model.DeliveryAgent;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AgentController {
    private AgentDAO agentDAO = new AgentDAO();

    public void loadAgentsToTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<DeliveryAgent> list = agentDAO.getAllAgents();

        for (int i = 0; i < list.size(); i++) {
            DeliveryAgent agent = list.get(i);
            model.addRow(new Object[]{
                    agent.getId(), agent.getName(), agent.getPhone(), agent.getVehicleNo(), agent.getVehicleType(), agent.getStatus()
            });
        }
    }

    public boolean saveAgent(String name, String phone, String email, String vehicleNo, String vehicleType, String status) {
        DeliveryAgent agent = new DeliveryAgent(0, name, phone, email, vehicleNo, vehicleType, status);
        return agentDAO.addAgent(agent);
    }
}