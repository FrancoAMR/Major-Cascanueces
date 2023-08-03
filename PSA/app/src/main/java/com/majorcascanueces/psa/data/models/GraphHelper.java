package com.majorcascanueces.psa.data.models;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;


public class GraphHelper {

    public static SimpleGraph<GeoPoint, DefaultWeightedEdge> loadGraph(InputStream file) {
        try {
            JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(file));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray vertex = jsonObject.get("vertex").getAsJsonArray();
            SimpleGraph<GeoPoint, DefaultWeightedEdge> graph = new SimpleGraph<>(DefaultWeightedEdge.class);
            for (JsonElement geoPoint : vertex) {
                GeoPoint gp = new GeoPoint(
                    geoPoint.getAsJsonObject().get("label").getAsString(),
                    geoPoint.getAsJsonObject().get("latitud").getAsDouble(),
                    geoPoint.getAsJsonObject().get("longitud").getAsDouble()
                );
                graph.addVertex(gp);
            }
            return graph;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void dijkstra(SimpleGraph<GeoPoint,DefaultWeightedEdge> graph, GeoPoint source, GeoPoint destination) {
        ShortestPathAlgorithm.SingleSourcePaths<GeoPoint, DefaultWeightedEdge> paths =
                new DijkstraShortestPath<>(graph).getPaths(source);

        GraphPath<GeoPoint,DefaultWeightedEdge> shortestPath = paths.getPath(destination);

        System.out.println("Camino más corto desde " + source + " a " + destination + ":");
        for (GeoPoint vertex : shortestPath.getVertexList()) {
            System.out.print(vertex + " ");
        }
        System.out.println("\nDistancia total: " + shortestPath.getWeight());
    }

    /*
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
    */


}
