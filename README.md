A simple [UCI](https://en.wikipedia.org/wiki/Universal_Chess_Interface) (*Universal Chess Interface*) Client written in Java.

Tested with [Stockfish 13](https://stockfishchess.org/blog/2021/stockfish-13/).

# Documentation 

## Starting / Closing the client

By using the `startStockfish()` method, the client assumes that Stockfish is already installed on the system, and it's accesible in `$PATH` as `stockfish`.

```java
var uci = new UCI();
uci.startStockfish();
```        

To test it with other engines the `start()` method can be used:

```java
var uci = new UCI();
uci.start("lc0");
```

By default each command you are sending to the engine, has a timeout of `60s` (during which the thread is blocked).

You can configure the global default timeout to a different value:

```java
var uci = new UCI(5000l); // default timeout 5 seconds
uci.startStockfish();
uci.close();
```

