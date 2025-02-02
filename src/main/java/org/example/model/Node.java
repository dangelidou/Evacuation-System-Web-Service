package org.example.model;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

// Παράδειγμα για Node
@Setter
@XmlRootElement(name = "node")
public class Node {
    private int id;
    private String label;
    private boolean exit;
    private boolean shelter;
    private boolean compromised;

    @XmlElement(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "label")
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    @XmlElement(name = "exit")
    public boolean isExit() {
        return exit;
    }
    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @XmlElement(name = "shelter")
    public boolean isShelter() {
        return shelter;
    }
    public void setShelter(boolean shelter) {
        this.shelter = shelter;
    }

    @XmlElement(name = "compromised")
    public boolean isCompromised() {
        return compromised;
    }
    public void setCompromised(boolean compromised) {
        this.compromised = compromised;
    }
}

