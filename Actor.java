import java.util.*;

public class Actor implements Comparable {

    String name;               // Actor name.
    int numMovies;             // How many movies they've been in.

    double x, y;               // For drawing with DrawTool.
    String color = "blue";     // Ditto.

    // This is what we'll need to compute.
    ArrayList<Actor> actedWith = new ArrayList<>();


    // For sorting (as done earlier) ...

    public int compareTo (Object obj)
    {
	Actor a = (Actor) obj;
	if (numMovies > a.numMovies) {
	    return -1;
	}
	else if (numMovies < a.numMovies) {
	    return 1;
	}
	else {
	    return 0;
	}
    }
}
