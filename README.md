WikiCat
=======

Wikipedia and Hot Cats

WikiCat is the result of a senior thesis project by Cris Feo and Benjamin Perez and advised by Andrew G. West and Insup Lee.

==================================================================

WikiCat has two main components: A Wikipedia category suggestion recommendation engine and a web-application for human labeling of these suggestions.

The Wikipedia category suggestion recommendation engine is a multithreaded Java application that recommends new Categories for Wikipedia pages by performing random walks along Wikipedia's hyperlinked graph of articles and categories.

The web-application for human labeling of the suggested categories is built in Python using the Flask microframework and shows the Wikipedia article in question, along with suggestions of new categories that may be relevant to that page. Users determine whether or not suggestions are relevant and results are stored in a database.

==================================================================

For more detailed information and analysis of WikiCat's algorithms, implementation, and performance, check out the project's final report at /Proposal/proposal.pdf 
