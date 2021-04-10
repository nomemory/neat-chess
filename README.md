A simple [UCI](https://en.wikipedia.org/wiki/Universal_Chess_Interface) (*Universal Chess Interface*) Client written in Java.

Tested with [Stockfish 13](https://stockfishchess.org/blog/2021/stockfish-13/).

# Documentation 

## Starting / Closing the client

By using the `startStockfish()` method, the client assumes that Stockfish is already installed on the system, and it's accesible in `$PATH` as `"stockfish"`.

```java
var uci = new UCI();
uci.startStockfish();
```        

Using `start(String cmd)` **neat-chess** can be tested with other chess engines:

```java
// leela chess 
var uci = new UCI();
uci.start("lc0");
```

By default each command you are sending to the engine, has a timeout of `60s` (during which the thread is blocked).

You can configure the global default timeout to a different value:

```java
var uci = new UCI(5000l); // default timeout 5 seconds
uci.startStockfish();
```

If the client commands exceed the timeout interval an unchecked `UCITimeoutException` is thrown. But more on that in the next sections.

To retrieve the `defaultTimeout` simply call: `var timeout = uci.getDefaultTimeout()`.

To close the client simply call: `uci.close();`.

## Commands

Most commands respond with an `UCIResponse<T>` object. This class wraps a possible exception, and the actual result value.

The most common idiom(s) is/are:

```java
var response = uci.<some_command>(...);
var result = response.getResultOrThrow();
// do something with the result
```

or

```java
var response = uci.<some_command>(...);
if (response.succes()) {
  var result = response.getResult();
  // do something with the result
}
```
