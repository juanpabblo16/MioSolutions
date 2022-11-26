package java.model;

import java.util.*;

public class Graph<K>{

    private boolean directed;
    private ArrayList<Vertice<K>> vertices;
    private ArrayList<Edge> edges;

    private static int [][]dis;
    private static int [][]next;

    public Graph(boolean directed){
        this.directed = directed;
        vertices = new ArrayList<>();
        edges = new ArrayList<>();

    }
    public void createMatrix(){
        dis = new int[vertices.size()][vertices.size()];
        next = new int[vertices.size()][vertices.size()];
    }
    public void initialize(){
        createMatrix();
        for(int i = 0; i < dis.length; i++){
            for(int j = 0; j < dis[0].length; j++){
                if(i== j){
                    dis[i][j] = 0;
                }else{
                    dis[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        for(Vertice vertex : vertices){
            for(int i = 0; i < vertex.getEdges().size(); i++){
                if(vertex.getEdges().get(i) instanceof Edge){
                    if((((Edge<?>) vertex.getEdges().get(i)).getWeight()) < (dis[(int)vertex.getKey()][(int) ( (Edge<Integer>)vertex.getEdges().get(i)).getEnd().getKey()])){
                        dis[(int)vertex.getKey()][(int) ((Edge<Integer>) vertex.getEdges().get(i)).getEnd().getKey()] = (((Edge<?>) vertex.getEdges().get(i)).getWeight());
                    }
                }
            }
        }

        for(int i = 0; i < vertices.size(); i++){
            for(int j = 0; j < vertices.size(); j++){
                if(dis[i][j] == Integer.MAX_VALUE){
                    //System.out.println("Dis: "+dis[i][j]);
                    next[i][j] = -1;
                    //System.out.println("Dis: "+next[i][j]);
                }else{
                    //System.out.println("Dis: "+dis[i][j]);
                    next[i][j] = j;
                    //System.out.println("Dis: "+next[i][j]);
                }
            }
        }

    }

    public Vector<Integer> constructPath(int u, int v) {
        if (next[u][v] == -1)
            return null;

        Vector<Integer> path = new Vector<Integer>();
        path.add(u);

        while (u != v) {
            u = next[u][v];
            path.add(u);
        }
        return path;
    }

    public String printPath(Vector<Integer> path) {
        String pathOut = "";
        int n = path.size();
        for(int i = 0; i < n - 1; i++) {
            pathOut+=(path.get(i)) + ",";
            System.out.print(path.get(i) + ",");
        }
            pathOut+=(path.get(n-1));
            System.out.print(path.get(n - 1) + "\n");
        return pathOut;

    }
    
    

    public void floydWarshall(int V) {
        for(int k = 0; k < V; k++) {
            for(int i = 0; i < V; i++) {
                for(int j = 0; j < V; j++) {
                    // We cannot travel through
                    // edge that doesn't exist
                    if (dis[i][k] == Integer.MAX_VALUE || dis[k][j] == Integer.MAX_VALUE) {
                        continue;
                    }
                    //System.out.println("Dis en i: "+i +" j "+j+" k "+k+" = "+dis[i][j] + " "+ dis[i][k]+ " "+dis[k][j] );
                    if (dis[i][j] > dis[i][k] + dis[k][j]) {
                        dis[i][j] = dis[i][k] + dis[k][j];
                        next[i][j] = next[i][k];
                        //System.out.println("Next 1: "+ i+" "+j+" "+next[i][j]);
                        //System.out.println("Next 2: "+ i+" "+k+" "+next[i][k]);

                    }
                }
            }
        }
    }


    public Graph<K> prim(K first){
        int totalVertex = vertices.size();
        Vertice<K> vOrigin = this.searchVertice(first);
        if(vOrigin != null){
            Graph<K> nGraph = new Graph<K>(this.directed);
            this.getVertices().stream().forEach((v) -> {
                nGraph.addVertice(v.getKey());
            });
            PriorityQueue<Edge<K>> queue = new PriorityQueue<>((Edge e1, Edge e2) -> Integer.compare(e1.getWeight(), e2.getWeight()));
            int cont = 0;
            while(cont < totalVertex){
                for(Iterator<Edge<K>> it = vOrigin.getEdges().iterator(); it.hasNext();){
                    Edge<K> edge = it.next();
                    if(!edge.getEnd().isVisited()){
                        queue.offer(edge);
                    }
                }
                Edge<K> edge = queue.poll();
                if(!edge.getEnd().isVisited()){
                    System.out.println("Entra");
                    edge.getEnd().setVisited(true);
                    nGraph.addEdge(edge.getSource().getKey(), edge.getEnd().getKey(), edge.getWeight());
                    cont++;
                    vOrigin = edge.getEnd();
                }
            }
            return nGraph;
        }
        return null;
    }

    public int printPrim(Graph<K> graph){
        int sum = 0;
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Edge> cEdges = new ArrayList<>(graph.getEdges());
        if(directed == false){
            for(int i = 0; i<cEdges.size(); i++){
                for(int j = 0; j<cEdges.size(); j++){
                    if((cEdges.get(i).getSource() == cEdges.get(j).getEnd()) && (cEdges.get(i).getEnd() == cEdges.get(j).getSource())){
                        cEdges.remove(j);
                    }
                }
            }
        }
        for(Edge e : cEdges){
            values.add(e.getWeight());
        }
        System.out.println("Tamaño"+values.size());
        values.remove(0);
        for(int a : values){
            System.out.println(a);
            sum += a;
        }
        System.out.println(sum);
        return (sum);
    }
    
    
	
    public void addVertice(K key){
        Vertice<K> v = new Vertice<>(key);
        vertices.add(v);
    }

    public void addVertice(K key, String value){
        Vertice<K> v = new Vertice<>(key, value);
        vertices.add(v);
    }

    public void addEdge(K sourceKey, K endKey, int weight){
        Vertice<K> source = null;
        Vertice<K> end = null;
        for(int i = 0; i<vertices.size(); i++){
            if(vertices.get(i).getKey() == sourceKey){
                source = vertices.get(i);
            }
        }
        for(int i = 0; i<vertices.size(); i++){
            if(vertices.get(i).getKey() == endKey){
                end = vertices.get(i);
            }
        }
        if(directed) {
            Edge<K> e = new Edge<>(source, end, weight);
            edges.add(e);
            source.getEdges().add(e);
        }else{
            Edge<K> e1 = new Edge<>(source, end, weight);
            Edge<K> e2 = new Edge<>(end, source, weight);
            edges.add(e1);
            edges.add(e2);
            source.getEdges().add(e1);
            end.getEdges().add(e2);
        }
    }

    public boolean existVertice(K key){
        boolean exist = false;
        for(int i = 0; i<vertices.size() && !exist; i++){
            if(vertices.get(i).getKey() == key){
                exist = true;
            }
        }
        return exist;
    }

    public Vertice<K> searchVertice(K key){
        boolean exist = false;
        Vertice<K> v = null;
        for(int i = 0; i<vertices.size() && !exist; i++){
            if(vertices.get(i).getKey() == key){
                exist = true;
                v = vertices.get(i);
            }
        }
        return v;
    }
    
    
    /*
	 * This method finds the neighboring nodes connected to a given vertex.
	 * This is a very important method to traverse a Graph used in BFS and DFS algorithms.
	 * 
	 */
	public List<Vertice> getAdjacentVertex(Vertice v) {
		List<Vertice> list = new LinkedList<Vertice>();
		if((v != null) && (edges != null)) {
			Iterator<Edge> iterator = edges.iterator();
			while(iterator.hasNext()) {
				Edge temp = iterator.next(); 
				if(temp.getSource().equals(v)) {
					list.add(temp.getEnd());
				}
			}
			
		} 
		
		return list;		
	}
    
    
    
   


    public void edit(K key ,K newValue){
        Vertice<K> toEdit = searchVertice(key);
        toEdit.setKey(newValue);
    }

    public void edit(K key ,int newValue, int oldValue){
        Vertice<K> toEdit = searchVertice(key);
        boolean out = false;
        for (int i = 0; i < toEdit.getEdges().size() && !out; i++) {
            if (toEdit.getEdges().get(i).getWeight() == oldValue){
                toEdit.getEdges().get(i).setWeight(newValue);
                out = true;
            }
        }
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public ArrayList<Vertice<K>> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertice<K>> vertices) {
        this.vertices = vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public static int[][] getDis() {
        return dis;
    }

    public static void setDis(int[][] dis) {
        Graph.dis = dis;
    }

    public static int[][] getNext() {
        return next;
    }

    public static void setNext(int[][] next) {
        Graph.next = next;
    }
}