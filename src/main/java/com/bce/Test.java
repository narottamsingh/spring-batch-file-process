package com.bce;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<Employee> emp = new ArrayList<>();
        emp.add(new Employee(UUID.randomUUID().toString(), "OK"));
        List<UUID> ids = emp.stream()
                            .map(e -> UUID.fromString(e.getID()))
                            .collect(Collectors.toList());
        System.out.println(ids);
        
        // Now 'ids' contains UUIDs corresponding to the IDs of Employee objects
    }
}

class Employee {
    String ID;
    String status;

    Employee(String ID, String status) {
        this.ID = ID;
        this.status = status;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
