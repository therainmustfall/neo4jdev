package graph.db.app;

import java.util.Properties;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;

import graph.db.app.utils.PnRc;

public class NeoBasic {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("neo.auth"));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
       
        String userName = properties.getProperty("user");
        String password = properties.getProperty("password");

        try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic(userName, password));
        Session session = driver.session()) {

            // Create nodes and a relationship
            // String createQuery = "CREATE (a:Person {name: 'Alice'})-[:FRIENDS_WITH]->(b:Person {name: 'Bob'})";
            String query = PnRc.exportMerge("savedrecs.txt");
            createNodeRelation(session, query);
            // String matchQuery = "MATCH (a:Person)-[r:FRIENDS_WITH]->(b:Person) RETURN a.name, b.name";
            // queryNodeRelation(session, matchQuery);
            // Visualizing
            // String cypherQuery = "MATCH (a)-[r]->(b) RETURN a, r, b";
            String cypString   = "MATCH (n) RETURN n";
            graphVis(session, cypString);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    static void createNodeRelation(Session session, String query) {
        try {
            session.run(query);
            System.out.println("Node and relationship created successfully.");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    static void queryNodeRelation(Session session, String query) {
        // Query nodes and relationships
        Result result = session.run(query);
        // Process the results
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println(record.get("a.name").asString() + " is friends with " + record.get("b.name").asString());
        }
    }

    static void queryTransaction(Session session, String query) {
        // Start a transaction
    }

    static void graphVis(Session session, String queryString) {

        // Fetch results
        Result result = session.run(queryString);
        // Greate a graph instance
        Graph graph = new SingleGraph("Neo4j Graph");
        // Process results and add to graph
        while (result.hasNext()) {
            Record record = result.next();
            Node erNode = record.get("n").asNode();
            graph.addNode(erNode.elementId());

            if (record.containsKey("a") && record.containsKey("b")) {
                System.out.println(record.get("a"));
                Node nodeA = record.get("a").asNode();
                Node nodeB = record.get("b").asNode();
                Relationship relationship = record.get("r").asRelationship();
                
                String aId = nodeA.elementId();
                String bId = nodeB.elementId();
                if (graph.getNode(aId) == null)
                    graph.addNode(nodeA.elementId()).setAttribute("label", nodeA.get("name").asString());
                if (graph.getNode(bId) == null)
                graph.addNode(nodeB.elementId()).setAttribute("label", nodeB.get("name").asString());
                
                graph.addEdge(relationship.elementId(), nodeA.elementId(), nodeB.elementId()).setAttribute("label", relationship.type());     

            }
        }
        System.setProperty("org.graphstream.ui", "swing");
        graph.display();
    }
}
