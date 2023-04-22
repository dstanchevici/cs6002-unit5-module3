import java.util.*;

public class ActorConnectivityMaker {

    static ArrayList<MovieData> movies = new ArrayList<>();

    public static ArrayList<Actor> makeActorConnectivityData (int N)
    {
	// Read in the data.
	readMovieData ();

	// 1. Use the other class that already solves the actor-list:
	ArrayList<Actor> actorList = ActorListMaker.getSortedActorList ();

	// Write code in the method reduceActorList to pick the first N:
	//actorList = reduceActorList (actorList, N);

	// 2. Build a hashmap of actor names to their Actor objects.

	// 3. Iterate through the movies:
	//      if the actor list is not null
	//         Build an array list of Actor objects with these actors.
	//         Process all pairs in this list.
	//         For each such pair, add one Actor to the other's actedWith list.

	// This should print for each actor who else acted with them
	// from the reduced list.
	for (Actor a: actorList) {
	    if (a.actedWith.size() > 0) {
		System.out.print ("In same movie as " + a.name + ": ");
		for (Actor b: a.actedWith) {
		    System.out.print (" " + b.name);
		}
		System.out.println ();
	    }
	}

	// Return this.
	return actorList;
    }

   static ArrayList<Actor> reduceActorList (ArrayList<Actor> actorList, int N)
    {
	// WRITE YOUR CODE HERE to create a new array list (of actors)
	// with at most N elements, copying over the first N (or fewer)
	// elements from actorList. The idea is to reduce clutter in the cloud.

	// Temporarily:
	return null;
    }

    static void readMovieData ()
    {
	DataTool.readData ("movies.data");
	int n = DataTool.getSize ();
	for (int i=0; i<n; i++) {
	    MovieData m = new MovieData ();
	    m.budget = DataTool.getDoubleValue ("budget", i);
	    m.revenue = DataTool.getDoubleValue ("revenue", i);
	    m.title = DataTool.getValue ("title", i);
	    m.director = DataTool.getValue ("director", i);
	    m.actors = DataTool.getValues ("actors", i);
	    movies.add (m);
	}	
    }

}
