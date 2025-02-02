package org.example.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "edge")
public class Edge {
    private int id;
    private String label;
    private int from;
    private int to;
    private double lengths;
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

    @XmlElement(name = "from")
    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    @XmlElement(name = "to")
    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @XmlElement(name = "lengths")
    public double getLengths() {
        return lengths;
    }
    public void setLengths(double lengths) {
        this.lengths = lengths;
    }

    @XmlElement(name = "compromised")
    public boolean isCompromised() {
        return compromised;
    }
    public void setCompromised(boolean compromised) {
        this.compromised = compromised;
    }
}
