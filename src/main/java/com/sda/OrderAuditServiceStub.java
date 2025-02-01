package com.sda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderAuditServiceStub {
    private final List<String> auditLogs = new ArrayList<>();

    public void recordAudit(String action, String orderId) {
        String timestamp = getFormattedTimeStamp();
        String entry = String.format("[%s] Order %s: %s", timestamp, orderId, action);
        auditLogs.add(entry);
    }

    protected String getFormattedTimeStamp() {
        return LocalDate.now().toString();
    }

    public void processOrderUpdate(List<Order> orders) {
        for (Order order : orders) {
            recordAudit("UPDATED", order.getOrderId());
        }
    }

    public boolean shouldProcessOrder(Order order) {
        return (order != null && order.getStatus() != OrderStatus.CANCELLED && order.getAmount() > 0);
    }

    public List<String> getAuditLogs() {
        return new ArrayList<>(auditLogs);
    }
}
