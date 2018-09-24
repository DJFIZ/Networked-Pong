# Networked-Pong

Specifics:

This program is extremely simple, simply connecting two players on a local network via a server to play a game of pong.
In playing pong itself, only the basic functions have been implemented.
A ball will 'spawn' at the middle of the screen and randomly move at an angle toward one side or the other.
The ball will reset to the middle if it passes either paddle and goes off-screen.


How to play:

To play the game, a computer must host the server and two other instances must connect to said server. 
This is easily done through a streamlined set of GUIs. 
Simply run PongLauncher and you will be prompted through several GUIs to help you set up or join a server.
Be prepared to enter portnumbers and hostnames.
Once two players have connected, the jFrame will load and the game will automatically begin.
The first player to join will be the paddle on the left; the second, the paddle on the right.

To move the paddles, use either 'w' or the uparrow to move up.
To move down, use either 's' or the downarrow.


Noticeable issues/possible fixes/future implementation:
(I know that seems like a lot for one section but bare with me)

The reason this program seems so simple is because it was intended to be. However, while making this program I found myself wanting to
implement more and more functionlity. Ultimately most of it ended up convoluting my code,and was thus commented outbut was never 
abandoned entirely. Within the code many variables can be found unused or otherwise commented out. These are placeholders for possible
future implementation or ways that might make the program run more efficiently.

The big issue, in fact the only issue I can think of as pertinent to the code's finalization is its usage of while(true) loops.
Several chunks of code require loops to be ran an indefinite number of times, but any programmer knows the only way to break such loops
is through throwing an error or terminating the JVM. Consequently, unless both (or all depending on if someone joins there own server or
not) JVMs are terminated simmultaneously, errors will be thrown. In my opinion these would best be handled through monitoring when a
player disconnects, though my attempts at doing so seemed to do a lot more harm than good.


Final remarks:

The game was desgined to be a basic proof of concept. This was my first extensive integration of sockets in java and resulted
in a great number of difficulties. Building a networked game of pong proved to be much harder than I had anticipated, however I am proud
of the result.

All of this code was written solely by me and may be reused as seen fit.
-Dustin Ficenec
