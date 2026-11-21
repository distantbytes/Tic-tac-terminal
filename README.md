# Tic-Tac-Terminal

Terminal MNK games written in *Kotlin*, with GOFAI opponent. The bot is designed to be beatable, of course.

Forget about all the maths stuff, here the most brute-force algorithm is used, the idea was simple, to loop through every square on each line, all directions, and analyze the patterns.

We've improved the algorithm __significantly__! Now the bot is much smarter and takes the initiative to attack.
![](docs/005.png)
___
### How to play
Download the jar from [releases](https://codeberg.org/distantbytes/Tic-tac-terminal/releases), then use this terminal command:
```shell
java -jar Tic-tac-terminal-<verison>.jar
```
Add argument `1` `2` or `3` to specify board.

For example:
```shell
java -jar Tic-tac-terminal-<version>.jar 2
```

(Replace `<version>` with actual version)

### Requirements
- _JDK 11_
___
### Have any problems? 
Go to [issues page](https://codeberg.org/distantbytes/Tic-tac-terminal/issues) 