import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.layout.mxFastOrganicLayout;
import javax.swing.*;

class CustomGraph {
    private final HashMap<String, HashSet<String>> adjList;

    public CustomGraph() {
        adjList = new HashMap<>();
    }

    public void addEdge(String src, String dest) {
        adjList.computeIfAbsent(src, k -> new HashSet<>()).add(dest);
        adjList.computeIfAbsent(dest, k -> new HashSet<>()).add(src);
    }

    public void removeEdge(String src, String dest) {
        adjList.get(src).remove(dest);
        adjList.get(dest).remove(src);
    }

    public HashSet<HashSet<String>> connectedComponents() {
        HashSet<HashSet<String>> components = new HashSet<>();
        HashSet<String> visited = new HashSet<>();

        for (String node : adjList.keySet()) {
            if (!visited.contains(node)) {
                HashSet<String> component = new HashSet<>();
                dfs(node, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private void dfs(String node, HashSet<String> visited, HashSet<String> component) {
        visited.add(node);
        component.add(node);

        for (String neighbor : adjList.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited, component);
            }
        }
    }
}

public class Task25 {
    private static void visualizeGraph(Graph<String, DefaultEdge> g){
        // Visualization
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(g);
        mxGraphComponent component = new mxGraphComponent(graphAdapter);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);

        // Apply a layout
        mxFastOrganicLayout layout = new mxFastOrganicLayout(graphAdapter);
        layout.setForceConstant(500); // Decrease for less spread
        layout.setMinDistanceLimit(1000); // Increase distance limit for more space between nodes
        layout.setInitialTemp(500);
        layout.setMaxIterations(2000); // Increase max iterations for potentially better results
        layout.execute(graphAdapter.getDefaultParent());

        // Set the style of the edges to not show arrowheads (non-directional)
        mxStylesheet stylesheet = graphAdapter.getStylesheet();
        Hashtable<String, Object> style = new Hashtable<>();
        style.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        style.put(mxConstants.STYLE_STROKECOLOR, "black");
        style.put(mxConstants.STYLE_FONTCOLOR, "black");
        stylesheet.setDefaultEdgeStyle(style);
        graphAdapter.setStylesheet(stylesheet);

        // Create and set up the window
        JFrame frame = new JFrame("Graph Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(component, BorderLayout.CENTER);

        // Display the window
        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        File input = new File("2023/input.txt");
        String line;
        Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class); // for visualization
        CustomGraph cg = new CustomGraph();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while((line = reader.readLine()) != null) {
                String[] graphStr = line.split(":");
                String[] destVertices = graphStr[1].trim().split(" ");
                for (String v : destVertices) {
                    g.addVertex(graphStr[0]);
                    if (!g.containsVertex(v))
                        g.addVertex(v);
                    g.addEdge(graphStr[0], v);
                    cg.addEdge(graphStr[0], v);
                }
            }
            SwingUtilities.invokeLater(() -> visualizeGraph(g));
            // edges to removed are obtained after visual inspection of the graph
            cg.removeEdge("pgt", "lnr");
            cg.removeEdge("zkt", "jhq");
            cg.removeEdge("tjz", "vph");
            HashSet<HashSet<String>> components = cg.connectedComponents();
            int product = 0;
            if (components.size() == 2) {
                Iterator<HashSet<String>> it = components.iterator();
                product = it.next().size() * it.next().size();
            }
            System.out.println(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
