package com.majorcascanueces.psa.data.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

public class GraphHelper {

    public static Graph<GeoPoint, DefaultWeightedEdge> loadGraph(InputStream file) {
        try {
            JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(file));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray vertex = jsonObject.get("vertex").getAsJsonArray();

            Graph<GeoPoint, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

            for (JsonElement geoPoint : vertex) {
                JsonObject jv = geoPoint.getAsJsonObject();
                GeoPoint gp = new GeoPoint(
                        jv.get("label").getAsString(),
                        jv.get("latitud").getAsDouble(),
                        jv.get("longitud").getAsDouble()
                );
                graph.addVertex(gp);
            }

            JsonArray edges = jsonObject.get("edge").getAsJsonArray();
            Set<GeoPoint> gp = graph.vertexSet();
            for (JsonElement edge : edges) {
                JsonObject je = edge.getAsJsonObject();
                DefaultWeightedEdge e = graph.addEdge(
                        geoPoint(gp, je.get("source").getAsString()),
                        geoPoint(gp, je.get("target").getAsString()));
                graph.setEdgeWeight(e,je.get("weight").getAsInt() * 1.0);
            }
            return graph;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GeoPoint geoPoint(Set<GeoPoint> set, String label) {
        for (GeoPoint gp : set) {
            if (gp.label.equals(label))
                return gp;
        }
        return null;
    }

    public static GraphPath<GeoPoint, DefaultWeightedEdge> dijkstra(Graph<GeoPoint,DefaultWeightedEdge> graph, GeoPoint source, GeoPoint destination) {
        ShortestPathAlgorithm.SingleSourcePaths<GeoPoint, DefaultWeightedEdge> paths =
                new DijkstraShortestPath<>(graph).getPaths(source);

        return paths.getPath(destination);
    }

    public static String[] getOptions(InputStream file) {
        try {
            JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(file));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray options = jsonObject.get("list_option").getAsJsonArray();
            ArrayList<String> optList = new ArrayList<>();
            String opt;
            for (JsonElement geoPoint : options) {
                opt = geoPoint.getAsString();
                if (opt.startsWith("p"))
                    continue;
                optList.add(opt);
            }
            return optList.toArray(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
