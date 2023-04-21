// IOTool.java
// 
// Author: Rahul Simha
// Summer 2017
// Modified: June 2018
// 
// A tool that hides the ugly details of screen and file IO
// for initial use. We'll later expose the detail.

import java.util.*;
import java.io.*;

public class IOTool {

    static Scanner fileByCharScanner = null;
    static String fileByCharLine = null;
    static int fileByCharPos = 0;

    static Scanner fileByWordScanner = null;
    static ArrayList<String> currentLineWords = new ArrayList<String> ();

    static Scanner fileBySentenceScanner = null;
    static ArrayList<ArrayList<String>> currentSentences = new ArrayList<>();
    static ArrayList<String> nextSentenceWords = new ArrayList<> ();

    public static String readStringFromTerminal (String prompt)
    {
	System.out.print (prompt);
	Scanner console = new Scanner (System.in);
	String s = console.nextLine ();
	return s;
    }

    public static int readIntFromTerminal (String prompt)
    {
	System.out.print (prompt);
	Scanner console = new Scanner (System.in);
	int i = console.nextInt ();
	return i;
    }

    public static double readDoubleFromTerminal (String prompt)
    {
	System.out.print (prompt);
	Scanner console = new Scanner (System.in);
	double d = console.nextDouble ();
	return d;
    }

    public static String[] readTextFileAsStringArray (String filename)
    {
	ArrayList<String> lines = readTextFileAsStringList (filename);
	// Convert to array.
	String[] lineArray = (String[]) lines.toArray ();
	return lineArray;
    }

    public static ArrayList<String> readTextFileAsStringList (String filename)
    {
	try {
	    File f = new File (filename);
	    if (! f.exists()) {
		System.out.println ("No such file: " + filename);
		return null;
	    }
	    LineNumberReader lnr = new LineNumberReader (new FileReader(f));
	    ArrayList<String> lines = new ArrayList<String> ();
	    String line = lnr.readLine ();
	    while (line != null) {
		lines.add (line);
		line = lnr.readLine ();
	    }
	    return lines;
	}
	catch (Exception e) {
	    System.out.println (e);
	    e.printStackTrace ();
	    System.exit (0);
	}
	return null;
    }

    public static void writeTextFileFromStringArray (String filename, String[] lines)
    {
	try {
	    PrintWriter pw = new PrintWriter (new FileWriter (filename));
	    for (String s: lines) {
		pw.println (s);
	    }
	    pw.flush ();
	    pw.close ();
	}
	catch (Exception e) {
	    System.out.println (e);
	    e.printStackTrace ();
	    System.exit (0);
	}
	
    }

    public static int stringToInt (String s)
    {
	try {
	    // Remove surrounding whitepace.
	    s = s.trim ();
	    // Parse.
	    int k = Integer.parseInt (s);
	    return k;
	}
	catch (Exception e) {
	    // If the string was illegal, our action is harsh: halt.
	    System.out.println ("Illegal string for integer: " + s);
	    System.exit (0);
	    // We won't be reaching here but the compiler needs a 
	    // return to be written.
	    return -1;
	}
    }

    static void openFileByChar (String filename)
    {
	fileByCharPos = 0;
	try {
	    fileByCharScanner = new Scanner (new File (filename));
	    if (fileByCharScanner.hasNextLine()) {
		fileByCharLine = fileByCharScanner.nextLine ();
	    }
	}
	catch (IOException e) {
	    System.out.println ("Could not open file: " + filename);
	    System.exit (0);
	}
    }

    static void closeFileByChar ()
    {
	fileByCharScanner.close ();
    }


    static int getNextChar ()
    {
	if (fileByCharLine == null) {
	    return -1;
	}

	if (fileByCharPos >= fileByCharLine.length()) {
	    if (fileByCharScanner.hasNextLine()) {
		fileByCharLine = fileByCharScanner.nextLine ();
		fileByCharPos = 0;
		// End of line, return <cr>
		return (int) '\n';
	    }
	    else {
		// We're done.
		return -1;
	    }
	}

	char c = fileByCharLine.charAt (fileByCharPos);
	fileByCharPos ++;
	return (int) c;
    }

    static void openFileByWord (String filename)
    {
	currentLineWords = new ArrayList<String> ();
	try {
	    fileByWordScanner = new Scanner (new File (filename));
	}
	catch (IOException e) {
	    System.out.println ("Could not open file: " + filename);
	    System.exit (0);
	}
    }

    static void extractWords (String line)
    {
	currentLineWords = new ArrayList<String> ();
	char[] letters = line.toCharArray ();
	boolean inWord = false;
	String word = "";
	for (int i=0; i<letters.length; i++) {
	    char c = letters[i];
	    if (inWord) {
		if ( (Character.isLetter(c)) || (c=='\'') ) {
		    word += c;
		}
		else {
		    // Word has ended.
		    inWord = false;
		    currentLineWords.add (word.toLowerCase());
		    word = "";
		}
	    }
	    else {
		if ( (Character.isLetter(c)) || (c=='\'') ) {
		    // Start a new word;
		    word = "" + c;
		    inWord = true;
		}
		else {
		    inWord = false;
		}
	    }
	}

	// See if a word has been ended by the line.
	if (word.length() > 0) {
	    // This hasn't been added yet.
	    currentLineWords.add (word.toLowerCase());
	}
    }

    static boolean hasChar (String line)
    {
	char[] c = line.toCharArray ();
	for (int i=0; i<c.length; i++) {
	    if ( (Character.isLetter(c[i])) || (c[i]=='\'') ) {
		return true;
	    }
	}
	return false;
    }

    public static String getNextWord ()
    {
	if (currentLineWords.size() > 0) {
	    return currentLineWords.remove (0);
	}

	boolean found = false;
	String line = null;
	while (! found) {
	    if (fileByWordScanner.hasNextLine()) {
		line = fileByWordScanner.nextLine ();
		if (hasChar(line)) {
		    extractWords (line);
		    found = true;
		}
	    }
	    else {
		return null;
	    }
	}

	// If we've reached here, there are chars, and therefore words.
	return currentLineWords.remove (0);
    }

    static void openFileBySentence (String filename)
    {
	currentSentences = new ArrayList<>();
	try {
	    fileBySentenceScanner = new Scanner (new File (filename));
	}
	catch (IOException e) {
	    System.out.println ("Could not open file: " + filename);
	    System.exit (0);
	}
    }

    /*
    public static LinkedList<String> getNextSentenceAsList ()
    {
	ArrayList<String> sentenceArray = getNextSentence ();
	if (sentenceArray == null) {
	    return null;
	}
	LinkedList<String> sentenceWordList = new LinkedList<>();
	for (String s: sentenceArray) {
	    sentenceWordList.add (s);
	}
	return sentenceWordList;
    }
    */

    public static ArrayList<String> getNextSentence ()
    {
	if (currentSentences.size() > 0) {
	    // There is something in the collection of sentences.
	    //System.out.println (">> initial return");
	    return currentSentences.remove (0);
	}

	// Otherwise, look for the next sentence or sentences, 
	// searching for period.
	if (fileBySentenceScanner.hasNextLine()) {
	    ArrayList<String> lineWords = new ArrayList<> ();
	    String line = fileBySentenceScanner.nextLine ();
	    while ( (line != null) && (hasNoPeriod(line)) ) {
		extractWords (line, lineWords);
		//System.out.println (">>>> In innerwhile: line=[" + line + "] hasNext=" + fileBySentenceScanner.hasNextLine() + " lineWords: " + lineWords);
		if (! fileBySentenceScanner.hasNextLine()) {
		    line = null;
		    break;
		}
		line = fileBySentenceScanner.nextLine ();
	    }
	    //System.out.println (">> After innerwhile: line=[" + line + "] lineWords: " + lineWords);
	    if (line == null) {
		// We're done. 
		if (lineWords.size() > 0) {
		    for (String s: lineWords) {
			nextSentenceWords.add (s);
		    }
		    ArrayList<String> sentenceWords = nextSentenceWords;
		    nextSentenceWords = new ArrayList<> ();
		    return sentenceWords;
		}
		else {
		    // Nothing was found.
		    return null;
		}
	    }
	    // At this point, we possibly have words in lineWords,
	    // and a line with one or more periods, which means one or 
	    // more sentences.
	    
	    ArrayList<String> remWords = extractSentences (line, lineWords);
	    // Add remWords and extracted words into currentSentenceWords
	    //System.out.println (">> After innerwhile2: remWords: " + remWords + " nextSentWords: " + nextSentenceWords + " currentSentences=" + currentSentences);
	    ArrayList<String> sentenceWords = new ArrayList<>();
	    if ( (currentSentences != null) && (currentSentences.size() > 0) ) {
		sentenceWords = currentSentences.remove (0);
	    }
	    // Add prior saved words.
	    for (String s: sentenceWords) {
		nextSentenceWords.add (s);
	    }
	    // Add line words 
	    for (String s: lineWords) {
		nextSentenceWords.add (s);
	    }
	    sentenceWords = nextSentenceWords;
	    nextSentenceWords = remWords;
	    return sentenceWords;
	}

	return null;
    }


    static boolean hasNoPeriod (String line)
    {
	char[] letters = line.toCharArray ();
	for (int i=0; i<letters.length; i++) {
	    if (letters[i] == '.') {
		return false;
	    }
	}
	return true;
    }

    static void extractWords (String line, ArrayList<String> lineWords)
    {
	// Assume: lineWords has been initialized. We just add to it.
	char[] letters = line.toCharArray ();
	boolean inWord = false;
	String word = "";
	for (int i=0; i<letters.length; i++) {
	    char c = letters[i];
	    if (inWord) {
		if ( (Character.isLetter(c)) || (c=='\'') ) {
		    word += c;
		}
		else {
		    // Word has ended.
		    inWord = false;
		    lineWords.add (word.toLowerCase());
		    word = "";
		}
	    }
	    else {
		if ( (Character.isLetter(c)) || (c=='\'') ) {
		    // Start a new word;
		    word = "" + c;
		    inWord = true;
		}
		else {
		    inWord = false;
		}
	    }
	}

	// See if a word has been ended by the line.
	if (word.length() > 0) {
	    // This hasn't been added yet.
	    lineWords.add (word.toLowerCase());
	}
    }


    static ArrayList<String> extractSentences (String line, ArrayList<String> lineWords)
    {
	// lineWords, if not empty/null, go into the first sentence.
	// One has to save the remaining words at the end and return it.
	if (lineWords == null) {
	    // We're going to possibly add so keep this handy.
	    lineWords = new ArrayList<> ();
	}

	// Assume: lineWords has been initialized. We just add to it.
	ArrayList<String> currentSentence = new ArrayList<> ();
	char[] letters = line.toCharArray ();
	boolean inWord = false;
	boolean inRemainder = false;
	boolean inFirstSentence = true;
	String word = "";
	for (int i=0; i<letters.length; i++) {
	    char c = letters[i];
	    if (inWord) {
		if ( (Character.isLetter(c)) || (c=='\'') ) {
		    word += c;
		}
		else {
		    // Word has ended.
		    inWord = false;
		    currentSentence.add (word.toLowerCase());
		    if (c == '.') {
			if (inFirstSentence) {
			    addToFront (lineWords, currentSentence);
			    inFirstSentence = false;
			}
			currentSentences.add (currentSentence);
			currentSentence = new ArrayList <>();
		    }		    
		    word = "";
		}
	    }
	    else {
		if ( (Character.isLetter(c)) || (c=='\'') ) {
		    // Start a new word;
		    word = "" + c;
		    inWord = true;
		}
		else {
		    inWord = false;
		}
	    }
	}

	// See if a word has been ended by the line.
	if (word.length() > 0) {
	    currentSentence.add (word.toLowerCase());
	}

	// If we reached here, we have no more periods. So all the
	// words in the current sentence are part of the following line

	return currentSentence;
    }

    static void addToFront (ArrayList<String> list1, ArrayList<String> list2)
    {
	// Add from list1 to the front of list2.
	for (int i=list1.size()-1; i>=0; i--) {
	    String s = list1.remove (i);
	    list2.add (0, s);
	}
    }

}
