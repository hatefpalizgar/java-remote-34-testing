package com.sda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class OrderAuditService {
    private final List<String> auditLogs = new ArrayList<>();
    private final OrderRepository repository;

    public OrderAuditService(OrderRepository repository) {
        this.repository = repository;
    }

    // records an audit entry with current timestamp
    public void recordAudit(String action, String orderId) {
        String timestamp = getFormattedTimeStamp();
        String entry = String.format("[%s] Order %s: %s", timestamp, orderId, action);
        auditLogs.add(entry);
    }

    // this method is used in tests to allow spying and stubbing
    protected String getFormattedTimeStamp() {
        return LocalDate.now().toString();
    }

    // Processes order updates with VERY complex business logic
    public void processOrderUpdate(List<Order> orders) {
        for (Order order : orders) {
            try {
                if (shouldProcessOrder(order)) {
                    repository.update(order);
                    recordAudit("UPDATED", order.getOrderId());
                }
            } catch (Exception e) {
                recordAudit("UPDATED_FAILED", order.getOrderId());
            }
        }
    }

    public boolean shouldProcessOrder(Order order) {
        return (order != null && order.getStatus() != OrderStatus.CANCELLED && order.getAmount() > 0);
    }

    public List<String> getAuditLogs() {
        return new ArrayList<>(auditLogs);
    }

    // Clears old audit entries based on complex criteria
    // Returns number of deleted entries
    public int clearOldEntries(LocalDateTime before) {
        int initialSize = auditLogs.size();
        auditLogs.removeIf(entry -> isEntryOlderThan(entry, before));
        return initialSize - auditLogs.size();
    }

    // Complex parsing logic
    public boolean isEntryOlderThan(String entry, LocalDateTime before) {
        try {
            String dateStr = entry.substring(1, entry.indexOf("]"));
            LocalDateTime entryTime = LocalDateTime.parse(dateStr);
            return entryTime.isBefore(before);
        } catch (Exception e) {
            return false;
        }
    }

}
