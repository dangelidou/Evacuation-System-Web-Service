package org.example.controller;

import com.github.javaparser.utils.Pair;
import org.example.model.Network;
import org.example.model.Node;
import org.example.service.Dijkstra;
import org.example.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.service.NetworkService.XMLValidationException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dijkstra")
public class DijkstraController {
    private static final Logger logger = LoggerFactory.getLogger(DijkstraController.class);
    private final NetworkService networkService;

    @Autowired
    public DijkstraController(NetworkService networkService) {
        this.networkService = networkService;
    }

    @PostMapping(value = "/upload", produces = "application/json")
    public ResponseEntity<Map<String, Map<String, Pair<List<String>, Float>>>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {

            // Load the Network object directly from the file input stream (no need for a temp file)
            Network network = networkService.loadNetworkFromXML(file.getInputStream());
            // Run Dijkstra's algorithm
            Dijkstra dijkstra = new Dijkstra();
            Map<String, Map<String,Pair<List<String>,Float>>> result = new HashMap<>();
            for (Node node : network.getNodes()) {
                Map<String, Pair<List<String>, Float>> shortestPaths = dijkstra.findShortestPaths(network, node.getId()); // Starting node
                if (shortestPaths == null || shortestPaths.isEmpty()) {
                    continue; // Skip nodes with no paths
                }
                result.put(node.getId(), shortestPaths);
            }

            return ResponseEntity.ok(result);
        } catch (XMLValidationException e) {
            logger.error("XML validation error: {}", e.getMessage(), e);
            // throw new RuntimeException(e);
            return ResponseEntity.status(400).body(
                Map.of("error", Map.of())
            );
        }
         catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                Map.of("error", Map.of())
            );
         }
    }

}