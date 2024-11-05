package graph.db.app;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class NeoGraph {
    public static void main(String[] args) {
        Graph graph = new SingleGraph("Hello world Graph.");
        graph.addNode("A" );
        graph.addNode("B" );
        graph.addNode("C" );
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

        System.setProperty("org.graphstream.ui", "swing");
        graph.display();
    }

}
