package com.example.loginsmartwatchsse;

public class Row {
    private String kit_id;
    private Integer task_status_id;

    public Row (String kit_id, Integer task_status_id) {
        this.kit_id = kit_id;
        this.task_status_id = task_status_id;
    }

    public String getKit_id() {
        return kit_id;
    }

    public void setKit_id(String kit_id) {
        this.kit_id = kit_id;
    }

    public Integer getTask_status_id() {
        return task_status_id;
    }

    public void setTask_status_id(Integer task_status_id) {
        this.task_status_id = task_status_id;
    }
}
