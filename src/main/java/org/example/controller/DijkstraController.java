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
    public ResponseEntity<List<Map<String, Map<String, Pair<List<String>, Float>>>>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {

            // Load the Network object directly from the file input stream (no need for a temp file)
            Network network = networkService.loadNetworkFromXML(file.getInputStream());
            // Run Dijkstra's algorithm
            Dijkstra dijkstra = new Dijkstra();
            Map<String, Map<String,Pair<List<String>,Float>>> paths = new HashMap<>();
            boolean hasDisabilityInfo = networkService.hasDisabilityInfo(file.getInputStream());
            Map<String, Map<String,Pair<List<String>,Float>>> disabilityPaths = new HashMap<>();
            
            for (Node node : network.getNodes()) {
                Map<String, Pair<List<String>, Float>> shortestPaths = dijkstra.findShortestPaths(network, node.getId(), false);
                paths.put(node.getId(), shortestPaths);
                if (hasDisabilityInfo) {
                    Map<String, Pair<List<String>, Float>> shortestPathsDisability = dijkstra.findShortestPaths(network, node.getId(), true);
                    disabilityPaths.put(node.getId(), shortestPathsDisability);
                }
            }
            List<Map<String, Map<String, Pair<List<String>, Float>>>> result = List.of(paths, disabilityPaths);
            // Return the result as a JSON response
            return ResponseEntity.ok(result);
        } catch (XMLValidationException e) {
            logger.error("XML validation error: {}", e.getMessage(), e);
            // throw new RuntimeException(e);
            return ResponseEntity.status(400).body(
                List.of(Map.of("error", Map.of("message", new Pair<>(List.of(), 0.0f))))
            );
        }
         catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                List.of(Map.of("error", Map.of("message", new Pair<>(List.of(), 0.0f))))
            );
         }
    }

}