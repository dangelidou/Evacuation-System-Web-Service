package org.example.controller;

import org.example.model.Network;
import org.example.model.Node;
import org.example.service.Dijkstra;
import org.example.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dijkstra")
public class DijkstraController {

    private final NetworkService networkService;

    @Autowired
    public DijkstraController(NetworkService networkService) {
        this.networkService = networkService;
    }

    @PostMapping(value = "/default", produces = "application/xml")
    public ResponseEntity<String> getShortestPathFromDefaultNetwork() {
        try {
            // Use the service to load the Network object from the XML file
            Network network = networkService.loadNetworkFromXML("network.xml");

            // Keep the nodes that represent exits or shelters in a list
            List<Node> targetNodes = new ArrayList<>();
            for (Node node : network.getNodes()) {
                if (node.isExit() || node.isShelter()) {
                    targetNodes.add(node);
                }
            }

            // Run Dijkstra's algorithm
            Dijkstra dijkstra = new Dijkstra();
            Map<Integer, Double> shortestPaths = dijkstra.findShortestPaths(network, 1); // Starting node 1

            // Get distances for target nodes
            Map<Integer, Double> targetDistances = new HashMap<>();
            for (Node targetNode : targetNodes) {
                if (shortestPaths.containsKey(targetNode.getId())) {
                    targetDistances.put(targetNode.getId(), shortestPaths.get(targetNode.getId()));
                }
            }

            shortestPaths = targetDistances;
            // Generate XML response
            String result = generateXMLResult(shortestPaths);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("<error>" + e.getMessage() + "</error>");
        }
    }

    private String generateXMLResult(Map<Integer, Double> shortestPaths) {
        StringBuilder result = new StringBuilder();
        result.append("<shortestPaths>");
        for (Map.Entry<Integer, Double> entry : shortestPaths.entrySet()) {
            result.append("<path>");
            result.append("<node>").append(entry.getKey()).append("</node>");
            result.append("<distance>").append(entry.getValue()).append("</distance>");
            result.append("</path>");
        }
        result.append("</shortestPaths>");
        return result.toString();
    }
}