# RDF/XML based knowledge graph plus source code for the WI Search Application

## Deliverables
1. Display the knowledge graph in RDF/XML format.
2. Display all concepts known to the knowledge graph;
3. Disply all instances known to the knowledge graph;
4. Display the classification tree/hierarchy of all concepts being part of the extracted knowledge graph.
5. Given a particular resource as search keyword, return all nodes describing that resource (i.e., RDF predicates and objects).
6. Given the same resource as of (e), find on DBpedia similar resources, which are a good match with the one returned by (e). Justify and rank similarities with the matched DBpedia resources by suggesting appropriate metrics.

## Process
1. Construct a knowledge graph initially with concepts or classes, at most ten (10), the white paper is referring to. The vocabulary to be used must be the Climate Thesaurus available at https://www.climatetagger.net/climate-thesaurus/
2. Add to the knowledge graph instances (individuals), at most ten (10), the white paper is referring to. These can be entities or particular things the document is discussing about. You may use any vocabulary you think is appropriate.
3. Convert your knowledge graph into RDF/XML to be validated by the RDF validator service available at http://www.w3.org/RDF/Validator/
4. Use the Apache Jena Framework to set up a project and application, which imports your knowledge graph and creates an in memory model.
5. Run the application (console/terminal mode only) in order to display the following:
+ Display the knowledge graph in RDF/XML format.
+ Display all concepts known to the knowledge graph;
+ Disply all instances known to the knowledge graph;
+ Display the classification tree/hierarchy of all concepts being part of the extracted knowledge graph.
+ Given a particular resource as search keyword, return all nodes describing that resource (i.e., RDF predicates and objects). 
+ Given the same resource as of (e), find on DBpedia similar resources, which are a good match with the one returned by (e). Justify and rank similarities with the matched DBpedia resources by suggesting appropriate metrics.
