package org.example.model;

import lombok.Setter;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@Setter
@XmlRootElement(name = "network")
public class Network {
    private List<Node> nodes;
    private List<Edge> edges;

    @XmlElement(name = "node")
    public List<Node> getNodes() {
        return nodes;
    }

    @XmlElement(name = "edge")
    public List<Edge> getEdges() {
        return edges;
    }

    public void removeNode(String id) {
        nodes.removeIf(node -> node.getId().equals(id));
    }

    public void removeEdge(int id) {
        edges.removeIf(edge -> edge.getId() == id);
    }
}
