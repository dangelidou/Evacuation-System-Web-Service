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
    private float speed;
    private boolean compromised;
    private @XmlElement(name = "flood") boolean flood;
    private float widths;
    private @XmlElement(name = "temperature") int temperature;
    private @XmlElement(name = "co") int co;
    private @XmlElement(name = "co2") int co2;
    private @XmlElement(name = "smoke") int smoke;
    private int crowd;
    private boolean disability;
    private float disabilitySpeed;


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

    @XmlElement(name = "speed")
    public float getSpeed() {
        return speed;
    }

    /* Returns true if the edge is compromised, the hallway is flooded or one of
     * the sensors are in a critical state (3).
     * The sensors are (measure): temperature, CO, CO2, smoke.
     */
    @XmlElement(name = "compromised")
    public boolean isCompromised() {
        return compromised || flood || temperature == 3 || co == 3 || co2 == 3 || smoke == 3;
    }

    @XmlElement(name = "widths")
    public float getWidths() {
        return widths;
    }
  
    @XmlElement(name = "crowd")
    public int getCrowd() {
        return crowd;
    }

    // Calculate time based on lengths and speed
    public float getTime() {
        return lengths / speed;
    }

    @XmlElement(name = "disability")
    public boolean isDisability() {
        return disability;
    }

    public boolean isAccessible() {
        return disability;
    }

    @XmlElement(name = "disabilitySpeed")
    public float getDisabilitySpeed() {
        if (disabilitySpeed == 0) {
            return 1.0f; // Default disability speed to 1 m/s if not set
        }
        return disabilitySpeed;
    }
}
