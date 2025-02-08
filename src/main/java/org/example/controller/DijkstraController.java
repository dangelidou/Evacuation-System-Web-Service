package org.example.controller;

import org.example.model.Network;
import org.example.model.Node;
import org.example.service.Dijkstra;
import org.example.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dijkstra")
public class DijkstraController {

    private final NetworkService networkService;

    @Autowired
    public DijkstraController(NetworkService networkService) {
        this.networkService = networkService;
    }

    @PostMapping(value = "/default", produces = "application/json")
    public ResponseEntity<Map<String,Map<String,Float>>> getShortestPathFromDefaultNetwork() {
        try {

            // Use the service to load the Network object from the XML file
            Network network = networkService.loadNetworkFromXML("network.xml");
            if (network == null || network.getNodes() == null || network.getNodes().isEmpty()) {
                return ResponseEntity.status(404).body(
                        Map.of("error", Map.of())
                );

            }
            // Run Dijkstra's algorithm
            Dijkstra dijkstra = new Dijkstra();
            Map<String,Map<String,Float>> result = new HashMap<>();
            for (Node node : network.getNodes()) {
                Map<String, Float> shortestPaths = dijkstra.findShortestPaths(network, node.getId()); // Starting node
                if (shortestPaths == null || shortestPaths.isEmpty()) {
                    continue; // Skip nodes with no paths
                }
                result.put(node.getId(), shortestPaths);
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
                return ResponseEntity.status(500).body(
                        Map.of("error", Map.of())
                );
        }
    }

}