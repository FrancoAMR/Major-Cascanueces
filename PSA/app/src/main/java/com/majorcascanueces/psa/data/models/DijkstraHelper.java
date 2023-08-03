package com.majorcascanueces.psa.data.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class DijkstraHelper<T> {

    public void dijkstra(Graph<T, DefaultWeightedEdge> graph, T source, T destination) {
        ShortestPathAlgorithm.SingleSourcePaths<T, DefaultWeightedEdge> paths =
                new DijkstraShortestPath<>(graph).getPaths(source);

        GraphPath<T, DefaultWeightedEdge> shortestPath = paths.getPath(destination);

        System.out.println("Camino más corto desde " + source + " a " + destination + ":");
        for (T vertex : shortestPath.getVertexList()) {
            System.out.print(vertex + " ");
        }
        System.out.println("\nDistancia total: " + shortestPath.getWeight());
    }

    public SimpleDirectedWeightedGraph<T, DefaultWeightedEdge> loadGraphFromJSONResource(int resourceID)
            throws IOException {
        InputStream inputStream = // Obtén la referencia al archivo JSON desde el recurso
                getClass().getResourceAsStream("/raw/graph.json");

        Gson gson = new Gson();
        Type graphType = new TypeToken<SimpleDirectedWeightedGraph<T, DefaultWeightedEdge>>() {}.getType();
        Graph<T, DefaultWeightedEdge> loadedGraph = gson.fromJson(new InputStreamReader(inputStream), graphType);

        SimpleDirectedWeightedGraph<T, DefaultWeightedEdge> graph =
                new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for (T vertex : loadedGraph.vertexSet()) {
            graph.addVertex(vertex);
        }

        for (DefaultWeightedEdge edge : loadedGraph.edgeSet()) {
            T sourceVertex = loadedGraph.getEdgeSource(edge);
            T targetVertex = loadedGraph.getEdgeTarget(edge);
            double weight = loadedGraph.getEdgeWeight(edge);

            graph.addVertex(sourceVertex);
            graph.addVertex(targetVertex);

            DefaultWeightedEdge newEdge = graph.addEdge(sourceVertex, targetVertex);
            graph.setEdgeWeight(newEdge, weight);
        }

        return graph;
    }
}
