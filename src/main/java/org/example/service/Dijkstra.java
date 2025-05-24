package org.example.service;

import com.github.javaparser.utils.Pair;
import org.example.model.Network;
import org.example.model.Node;
import org.example.model.Edge;
import java.lang.Math;

import java.util.*;

public class Dijkstra {

    public Map<String, Pair<List<String>, Float>> findShortestPaths(Network network, String startId, boolean disability) {
        // Convert Network into an adjacency list
        Map<String, Map<String, Edge>> adjacencyList = buildAdjacencyList(network, disability);

        // Map to store minimum weights from the start node
        Map<String, Float> weights = new HashMap<>();
        // Initialize all weights to infinity
        for (Node node : network.getNodes()) {
            weights.put(node.getId(), Float.MAX_VALUE);
        }
        weights.put(startId, 0.0F);
        Set<String> visited = new HashSet<>();
        Map<String,String> predecessors = new HashMap<>();
        
        // PriorityQueue for maintaining the next node to visit
        PriorityQueue<Map.Entry<String, Float>> priorityQueue =
                new PriorityQueue<>(Map.Entry.comparingByValue());

        
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
            Map<String, Edge> neighbors = adjacencyList.getOrDefault(currentNodeId, Collections.emptyMap());
            for (Map.Entry<String, Edge> neighbor : neighbors.entrySet()) {
                String neighborId = neighbor.getKey();
                Edge edge = neighbor.getValue();
                Node fromNode = network.getNodeById(currentNodeId);
                int crowd = edge.getCrowd() + fromNode.getPeople(); // total crowd in the hallway
                System.out.println("Crowd: " + crowd + " = edge: " + edge.getCrowd() + " + node: " + fromNode.getPeople() + " from node: " + currentNodeId + " to node: " + neighborId);
                
                // Calculate new speed based on crowd density
                float density = crowd / (edge.getLengths() * edge.getWidths()); // density (People/m^2)
                float speed = edge.getSpeed(); // speed (m/s)
                if (disability) {
                    speed = edge.getDisabilitySpeed(); // speed for disabled people (m/s)
                }
                double newSpeed = speed*(1-Math.exp(-1.913*(1/density - 1/5.4))); // Kladek formula
                if (newSpeed < 0.1f) {
                    newSpeed = 0.1;
                }
                // System.out.println("New speed: " + newSpeed + ", Density: " + density);
                float newTime = edge.getLengths()/((float) newSpeed);
                // System.out.println("New time: " + newTime + "= " + edge.getLengths() + "/" + newSpeed + " from node: " + currentNodeId + " to node: " + neighborId);
                float newWeight = Math.round((weights.get(currentNodeId) + newTime)*100f)/100f;

                // Update minimum time if needed
                if (newWeight < weights.get(neighborId)) {
                    weights.put(neighborId, newWeight);
                    predecessors.put(neighborId, currentNodeId);
                    priorityQueue.offer(new AbstractMap.SimpleEntry<>(neighborId, newWeight));
                }
            }
        }

        // Keep the nodes that represent exits in a list
        List<Node> exitNodes = new ArrayList<>();
        for (Node node : network.getNodes()) {
            if (node.isExit() && !node.isCompromised()) {
                exitNodes.add(node);
            }
        }

        // Find weights to the exit nodes
        Map<String, Float> targetWeights = findTargetWeights(weights, exitNodes);

        // If there are no exits, look for the closest shelter
        if (targetWeights.isEmpty() || targetWeights.values().stream().allMatch(weight -> weight == Float.MAX_VALUE)) {
            System.out.println("No exits found, looking for shelters...");
            // Keep the nodes that represent shelters in a list
            List<Node> shelterNodes = new ArrayList<>();
            for (Node node : network.getNodes()) {
                if (node.isShelter() && !node.isCompromised()) {
                    shelterNodes.add(node);
                }
            }
            targetWeights = findTargetWeights(weights, shelterNodes);
        }
        Map<String, Float> closestNodes = findClosest(targetWeights);
        return reconstructPathsAndWeights(predecessors, closestNodes, weights);
    }

    private Map<String, Map<String, Edge>> buildAdjacencyList(Network network, boolean disability) {
        Map<String, Map<String, Edge>> adjacencyList = new HashMap<>();

        // Collect IDs of compromised nodes
        Set<String> compromisedNodes = new HashSet<>();
        for (Node node : network.getNodes()) {
            if (node.isCompromised()) {
                // System.out.println("Compromised node: " + node.getId());
                compromisedNodes.add(node.getId());
            }
        }

        // Populate the adjacency list excluding compromised edges and nodes
        for (Edge edge : network.getEdges()) {
            System.out.println(edge.isAccessible());
            
            if (edge.isCompromised() || compromisedNodes.contains(edge.getFrom()) || compromisedNodes.contains(edge.getTo())) {
                // System.out.println("Compromised edge: " + edge.getFrom() + " to " + edge.getTo());
                continue; // Skip this edge
            }
            
            if (disability) {
                // Check if the edge is accessible for disabled people
                if (!edge.isAccessible()) {
                    System.out.println("Edge not accessible for disabled: " + edge.getFrom() + " to " + edge.getTo());
                    continue; // Skip this edge
                }
            }
            adjacencyList
                    .computeIfAbsent(edge.getFrom(), k -> new HashMap<>())
                    .put(edge.getTo(), edge);
            adjacencyList
                    .computeIfAbsent(edge.getTo(), k -> new HashMap<>())
                    .put(edge.getFrom(), edge);
        }

        return adjacencyList;
    }


    private Map<String, Float> findTargetWeights(Map<String, Float> weights, List<Node> targetNodes) {
        // Filter weights only for target nodes
        Map<String, Float> targetWeights = new HashMap<>();
        for (Node targetNode : targetNodes) {
            if (weights.containsKey(targetNode.getId())) {
                targetWeights.put(targetNode.getId(), weights.get(targetNode.getId()));
            }
        }
        return targetWeights;
    }

    private Map<String, Float> findClosest(Map<String, Float> weights) {
        Map<String, Float> closest = new HashMap<>();
        float minDistance = Float.MAX_VALUE;
        for (Map.Entry<String, Float> entry : weights.entrySet()) {
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

    // Reconstructs paths to target nodes and returns both paths and weights.
    private Map<String, Pair<List<String>, Float>> reconstructPathsAndWeights(
            Map<String, String> predecessors, Map<String, Float> closestNodes, Map<String, Float> weights) {

        Map<String, Pair<List<String>, Float>> result = new HashMap<>();

        for (String targetNode : closestNodes.keySet()) {
            LinkedList<String> path = new LinkedList<>();
            if (weights.get(targetNode) == Float.MAX_VALUE) {
                path.add("No path found");
                result.put("N/A", new Pair<>(path, Float.POSITIVE_INFINITY)); // Store path and time
                return result;
            }
            
            String currentNode = targetNode;
            // Trace the path back to the start
            while (currentNode != null) {
                path.addFirst(currentNode);
                currentNode = predecessors.get(currentNode);    
            }
            result.put(targetNode, new Pair<>(path, weights.get(targetNode))); // Store path and time
        }
        return result;
    }     
    
    
}