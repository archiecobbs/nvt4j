
About
-----

The Network Virtual Terminal for Java (NVT4J) is a library which can drive
a terminal emulator connected to the application through a telnet session.

Current Status
--------------

This project was imported as an abandonded orphan from [Google Code](https://code.google.com/p/nvt4j/)
and given a new home here on Github on 17-Nov-2015.

Usage
-----

The API consists of the interface `nvt4j.Terminal`. The implementation of this
class is called `nvt4j.impl.Terminal`. To allow the user to connect to the
application you need to listen for a tcp connection and then wrap a Terminal
object around it:

```
ServerSocket serverSocket = new ServerSocket(8000);
Socket socket = serverSocket.accept();
Terminal terminal = new nvt4j.impl.Terminal(socket);
```

For example, to run the sample class Example.java from an xterm session, you can
do this:

```
user@localhost$ java -classpath nvt4j.jar Example &
user@localhost$ telnet localhost 8000
```

NVT4J uses the Telnet LINEMODE option to put the terminal driver (xterm in this case)
in what the posix standard calls 'raw' or 'non-canonical' mode, so that it can receive
all user inputs asap, instead of waiting for a new-line character.

Unfortunately in pure Java there is no way to put the operating system console in
raw mode. On Linux and similar systems this is done using the stty command, which is
a native program which manipulates the terminal driver in the operating system.

Downloads
---------

See [Downloads](https://github.com/archiecobbs/nvt4j/wiki/Downloads).

Documentation
-------------

View the [Javadoc API](http://archiecobbs.github.io/nvt4j/api/index.html).

License
-------

See the [LICENSE](https://github.com/archiecobbs/nvt4j/blob/master/LICENSE) file.

Copyright 2006 Guglielmo Lichtner
Copyright 2015 Archie L. Cobbs

