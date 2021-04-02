# neat-chess

POC implementation for the UCI Protocol. 

This is project is work in progresa and subject to change.

Example:

```java
public static void main(String[] args) {
    long timeout = 4000l;
    var uci = new UCI();
    try {
        uci.start(STOCKFISH);
        uci.uci(3000l);
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
