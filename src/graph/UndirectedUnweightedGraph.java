package graph;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

//import org.graalvm.compiler.core.phases.GraphChangeMonitoringPhase;
/**
 * This class implements general operations on a graph as specified by UndirectedGraphADT.
 * It implements a graph where data is contained in Vertex class instances.
 * Edges between verticies are unweighted and undirected.
 * A graph coloring algorithm determines the chromatic number. 
 * Colors are represented by integers. 
 * The maximum number of vertices and colors must be specified when the graph is instantiated.
 * You may implement the graph in the manner you choose. See instructions and course material for background.
 */
 
 public class UndirectedUnweightedGraph<T> implements UndirectedGraphADT<T> {
   // private class variables here.
   
   private int MAX_VERTICES;
   private int MAX_COLORS;
    // TODO: Declare class variables here.
    public ArrayList<ArrayList<Vertex<T>>> graph = new ArrayList<ArrayList<Vertex<T>>>();
   
   /**
    * Initialize all class variables and data structures. 
   */   
   public UndirectedUnweightedGraph (int maxVertices, int maxColors){
      MAX_VERTICES = maxVertices;
      MAX_COLORS = maxColors; 
     // TODO: Implement the rest of this method.
    
   }

   /**
    * Add a vertex containing this data to the graph.
    * Throws Exception if trying to add more than the max number of vertices.
   */
   public void addVertex(T data) throws Exception {
    // TODO: Implement this method.
    if (graph.size() == MAX_VERTICES)
      throw new Exception();

    Vertex<T> newVertex = new Vertex<T>(data);
    ArrayList<Vertex<T>> newArrayList = new ArrayList<Vertex<T>>();
    newArrayList.add(newVertex);
    graph.add(newArrayList);
   }
   
   /**
    * Return true if the graph contains a vertex with this data, false otherwise.
   */   
   public boolean hasVertex(T data){
    // TODO: Implement this method.
    for (int i = 0; i < graph.size(); i++) {
      if (graph.get(i).get(0).getData().equals(data))
        return true;
    }
    
      return false;
   } 

   /**
    * Add an edge between the vertices that contain these data.
    * Throws Exception if one or both vertices do not exist.
   */   
   public void addEdge(T data1, T data2) throws Exception{
    // TODO: Implement this method.
    int index1 = -1;
    int index2 = -1;
    boolean data1Exists = false;
    boolean data2Exists = false;
    
    for (int i = 0; i < graph.size(); i++) {
      if (graph.get(i).get(0).getData().equals(data1))
        index1 = i;
        data1Exists = true;

      if (graph.get(i).get(0).getData().equals(data2))
        index2 = i;
        data2Exists = true;
    }

    if (data1Exists && data2Exists) {
      graph.get(index1).add(graph.get(index2).get(0));
      graph.get(index2).add(graph.get(index1).get(0));
    } else {
      throw new Exception();
    }

   }

   /**
    * Get an ArrayList of the data contained in all vertices adjacent to the vertex that
    * contains the data passed in. Returns an array of zero length if no adjacencies exist in the graph.
    * Throws Exception if a vertex containing the data passed in does not exist.
   */   
   public ArrayList<T> getAdjacentData(T data) throws Exception{
    // TODO: Implement this method.
      ArrayList<T> arrayWithData = new ArrayList<T>();

      for (int i = 0; i < graph.size(); i++) {
        if (graph.get(i).get(0).getData().equals(data))
          for (int j = 1; j < graph.get(i).size(); j++) {
            arrayWithData.add(graph.get(i).get(j).getData());
          }
      }

      if (arrayWithData.isEmpty()) {
        throw new Exception();
      } else if ( arrayWithData.size() == 1) {
        arrayWithData.remove(0);
        return arrayWithData;
      }

      return arrayWithData;
   }
   
   /**
    * Returns the total number of vertices in the graph.
   */   
   public int getNumVertices(){
    // TODO: Implement this method.
      return graph.size();
   }

   /**
    * Returns the total number of edges in the graph.
   */   
   public int getNumEdges(){
    // TODO: Implement this method.
      int doubleTotalEdges = 0;

      for (int i=0; i < graph.size(); i++) {
        doubleTotalEdges += graph.get(i).size() - 1;
      }

      return doubleTotalEdges/2;
   }

   /**
    * Returns the minimum number of colors required for this graph as 
    * determined by a graph coloring algorithm.
    * Throws an Exception if more than the maximum number of colors are required
    * to color this graph.
   */   
   public int getChromaticNumber() throws Exception{
    // TODO: Implement this method.
      Queue<Vertex<T>> queue = new LinkedList<Vertex<T>>();
      Vertex<T> currentV;
      int numberColors = 0;
      queue.add(graph.get(0).get(0));
      
      while (!queue.isEmpty()) {
        currentV = queue.remove();
        currentV.setVisited(true);

        int curIndex = getIndexOfAdjacentVertices(currentV.getData());
        int highestAdjacentColor = getHighestColor(curIndex, currentV);

        if (highestAdjacentColor > numberColors) {
          numberColors = highestAdjacentColor;
        }
        if (numberColors > MAX_COLORS) {
          throw new Exception();
        }

        for (int i = 1; i < graph.get(curIndex).size(); i++) {
          if (!graph.get(curIndex).get(i).isVisited()) {
            graph.get(curIndex).get(i).setVisited(true);
            queue.add(graph.get(curIndex).get(i));
          }
        }

      }
      return numberColors;
   }

   private int getIndexOfAdjacentVertices(T data) {
    for (int i = 0; i < graph.size(); i++) {
      if (graph.get(i).get(0).getData().equals(data))
        return i;
    }
  
    return -1;
   }
   
   private int getHighestColor(int index, Vertex<T> vertex) {
     int lowestColor = graph.get(index).get(0).getColor();
     int highestColor =  lowestColor;
     ArrayList<Integer> colorsUsed = new ArrayList<Integer>();

     for (int i = 0; i < graph.get(index).size(); i++) {
      if (graph.get(index).get(i).getColor() == -1) {
        continue;
      } else {
        colorsUsed.add(graph.get(index).get(i).getColor());
      }      
    }

    for (int j = 0; j < colorsUsed.size(); j++) {
      if (colorsUsed.get(j) < lowestColor || lowestColor == -1) {
        lowestColor = colorsUsed.get(j);
      }
      
      if (colorsUsed.get(j) > highestColor) {
        highestColor = colorsUsed.get(j);
      }
    }

    if (lowestColor == -1 && colorsUsed.isEmpty()) {
      vertex.setColor(1);
      highestColor = 1;
    } else if (lowestColor != 1) {
      vertex.setColor(lowestColor - 1);
    } else {
      vertex.setColor(highestColor + 1);
      highestColor++;
    }

    return highestColor;
   }
}