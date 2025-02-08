package org.example.model;

import lombok.Setter;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

// Παράδειγμα για Node
@Setter
@XmlRootElement(name = "node")
public class Node {
    private String id;
    private String label;
    private boolean exit;
    private boolean shelter;
    private boolean compromised;

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    @XmlElement(name = "label")
    public String getLabel() {
        return label;
    }

    @XmlElement(name = "exit")
    public boolean isExit() {
        return exit;
    }

    @XmlElement(name = "shelter")
    public boolean isShelter() {
        return shelter;
    }

    @XmlElement(name = "compromised")
    public boolean isCompromised() {
        return compromised;
    }
}

