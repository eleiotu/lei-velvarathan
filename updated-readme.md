# Assignment 01 - Spam Detector 

## Project Information
This project is essentially a spam detector we have developed. This program filters through your emails collecting the the frequency of each word and how much of your emails it appears in.
Using Baye's Theorem we essentially create our own spam filter by training it through our old emails that we have labeled spam or ham. Spam meaning emails that are not desired while ham is the good stuff.
We then test our program on some test files to determine the accuracy of our program. 
This project was create by Edison Lei and Nirujan Velvarathan



## Improvements
We improved our model by making changes to our spam filter. We only include words in our frequency find if they consisted of letters. This removed a lot of unnecessary filler characters
in emails such as from and to addresses and other email meta data. Secondly we improved our training by including both ham folders which increased the amount of data our program 
took in which increased its accuracy


## How to Run

To run this project
First, open up the client folder and server folder in two seperate intellij projects. 
Second, start up the glassfish server and make sure the artifact has been successfully deployed. 
Third, once the server is up and running successfully, you can go into the client project and open up one of the html files. Once this is up you should be able to see the 
the data from the spam filter as well as the accuracy and precision. 


