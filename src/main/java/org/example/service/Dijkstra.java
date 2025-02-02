package org.example.service;

import org.example.model.Network;
import org.example.model.Node;
import org.example.model.Edge;

import java.util.*;

public class Dijkstra {

    public Map<Integer, Double> findShortestPaths(Network network, int startId) {
        // Convert Network into an adjacency list
        Map<Integer, Map<Integer, Double>> adjacencyList = buildAdjacencyList(network);

        // Map to store minimum distances from the start node
        Map<Integer, Double> distances = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        // PriorityQueue for maintaining the next node to visit
        PriorityQueue<Map.Entry<Integer, Double>> priorityQueue =
                new PriorityQueue<>(Map.Entry.comparingByValue());

        // Initialize all distances to infinity
        for (Node node : network.getNodes()) {
            distances.put(node.getId(), Double.MAX_VALUE);
        }
        distances.put(startId, 0.0);
        priorityQueue.offer(new AbstractMap.SimpleEntry<>(startId, 0.0));

        // Process nodes
        while (!priorityQueue.isEmpty()) {
            int currentNodeId = priorityQueue.poll().getKey();

            // Skip if the node has already been visited
            if (visited.contains(currentNodeId)) {
                continue;
            }
            visited.add(currentNodeId);

            // Process all neighbors of the current node
            Map<Integer, Double> neighbors = adjacencyList.getOrDefault(currentNodeId, Collections.emptyMap());
            for (Map.Entry<Integer, Double> neighbor : neighbors.entrySet()) {
                int neighborId = neighbor.getKey();
                double weight = neighbor.getValue();
                double newDist = distances.get(currentNodeId) + weight;

                // Update minimum distance if needed
                if (newDist < distances.get(neighborId)) {
                    distances.put(neighborId, newDist);
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
        Map<Integer, Double> targetDistances = findTargetDistances(distances, exitNodes);

        // If there are no exits, look for the closest shelter
        if (targetDistances.isEmpty()) {
            // Keep the nodes that represent shelters in a list
            List<Node> shelterNodes = new ArrayList<>();
            for (Node node : network.getNodes()) {
                if (node.isExit()) {
                    shelterNodes.add(node);
                }
            }
            targetDistances = findTargetDistances(distances, shelterNodes);
        }

        return findClosest(targetDistances);
    }

    private Map<Integer, Map<Integer, Double>> buildAdjacencyList(Network network) {
        Map<Integer, Map<Integer, Double>> adjacencyList = new HashMap<>();

        for (Node node : network.getNodes()) {
            if (node.isCompromised()) network.removeNode(node.getId());
        }
        // Populate the adjacency list
        for (Edge edge : network.getEdges()) {
            if (edge.isCompromised()) {
                network.removeEdge(edge.getId());
                continue;
            }

            adjacencyList
                    .computeIfAbsent(edge.getFrom(), k -> new HashMap<>())
                    .put(edge.getTo(), edge.getLengths());
            adjacencyList.computeIfAbsent(edge.getTo(), k -> new HashMap<>()).put(edge.getFrom(), edge.getLengths());
        }

        return adjacencyList;
    }

    private Map<Integer, Double> findTargetDistances(Map<Integer, Double> distances, List<Node> targetNodes) {

        // Filter distances only for target nodes
        Map<Integer, Double> targetDistances = new HashMap<>();
        for (Node targetNode : targetNodes) {
            if (distances.containsKey(targetNode.getId())) {
                targetDistances.put(targetNode.getId(), distances.get(targetNode.getId()));
            }
        }
        return targetDistances;
    }

    private Map<Integer, Double> findClosest(Map<Integer, Double> distances) {
        Map<Integer, Double> closest = new HashMap<>();
        double minDistance = Double.MAX_VALUE;
        for (Map.Entry<Integer, Double> entry : distances.entrySet()) {
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
}