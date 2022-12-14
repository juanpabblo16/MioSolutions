package java.ui;

import java.dto.RouteDto;
import java.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    private static RouteDto routeDto;
    private Graph<Integer> graph;
    private int numOfStations;
    private Scanner sc;
    private File file;
    private static String FILE_ROUTE = "src/java/data/graph.csv";

    public Main() throws FileNotFoundException {
        this.file = new File(FILE_ROUTE);
        this.sc = new Scanner(file);
    }

    public void importData(){
        graph = new Graph<>(false);
        while(sc.hasNextLine()){
            String data = sc.nextLine();
            String[] splitData = data.split(";");
            if(splitData[0].equals("0")){
                System.out.println("Añado a: "+splitData[1]);
                graph.addVertice(Integer.valueOf(splitData[2]),splitData[1]);
            }else{
                System.out.println("Creo camino entre: "+splitData[1]+" y "+splitData[2]);
                graph.addEdge(Integer.valueOf(splitData[1]),Integer.valueOf(splitData[2]),1);
            }
        }
        sc.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Main main = new Main();
        
    }
    
    public static void DFS(Graph graph, Vertice v) {
		
		if((graph != null) && (v != null)) {
			Stack<Vertice> stack = new Stack<Vertice>();
			List<Vertice> list = new ArrayList<Vertice>();
			stack.push(v);
			list.add(v);
			while(!stack.empty()) {
				Vertice temp = stack.pop();
				System.out.print(temp + " "); //Printing the root
				for(Vertice i : graph.getAdjacentVertex(temp)) {
					if(!list.contains(i)) {
						list.add(i);
						stack.push(i);						
					}
				}
			}
			
		}
		
	}
}
