package org.example.service;

import com.github.javaparser.utils.Pair;
import org.example.model.Network;
import org.example.model.Node;
import org.example.model.Edge;

import java.util.*;

public class Dijkstra {

    public Map<String, Pair<List<String>, Float>> findShortestPaths(Network network, String startId) {
        // Convert Network into an adjacency list
        Map<String, Map<String, Float>> adjacencyList = buildAdjacencyList(network);

        // Map to store minimum distances from the start node
        Map<String, Float> distances = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Map<String,String> predecessors = new HashMap<>();
        // PriorityQueue for maintaining the next node to visit
        PriorityQueue<Map.Entry<String, Float>> priorityQueue =
                new PriorityQueue<>(Map.Entry.comparingByValue());

        // Initialize all distances to infinity
        for (Node node : network.getNodes()) {
            distances.put(node.getId(), Float.MAX_VALUE);
        }
        distances.put(startId, 0.0F);
        priorityQueue.offer(new AbstractMap.SimpleEntry<>(startId, 0.0f));

        // Process nodes
        while (!priorityQueue.isEmpty()) {
            String currentNodeId = priorityQueue.poll().getKey();

            // Skip if the node has already been visited
            if (visited.contains(currentNodeId)) {
                continue;
            }
            visited.add(currentNodeId);

            // Process all neighbors of the current node
            Map<String, Float> neighbors = adjacencyList.getOrDefault(currentNodeId, Collections.emptyMap());
            for (Map.Entry<String, Float> neighbor : neighbors.entrySet()) {
                String neighborId = neighbor.getKey();
                float weight = neighbor.getValue();
                float newDist = Math.round((distances.get(currentNodeId) + weight)*100f)/100f;

                // Update minimum distance if needed
                if (newDist < distances.get(neighborId)) {
                    distances.put(neighborId, newDist);
                    predecessors.put(neighborId, currentNodeId);
                    priorityQueue.offer(new AbstractMap.SimpleEntry<>(neighborId, newDist));
                }
            }
        }

        // Keep the nodes that represent exits in a list
        List<Node> exitNodes = new ArrayList<>();
        for (Node node : network.getNodes()) {
            if (node.isExit()) {
                exitNodes.add(node);
            }
        }

        // Find distances to the exit nodes
        Map<String, Float> targetDistances = findTargetDistances(distances, exitNodes);

        // If there are no exits, look for the closest shelter
        if (targetDistances.isEmpty()) {
            // Keep the nodes that represent shelters in a list
            List<Node> shelterNodes = new ArrayList<>();
            for (Node node : network.getNodes()) {
                if (node.isShelter()) {
                    shelterNodes.add(node);
                }
            }
            targetDistances = findTargetDistances(distances, shelterNodes);
        }
        Map<String, Float> closestNodes = findClosest(targetDistances);
        return reconstructPathsAndDistances(predecessors, closestNodes, distances);
    }

    private Map<String, Map<String, Float>> buildAdjacencyList(Network network) {
        Map<String, Map<String, Float>> adjacencyList = new HashMap<>();

        // Collect IDs of compromised nodes
        Set<String> compromisedNodes = new HashSet<>();
        for (Node node : network.getNodes()) {
            if (node.isCompromised()) {
                compromisedNodes.add(node.getId());
            }
        }

        // Populate the adjacency list excluding compromised edges and nodes
        for (Edge edge : network.getEdges()) {
            if (edge.isCompromised() || compromisedNodes.contains(edge.getFrom()) || compromisedNodes.contains(edge.getTo())) {
                continue; // Skip this edge
            }
            System.out.println(edge.getFrom() + " -> " + edge.getTo() + "distance: " + edge.getLengths());
            adjacencyList
                    .computeIfAbsent(edge.getFrom(), k -> new HashMap<>())
                    .put(edge.getTo(), edge.getLengths());
            adjacencyList
                    .computeIfAbsent(edge.getTo(), k -> new HashMap<>())
                    .put(edge.getFrom(), edge.getLengths());
        }

        return adjacencyList;
    }


    private Map<String, Float> findTargetDistances(Map<String, Float> distances, List<Node> targetNodes) {

        // Filter distances only for target nodes
        Map<String, Float> targetDistances = new HashMap<>();
        for (Node targetNode : targetNodes) {
            if (distances.containsKey(targetNode.getId())) {
                targetDistances.put(targetNode.getId(), distances.get(targetNode.getId()));
            }
        }
        return targetDistances;
    }

    private Map<String, Float> findClosest(Map<String, Float> distances) {
        Map<String, Float> closest = new HashMap<>();
        float minDistance = Float.MAX_VALUE;
        for (Map.Entry<String, Float> entry : distances.entrySet()) {
            if (entry.getValue() < minDistance) {
                minDistance = entry.getValue();
                closest.clear();
                closest.put(entry.getKey(), entry.getValue());
            } else if (entry.getValue() == minDistance) {
                closest.put(entry.getKey(), entry.getValue());
            }
        }

        return closest;
    }

    /**
     * Reconstructs paths to target nodes and returns both paths and distances.
     */
    private Map<String, Pair<List<String>, Float>> reconstructPathsAndDistances(
            Map<String, String> predecessors, Map<String, Float> closestNodes, Map<String, Float> distances) {

        Map<String, Pair<List<String>, Float>> result = new HashMap<>();

        for (String targetNode : closestNodes.keySet()) {
            List<String> path = new ArrayList<>();
            String currentNode = targetNode;

            // Trace the path back to the start
            while (currentNode != null) {
                path.add(currentNode);
                currentNode = predecessors.get(currentNode);
            }

            Collections.reverse(path); // Reverse to get the correct order
            result.put(targetNode, new Pair<>(path, distances.get(targetNode))); // Store path and distance
        }

        return result;
    }
}