Heat distribution
------------
By emilssipols11


# Description
This project simulates heat distribution on a 2D metal square. It is possible to set temperatures of each of the 4 walls and also the max time limit. The simulation is computed by solving the corresponding partial differential equation (heat equation).

![GIF](https://raw.githubusercontent.com/emilssipols11/heat-distribution/master/heat.gif)

The corresponding colors are: white for 0K - 29K; cyan for 29K - 58K; blue for 58K to 87K; green for 87K - 116K;
yellow for 116K - 145K; magenta for 145K - 174K; red for >=174K. There also is code for writing the corresponding data to a text file and creating a graph in a graphing 
software application, for example gnuplot.

Example below: 1D metal rod heat distribution graph over time
![screenshot of 1D rod graph](https://raw.githubusercontent.com/emilssipols11/heat-distribution/master/1d_rod_graph.jpg)

# Setup
 Download the App.java file and move it into your projects source directory. The project also uses a C++ based visual library sfml that was rewritten in Java as jsfml and also the colt library for scientific and technical Computing in Java. Download the jsfml.jar and colt.jar files and add them as a libraries to your project. After that run the main method in App.java.
