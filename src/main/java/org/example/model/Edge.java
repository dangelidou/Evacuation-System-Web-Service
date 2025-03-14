package org.example.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.Setter;


@Setter
@XmlRootElement(name = "edge")
public class Edge {
    private int id;
    private String label;
    private String from;
    private String to;
    private String lengthsRaw;
    @Getter
    @XmlTransient
    private Float lengths;
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


    public String getLengthsRaw() {
        return lengthsRaw;
    }

    @XmlElement(name = "lengths")
    public void setLengthsRaw(String lengthsRaw) {
        this.lengthsRaw = lengthsRaw;
        try {
            this.lengths = Float.parseFloat(lengthsRaw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid length value: " + lengthsRaw);
        }
    }

    @XmlElement(name = "compromised")
    public boolean isCompromised() {
        return compromised;
    }
}
