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
	Slow Mover
		SpinEngineOption{name='Slow Mover', defaultValue=100, min=10, max=1000}
	SyzygyProbeDepth
		SpinEngineOption{name='SyzygyProbeDepth', defaultValue=1, min=1, max=100}
	Analysis Contempt
		ComboEngineOption{possibleOptions=null, name='Analysis Contempt', defaultValue=Both}
	nodestime
		SpinEngineOption{name='nodestime', defaultValue=0, min=0, max=10000}
	Syzygy50MoveRule
		CheckEngineOption{name='Syzygy50MoveRule', defaultValue=true}
	SyzygyPath
		StringEngineOption{name='SyzygyPath', defaultValue=<empty>}
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
	UCI_Chess960
		CheckEngineOption{name='UCI_Chess960', defaultValue=false}
	Ponder
		CheckEngineOption{name='Ponder', defaultValue=false}
	EvalFile
		StringEngineOption{name='EvalFile', defaultValue=nn-62ef826d1a6d.nnue}
	UCI_ShowWDL
		CheckEngineOption{name='UCI_ShowWDL', defaultValue=false}
	UCI_Elo
		SpinEngineOption{name='UCI_Elo', defaultValue=1350, min=1350, max=2850}
	Debug Log File
		StringEngineOption{name='Debug Log File', defaultValue=}
	Skill Level
		SpinEngineOption{name='Skill Level', defaultValue=20, min=0, max=20}
	Contempt
		SpinEngineOption{name='Contempt', defaultValue=24, min=-100, max=100}
	Use NNUE
		CheckEngineOption{name='Use NNUE', defaultValue=true}
	Clear Hash
		ButtonEngineOption{name='Clear Hash', defaultValue=}
	SyzygyProbeLimit
		SpinEngineOption{name='SyzygyProbeLimit', defaultValue=7, min=0, max=7}
```

## Setting an option

For changing an option of the engine you can use the following methods:
- `UCIResponse<List<String>> uci.setOption(String optionName, String value, long timeout)`
- `UCIResponse<List<String>> setOption(String optionName, String value)`

For example, modifying the `MultiPV` option to `10` is as simple as:

```java
var uci = new UCI(5000l); // default timeout 5 seconds
uci.startStockfish();
uci.setOption("MultiPV", "10", 3000l).getResultOrThrow(); // custome timeout 3 seconds
uci.close();
```

##
