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
	actorList = reduceActorList (actorList, N);

	// 2. Build a hashmap of actor names to their Actor objects.
	// Then the same object will be accessible from both actorList and actorMap
	HashMap<String, Actor> actorMap = new HashMap<>();
	for (Actor a: actorList) {
	    String key = a.name; // Pointer copy: key and a.name point to the same String object
	    Actor value = a; // Pointer copy: value and a point to the same Actor object
	    actorMap.put (key, value);
	}
	// What if key or value were primitives, like ints?

	for (Map.Entry<String,Actor> mapElement : actorMap.entrySet()) {
            System.out.println (mapElement.getKey());
	}
       
	// 3. Iterate through the movies:
	for (MovieData m: movies) {
	    // if the actor list is not null
	    if (m.actors == null) continue;
	    
	    // Build an array list of Actor objects with these actors.
	    ArrayList<Actor> movieActors = new ArrayList<>();
;
	    for (String a: m.actors) {
		if ( actorMap.containsKey(a) ) {
		    Actor actorObjects = actorMap.get (a);
		    movieActors.add (actorObjects);
		}
	    }

	    
	    // Process all pairs in this list.
	    // For each such pair, add one Actor to the other's actedWith list.
	    if (movieActors.size() > 1) {
		for (int i=0; i<movieActors.size(); i++) {
		    for (int j=0; j<movieActors.size(); j++) {
			if ( movieActors.get(i) == movieActors.get(j) ) continue; // evry i adds movieActors.size()-1
			ArrayList<Actor> costarList = movieActors.get(i).actedWith;
			Actor newCostar = movieActors.get(j);
			if ( !  costarList.contains (newCostar) ) {
			    costarList.add (newCostar);
			}

		    }
		}
	    }
	   
	    
	    
	} // end - for each movie
	

	
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

	ArrayList<Actor> reducedList = new ArrayList<>();
	
	for (int i=0; i<N; i++) {
	    Actor a = actorList.get(i);
	    reducedList.add (a);
	}

	return reducedList;
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
