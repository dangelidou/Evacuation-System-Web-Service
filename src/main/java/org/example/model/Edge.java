package org.example.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;


@Setter
@XmlRootElement(name = "edge")
public class Edge {
    private int id;
    private String label;
    private String from;
    private String to;
    private float lengths;
    private boolean compromised;

    @XmlElement(name = "id")
    public int getId() {
        return id;
    }

    @XmlElement(name = "label")
    public String getLabel() {
        return label;
    }

    @XmlElement(name = "from")
    public String getFrom() {
        return from;
    }

    @XmlElement(name = "to")
    public String getTo() {
        return to;
    }

    @XmlElement(name = "lengths")
    public float getLengths() {
        return lengths;
    }

    @XmlElement(name = "compromised")
    public boolean isCompromised() {
        return compromised;
    }
}
