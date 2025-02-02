package org.example.model;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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

    public void removeNode(int id) {
        nodes.removeIf(node -> node.getId() == id);
    }

    public void removeEdge(int id) {
        edges.removeIf(edge -> edge.getId() == id);
    }
}
