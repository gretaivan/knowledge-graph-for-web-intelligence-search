package w1670486_WI_CW;

import java.io.InputStream;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.*;
import java.util.HashMap; // import the HashMap class
import java.util.Scanner;
import org.apache.jena.query.*;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.util.iterator.ExtendedIterator;

/**
 * @author w1670486 Main class to execute coursework tasks
 */
public class main extends Object {

    //empty model
    static Model model = ModelFactory.createDefaultModel();
    static String inputFileName = "w1670486_WI.rdf";

    public static void main(String args[]) {

        //call the rdf graph reader method
        readRDF(inputFileName);

        //task 5a. display model in rdf/xml format
        model.write(System.out);

        //task 5b: display all concepts
        queryConcepts();

        //task 5c: display all instances
        queryInstances();
        
        
        
        //task 5d: All concepts as part of knowledge graph
        queryhierarchy(); 

        //task 5e: get nodes describing the given resource - natural disasters
        String resource = "sector";
        queryDescribe(resource);
        
        //task 5f get Natural Disasters Resource
        dbpedia(); 

        //commands - for easier readability 
        HashMap<Integer, Runnable> commands = new HashMap<Integer, Runnable>();
        commands.put(1, () -> model.write(System.out));
        commands.put(2, () -> queryConcepts());
        commands.put(3, () -> queryInstances());
        commands.put(4, () -> queryhierarchy());
        commands.put(5, () -> queryDescribe(resource));
        commands.put(6, () -> dbpedia());
        //Get selection
        Scanner input = new Scanner(System.in);
        int cmd;

        System.out.println("\nSEE ABOVE FOR DEFAULT RUN RESULTS");
        menu();

        do {
            // Invoke command
            cmd = input.nextInt();
            commands.get(cmd).run();

            System.out.println("\nCommand " + cmd + " executed. Enter another command or 0 to exit or 9 to see menu: ");
        } while (cmd != 0);
    }

    //coresponds to task 4: imports knowledge graph into the memory model
    private static void readRDF(String inputFileName) {
        InputStream in = FileManager.get().open(inputFileName);

        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }
        // read the RDF/XML file
        model.read(in, "");
    }

    //Prints all concept literal values found in rdf graph
    private static void queryConcepts() {
        System.out.println("===============================================");
        System.out.println("EXISTING CONCEPTS: ");
        System.out.println("===============================================");
        
        String conceptQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
            +"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
            +"select * where { ?concepts rdf:type rdfs:Class . }";
        
        Query query = QueryFactory.create(conceptQuery);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)){
            ResultSet rs = qexec.execSelect();
            ResultSetFormatter.out(System.out, rs);
        }
        
        System.out.println("\nThe literal values of concepts below: ");
        ResIterator iter = model.listResourcesWithProperty(RDFS.label);

        if (iter.hasNext()) {
            
            int i = 1;

            while (iter.hasNext()) {
                System.out.println(i + ":  " + iter.nextResource()
                        .getRequiredProperty(RDFS.label)
                        .getString());
                i++;
            }
        } else {
            System.out.println("No mathching properties were found in the RDF file");
        }
    }

    //prints all instances found in the graph
    private static void queryInstances() {

        System.out.println("===============================================");
        System.out.println("EXISTING INSTANCES: ");
        System.out.println("===============================================");

        StmtIterator iter = model.listStatements();
        int i = 1;

        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();  // get next statement
            Resource subject = stmt.getSubject();     // get the subject
            Property predicate = stmt.getPredicate();   // get the predicate
            RDFNode object = stmt.getObject();      // get the object

            if ((object.isLiteral()) && !(object instanceof Resource) && (predicate.toString().equalsIgnoreCase("http://schema.org/name"))) {
                System.out.println(i + ": " + object.toString() + " ");
                i++;
            }
        }
    }
    
    //optional functionality for menu
    private static void menu() {
        System.out.println("===============================================");
        System.out.println("Please enter the corresponding integer to the desired command: \n"
                + "1: To display graph in RDF/XML format\n"
                + "2: To display all concepts in the graph\n"
                + "3: To display all instances in the graph\n"
                + "4: To display all concepts that are part of graph hierarchy"
                + "5: To return all nodes describing the resource\n"
                + "6: To extract given resource from dbpedia\n"
                + "0: to stop");
        System.out.println("===============================================");
    }
    
    public static void query(String queryString){
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)){
            ResultSet rs = qexec.execSelect();
            ResultSetFormatter.out(System.out, rs);
        }
    }

    private static void queryDescribe(String resource) {
        System.out.println("===============================================");
        System.out.println("THE DESCRIBING NODES FOR: " + resource + ": ");
        System.out.println("===============================================");
        String queryDescribe = "PREFIX dict: <http://dictionary.cambridge.org/dictionary/english/>"
            +"PREFIX so: <http://schema.org/>"
            +"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
            +"SELECT * WHERE { <https://www.climatetagger.net/climate-thesaurus/#" + resource +"> ?p ?o .}";
        
        query(queryDescribe);
    }

    private static void dbpedia() {
        System.out.println("===============================================");
        System.out.println("QUERY TO DBPEDIA RESULTS:");
        System.out.println("===============================================");
        String dbpediaQuery = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
            + "SELECT * WHERE {<http://dbpedia.org/resource/Category:Natural_disasters> ?p ?o .}";
       
        Query query = QueryFactory.create(dbpediaQuery);
       
         // Remote execution.
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
            // Set the DBpedia specific timeout.
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

            ResultSet rs = qexec.execSelect();
           // ResultSetFormatter.out(System.out, rs, query);
  
            try {
                ResultSetRewindable results = ResultSetFactory.makeRewindable(rs);
                ResultSetFormatter.out(System.out, results);
                results.reset();
            } 
            
            finally { qexec.close(); }      
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        /*
        *There is some similarity between the created rdf graph and extracted from
        *dbpedia. First, concept and class have the same purpose in this instance, 
        *and could be interpreted as related. As well as it has and exactly same
        *predicate. The extracted information is from much larger knowledge graph
        */
    }

    private static void queryhierarchy() {
         System.out.println("===============================================");
        System.out.println("HIERARCHY OF ALL CONCEPTS");
        System.out.println("===============================================");
        StmtIterator iter = model.listStatements();
        int i = 1;

        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();  // get next statement
            Resource subject = stmt.getSubject();     // get the subject
            Property predicate = stmt.getPredicate();   // get the predicate
            RDFNode object = stmt.getObject();      // get the object
            System.out.println("______________________________________");
            System.out.println("\nTriple: " +  i +":");

            System.out.println("Subject: " + subject.toString());
            System.out.println("Predicate: "+ predicate.toString());
            System.out.println("Object: " + object.toString());
            i++;
        }
    }

}
