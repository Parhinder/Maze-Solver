package graph;
import graph.WeightedGraph;
import maze.Juncture;
import maze.Maze;

/** 
 * <P>The MazeGraph is an extension of WeightedGraph.  
 * The constructor converts a Maze into a graph.</P>
 */
public class MazeGraph extends WeightedGraph<Juncture> {

	
	/** 
	 * <P>Construct the MazeGraph using the "maze" contained
	 * in the parameter to specify the vertices (Junctures)
	 * and weighted edges.</P>
	 * 
	 * <P>The Maze is a rectangular grid of "junctures", each
	 * defined by its X and Y coordinates, using the usual
	 * convention of (0, 0) being the upper left corner.</P>
	 * 
	 * <P>Each juncture in the maze should be added as a
	 * vertex to this graph.</P>
	 * 
	 * <P>For every pair of adjacent junctures (A and B) which
	 * are not blocked by a wall, two edges should be added:  
	 * One from A to B, and another from B to A.  The weight
	 * to be used for these edges is provided by the Maze. 
	 * (The Maze methods getMazeWidth and getMazeHeight can
	 * be used to determine the number of Junctures in the
	 * maze. The Maze methods called "isWallAbove", "isWallToRight",
	 * etc. can be used to detect whether or not there
	 * is a wall between any two adjacent junctures.  The 
	 * Maze methods called "getWeightAbove", "getWeightToRight",
	 * etc. should be used to obtain the weights.)</P>
	 * 
	 * @param maze to be used as the source of information for
	 * adding vertices and edges to this MazeGraph.
	 */
	public MazeGraph(Maze maze) {
		//call super constructor
		super();
		// Create a two-dimensional array of Juncture objects to hold the vertices
        Juncture[][] junctureArray = new Juncture[maze.getMazeWidth()][maze.getMazeHeight()];
        // Loop through the width and height and add the vertex from the juncture of the x and y coordinates
        for(int x = 0; x < maze.getMazeWidth(); x++){
            for(int y = 0; y < maze.getMazeHeight(); y++){
                Juncture j = new Juncture(x, y);
                junctureArray[x][y] = j;
                this.addVertex(j);
            }
        }

		//loop through maze to add edges
        for(int x = 0; x < maze.getMazeWidth(); x++){
            for(int y = 0; y < maze.getMazeHeight(); y++){
                //create juncture object with x and y coordinates
            	Juncture currentJuncture = junctureArray[x][y];
            	//check if there is no wall to the left of current juncture
                if(x > 0 && !maze.isWallToLeft(currentJuncture)){
                    //add weighted edge from current to left
                	Juncture leftJuncture = junctureArray[x - 1][y];
                    this.addEdge(currentJuncture, leftJuncture, maze.getWeightToLeft(currentJuncture));
                }
                //check if there is no wall above
                if(y > 0 && !maze.isWallAbove(currentJuncture)){
                    //add weighted edge from current to top
                	Juncture topJuncture = junctureArray[x][y - 1];
                    this.addEdge(currentJuncture, topJuncture, maze.getWeightAbove(currentJuncture));
                }
                //check if there is a wall to right
                if(x < maze.getMazeWidth() - 1 && !maze.isWallToRight(currentJuncture)){
                    //add weighted edge from current to right
                	Juncture rightJuncture = junctureArray[x + 1][y];
                    this.addEdge(currentJuncture, rightJuncture, maze.getWeightToRight(currentJuncture));
                }
                //check if there is a wall below
                if(y < maze.getMazeHeight() - 1 && !maze.isWallBelow(currentJuncture)){
                    //add weighted edge from current to below
                	Juncture bottomJuncture = junctureArray[x][y + 1];
                    this.addEdge(currentJuncture, bottomJuncture, maze.getWeightBelow(currentJuncture));
                }
            }
        }
	}
}
