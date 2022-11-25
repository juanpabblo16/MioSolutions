package java.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Manager{
    //Clase responsable de crear las rutas según con 2 nodos dados

    private Graph<Integer> graph;
    private int numOfStations;
    private Scanner sc;
    private File file;
    private static String FILE_ROUTE = "src/main/resources/static/data/graph2.csv";

    public Manager(){
        this.graph = new Graph<>(false);
        this.file = new File(FILE_ROUTE);
        try {
            this.sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error creating graph");
        }
        while(sc.hasNextLine()){
            String data = sc.nextLine();
            String[] splitData = data.split(";");
            if(splitData[0].equals("0")){
                graph.addVertice(Integer.valueOf(splitData[2]),splitData[1]);
            }else{
                graph.addEdge(Integer.valueOf(splitData[1]),Integer.valueOf(splitData[2]),1);
            }
        }
        sc.close();
        this.numOfStations = graph.getVertices().size();
        getGraph().initialize();
        graph.floydWarshall(numOfStations-1);
    }
    public String bestRoute(int initialPoint, int finalPoint){
        return graph.printPath(graph.constructPath(initialPoint, finalPoint));
    }

    public Graph<Integer>getGraph() {
        return graph;
    }
}
