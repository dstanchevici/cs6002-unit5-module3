
import java.io.*;
import java.util.*;

class DataItem {
    public boolean isList = false;
    public String name;
    public String value;
    public ArrayList<String> values;
    public String toString ()
    {
	String s = name +":";
	if (! isList) {
	    s += " " + value;
	    return s;
	}
	for (String v: values) {
	    s += " [" + v + "]";
	}
	return s;
    }
}

public class DataTool {

    static ArrayList<HashMap<String, DataItem>> data;
    static ArrayList<String> keyList;

    public static int getSize ()
    {
	if (data == null) return 0;
	else return data.size();
    }

    public static ArrayList<String> getFieldNames ()
    {
	return keyList;
    }

    public static int stringToInt (String s)
    {
	try {
	    return Integer.parseInt (s.trim());
	}
	catch (Exception e) {
	    System.out.println ("ERROR: cannot convert to int: " + s);
	    return -1;
	}
    }

    public static double stringToDouble (String s)
    {
	try {
	    return Double.parseDouble (s.trim());
	}
	catch (Exception e) {
	    System.out.println ("ERROR: cannot convert to double: " + s);
	    return -1;
	}
    }

    public static String getValue (String name, int itemNum)
    {
	if (itemNum >= data.size()) {
	    return null;
	}
	HashMap<String, DataItem> map = data.get (itemNum);
	DataItem d = map.get (name);
	if (d != null) {
	    if (! d.isList) {
		return d.value;
	    }
	}
	return null;
    }

    public static int getIntValue (String name, int itemNum)
    {
	String v = getValue (name, itemNum);
	if (v == null) {
	    System.out.println ("ERROR: cannot find item# " + itemNum + " in category " + name);
	    System.exit (0);
	}
	return stringToInt(v);
    }

    public static double getDoubleValue (String name, int itemNum)
    {
	String v = getValue (name, itemNum);
	if (v == null) {
	    System.out.println ("ERROR: cannot find item# " + itemNum + " in category " + name);
	    System.exit (0);
	}
	return stringToDouble(v);
    }

    public static ArrayList<String> getValues (String name, int itemNum)
    {
	if (itemNum >= data.size()) {
	    return null;
	}
	HashMap<String, DataItem> map = data.get (itemNum);
	DataItem d = map.get (name);
	if (d != null) {
	    if (d.isList) {
		return d.values;
	    }
	}
	return null;
    }


    public static ArrayList<Integer> getIntValues (String name, int itemNum)
    {
	ArrayList<String> values = getValues (name, itemNum);
	if (values == null) {
	    System.out.println ("ERROR: cannot find item # " + itemNum + " in category " + name);
	    System.exit (0);
	}
	ArrayList<Integer> intValues = new ArrayList<>();
	for (String s: values) {
	    intValues.add ( stringToInt(s) );
	}
	return intValues;
    }

    public static ArrayList<Double> getDoubleValues (String name, int itemNum)
    {
	ArrayList<String> values = getValues (name, itemNum);
	if (values == null) {
	    System.out.println ("ERROR: cannot find item # " + itemNum + " in category " + name);
	    System.exit (0);
	}
	ArrayList<Double> dValues = new ArrayList<>();
	for (String s: values) {
	    dValues.add ( stringToDouble(s) );
	}
	return dValues;
    }


    public static void readData (String filename) 
    {
	try {
	    LineNumberReader lnr = new LineNumberReader (new FileReader(filename));
	    String line = lnr.readLine ();
	    data = new ArrayList<>();
	    keyList = new ArrayList<>();
	    DataItem currentItem = null;
	    HashMap<String, DataItem> currentMap = null;
	    int lineNum = 0;
	    while (line != null) {
		line = line.trim ();
		lineNum++;

		//if (lineNum > 100) break;

		if ((line.length() == 0) || (line.startsWith("#"))) {
		    line = lnr.readLine ();
		    continue;
		}

		if (line.startsWith(".")) {
		    // Put previous map in list.
		    if (currentMap != null) {
			//printMap (currentMap);
			data.add (currentMap);
		    }
		    currentMap = new HashMap<>();
		    line = lnr.readLine ();
		    continue;
		}

		// Otherwise, it's data.
		DataItem d = parse (line);
		if (d == null) {
		    System.out.println ("ERROR in data file: line# " + lineNum + " line: " + line);
		    System.exit (0);
		}

		currentMap.put (d.name, d);
		if (! keyList.contains(d.name) ) {
		    keyList.add (d.name);
		}

		line = lnr.readLine ();

	    } // endwhile.

	    if (currentMap != null) {
		data.add (currentMap);
	    }

	}
	catch (Exception e) {
	    System.out.println ("ERROR in data file. Read failed");
	    System.exit (0);
	}
    }

    static void printMap (HashMap<String, DataItem> map) 
    {
	System.out.println ("Map: ");
	for (String key: keyList) {
	    DataItem d = map.get (key);
	    System.out.println (d);
	}
    }

    static DataItem parse (String line)
    {
	line = line.trim ();
	DataItem d = new DataItem ();

	// Find first colon, save name.
	int k = line.indexOf (':');
	if (k < 0) {
	    // Not found.
	    return null;
	}
	d.name = line.substring (0, k);
	String valStr = line.substring (k+1, line.length());
	valStr = valStr.trim();
	if (! valStr.startsWith("[")) {
	    // Plain value.
	    if (valStr.length() > 0) {
		d.value = valStr;
		if (d.value.equalsIgnoreCase("null")) {
		    d.value = null;
		}
	    }
	    else {
		d.value = null;
	    }
	    return d;
	}

	// Otherwise, it's a list.
	d.values = new ArrayList<>();
	d.isList = true;
	int index = valStr.indexOf ("[");
	while (index >= 0) {
	    // Find matching ]
	    int index2 = valStr.indexOf ("]");
	    //System.out.println ("1. index=" + index + " index2=" + index2);
	    if (index2 < 0) {
		return null;
	    }
	    String v = valStr.substring (index+1, index2);
	    d.values.add (v);
	    //System.out.println ("2. index=" + index + " index2=" + index2 + " v=" + v + " valStr=" + valStr);
	    valStr = valStr.substring (index2+1, valStr.length());
	    index = valStr.indexOf ("[");
	    //System.out.println ("3. index=" + index + " valStr=" + valStr);
	}

	return d;
    }

    public static void main (String[] argv)
    {
	//simpleTest ();
	testWithData ();
    }

    static void testWithData ()
    {
	readData ("movies.data");
	System.out.println ("Data size: " + getSize());
	System.out.print ("Fields: ");
	ArrayList<String> fieldnames = getFieldNames ();
	for (String field: fieldnames) {
	    System.out.print (" " + field);
	}
	System.out.println ();

	for (int i=0; i<3; i++) {
	    String title = getValue ("title", i);
	    System.out.println (title);
	    ArrayList<String> actors = getValues ("actors", i);
	    System.out.print ("Actors: ");
	    for (String s: actors) {
		System.out.print (" [" + s + "]");
	    }
	    System.out.println ();
	}
    }

    static void simpleTest ()
    {
	String testStr = "dir: blah: blah2";
	//String testStr = "genrelist: [Action] [Adventure] [Fantasy] [Science Fiction]";
	DataItem d = parse (testStr);
	System.out.println (d);
    }

}
