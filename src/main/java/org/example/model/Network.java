package org.example.model;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@Setter
@XmlRootElement(name = "network")
public class Network {
    private List<Node> nodes;
    private List<Edge> edges;

    public Network() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    @XmlElement(name = "node")
    public List<Node> getNodes() {
        return nodes;
    }

    @XmlElement(name = "edge")
    public List<Edge> getEdges() {
        return edges;
    }

    public Node getNodeById(String id) {
        for (Node node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }

    
}
