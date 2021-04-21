A simple [UCI](https://en.wikipedia.org/wiki/Universal_Chess_Interface) (*Universal Chess Interface*) Client written in Java.

Tested with [Stockfish 13](https://stockfishchess.org/blog/2021/stockfish-13/).

Maven:

```xml
<dependency>
  <groupId>net.andreinc</groupId>
  <artifactId>neatchess</artifactId>
  <version>1.0</version>
</dependency>
```

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

An `UCIResponse<T>` can only throw a `UCIRuntimeException`. This class has the following sub-types:
- `UCIExecutionException` - when the `Thread` executing the command fails;
- `UCIInterruptedException`- when the `Thread` executing the command fails;
- `UCITimeoutException` - when `Thread` exeucuting the command timeouts;
- `UCIUncheckedIOException` - when the communication with the engine process fails;
- `UCIUnknownCommandException` - when the server doesn't understand the command the client has sent;
- `UCIParsingException` - when the engine sends output that the client doesn't understand;

All commands support custom timeout. Usually this is the last parametere of the command function:

```java
var response = uci.<some_command>(args, long timeout);
```

## Retrieving the engine information

The method for obtaining the engine information (the current engine name and supported engine options) is `UCIResponse<EngineInfo> = uci.getEngineInfo()`.

Example:

```java
var uci = new UCI(5000l); // default timeout 5 seconds
uci.startStockfish();
UCIResponse<EngineInfo> response = uci.getEngineInfo();
if (response.success()) {

    // Engine name
    EngineInfo engineInfo = response.getResult();
    System.out.println("Engine name:" + engineInfo.getName());
    
    // Supported engine options
    System.out.println("Supported engine options:");
    Map<String, EngineOption> engineOptions = engineInfo.getOptions();
    engineOptions.forEach((key, value) -> {
        System.out.println("\t" + key);
        System.out.println("\t\t" + value);
    });
}
uci.close();
```

Output:

```
Engine name:Stockfish 13
Supported engine options:
	Hash
		SpinEngineOption{name='Hash', defaultValue=16, min=1, max=33554432}
	Move Overhead
		SpinEngineOption{name='Move Overhead', defaultValue=10, min=0, max=5000}
	UCI_AnalyseMode
		CheckEngineOption{name='UCI_AnalyseMode', defaultValue=false}
	UCI_LimitStrength
		CheckEngineOption{name='UCI_LimitStrength', defaultValue=false}
	Threads
		SpinEngineOption{name='Threads', defaultValue=1, min=1, max=512}
	MultiPV
		SpinEngineOption{name='MultiPV', defaultValue=1, min=1, max=500}
/// and so on
```

## Setting an option

For changing an option of the engine you can use the following methods:
- `UCIResponse<List<String>> setOption(String optionName, String value, long timeout)`
- `UCIResponse<List<String>> setOption(String optionName, String value)`

For example, modifying the `MultiPV` option to `10` is as simple as:

```java
var uci = new UCI(5000l); // default timeout 5 seconds
uci.startStockfish();
uci.setOption("MultiPV", "10", 3000l).getResultOrThrow(); // custome timeout 3 seconds
uci.close();
```

## Getting the best move for a position

If you plan using the engine to analyse various positions that are not part of the same game, it's recommended to call the `uciNewGame()` first.

An UCI engine understands [FEN notations](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation), so the method to make the engine aware of the position he needs to analyse is: 
- `UCIResponse<List<String>> positionFen(String fen, long timeout)`
- `UCIResponse<List<String>> positionFen(String fen)`

After the position has been set on the board, to retrieve the best move:
- `UCIResponse<BestMove> bestMove(int depth, long timeout)` - to analyse for a given depth (e.g.: 18 moves deep);
- `UCIResponse<BestMove> bestMove(int depth)`;
- `UCIResponse<BestMove> bestMove(long moveTime, long timeout)` - to analyse for a fixed amount of time (e.g.: 10000l - 10 seconds);
- `UCIResponse<BestMove> bestMove(long moveTime)`;

Let's take the example the following position:

<img src="https://github.com/nomemory/neat-chess/blob/main/assets/position01.png" width="30%"/>

The corresponding FEN for the position is:

```
rnbqk3/pp6/5b2/2pp1p1p/1P3P1P/5N2/P1PPP1P1/RNBQKB2 b Qq - 0 14
```

The code that determines what the best move is:

```java
var uci = new UCI(5000l); // default timeout 5 seconds
uci.startStockfish();
uci.uciNewGame();

uci.positionFen("rnbqk3/pp6/5b2/2pp1p1p/1P3P1P/5N2/P1PPP1P1/RNBQKB2 b Qq - 0 14");

var result10depth = uci.bestMove(10).getResultOrThrow();
System.out.println("Best move after analysing 10 moves deep: " + result10depth);

var result10seconds = uci.bestMove(10_000l).getResultOrThrow();
System.out.println("Best move after analysing for 10 seconds: " + result10seconds);

uci.close();
```

## Analysing a position

Analysing the best N lines for a given FEN position is very similar to the code for finding what is best move. 

The methods for finding out what are the best lines are:
- `UCIResponse<Analysis> analysis(long moveTime, long timeout)` - to analyse for a given depth (e.g.: 18 moves deep);
- `UCIResponse<Analysis> analysis(long moveTime)`
- `UCIResponse<Analysis> analysis(int depth, long timeout)` - to analyse for a fixed amount of time (e.g.: 10000l - 10 seconds);
- `UCIResponse<Analysis> analysis(int depth)` 

> By default Stockfish analyses only one line, so if you want to analyse multiple lines in parallel, you need to set `MultiPV`: `uci.setOption("MultiPV", "10", 3000l)`

Let's take for example the following position:

<img src="https://github.com/nomemory/neat-chess/blob/main/assets/position02.png" width="30%"/>

The corresponding FEN for the position is:

```
r1bqkb1r/2pp1ppp/p1n2n2/1p2p3/4P3/1B3N2/PPPP1PPP/RNBQK2R w KQkq - 2 6
```

And in order to get the 10 best continuations, the code is:

```java
var uci = new UCI();
uci.startStockfish();
uci.setOption("MultiPV", "10");

uci.uciNewGame();
uci.positionFen("r1bqkb1r/2pp1ppp/p1n2n2/1p2p3/4P3/1B3N2/PPPP1PPP/RNBQK2R w KQkq - 2 6");
UCIResponse<Analysis> response = uci.analysis(18);
var analysis = response.getResultOrThrow();

// Best move
System.out.println("Best move: " + analysis.getBestMove());
System.out.println("Is Draw: " + analysis.isDraw());
System.out.println("Is Mate: " + analysis.isMate());

// Possible best moves
var moves = analysis.getAllMoves();
moves.forEach((idx, move) -> {
    System.out.println("\t" + move);
});

uci.close();
```

The output:

```
Best move: 
	Move{lan='e1g1', strength=0.6, pv=1, depth=18, continuation=[c8b7, d2d3, f8c5, b1c3, ...]}
Best moves:
	Move{lan='e1g1', strength=0.6, pv=1, depth=18, continuation=[c8b7, d2d3, f8c5, b1c3, ...]}
	Move{lan='d2d4', strength=0.55, pv=2, depth=18, continuation=[d7d6, c2c3, f8e7, e1g1, ...]}
	Move{lan='a2a4', strength=0.52, pv=3, depth=18, continuation=[c8b7, d2d3, b5b4, b1d2, ...]}
	Move{lan='b1c3', strength=0.36, pv=4, depth=18, continuation=[f8e7, d2d3, d7d6, a2a4, ...]}
	Move{lan='d2d3', strength=0.26, pv=5, depth=18, continuation=[f8c5, b1c3, d7d6, c1g5, ...]}
	Move{lan='d1e2', strength=-0.02, pv=6, depth=18, continuation=[f8c5, a2a4, a8b8, a4b5, ...]}
	Move{lan='c2c3', strength=-0.50, pv=7, depth=18, continuation=[f6e4, e1g1, d7d5, f1e1, ...]}
	Move{lan='b3d5', strength=-0.57, pv=8, depth=18, continuation=[f6d5, e4d5, c6d4, f3d4, ...]}
	Move{lan='h2h3', strength=-0.63, pv=9, depth=18, continuation=[f6e4, e1g1, d7d5, b1c3, ...]}
	Move{lan='f3g5', strength=-0.7, pv=10, depth=18, continuation=[d7d5, d2d3, c6d4, e4d5, ...]}
```
