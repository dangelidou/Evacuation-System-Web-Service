package org.example.model;

import lombok.Setter;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@Setter
@XmlRootElement(name = "node")
public class Node {
    private String id;
    private String label;
    private boolean exit;
    private boolean shelter;
    private boolean compromised;
    private @XmlElement(name = "temperature") int temperature;
    private @XmlElement(name = "co") int co;
    private @XmlElement(name = "co2") int co2;
    private @XmlElement(name = "smoke") int smoke;
    private @XmlElement(name = "flood") boolean flood;
    private @XmlElement(name = "water") int water;
    private @XmlElement(name = "fire") boolean fire;
    private @XmlElement(name = "ventilation") int ventilation;
    private int capacity;
    private int people;

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

    @XmlElement(name = "people")
    public int getPeople() {
        return people;
    }

    @XmlElement(name = "capacity")
    public int getCapacity() {
        if (capacity == 0) {
            return 1000; // Default capacity to 1000 if not set
        } 
        
        return capacity;
    }

    /* Returns true if the node is compromised, the room is flooded or on fire or one of 
     * the sensors are in a critical state (3).
     * The sensors are: temperature, CO, CO2, smoke, water, ventilation.
     */
    @XmlElement(name = "compromised")
    public boolean isCompromised() {
        return compromised ||
                fire || flood ||
                temperature == 3 || co == 3 || co2 == 3 || smoke == 3 || ventilation == 3 || water == 3;
    }
}

