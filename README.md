# neat-chess

POC implementation for the UCI Protocol. 

This project is work in progress.

The API is subject to change.

`stockfish` is installed as a command-line tool (for MAC: `brew install stockfish`)

Example:

```java
public static void main(String[] args) {
    long timeout = 4000l;
    var uci = new UCI();
    try {
        uci.start(STOCKFISH);
        uci.uci(timeout);
        uci.setoption("MultiPV", 10, timeout);
        var result = uci.go(20, 1000l, timeout);
        //Showing best moves
        System.out.println(result);
        uci.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    uci.close();
}
```
