
Copyright 2006 Guglielmo Lichtner (lichtner_at_bway_dot_net)

Current Status
--------------

This project was imported from [Google Code](https://code.google.com/p/nvt4j/) to Github on Tue Nov 17 2015.

[Javadoc API](http://archiecobbs.github.io/nvt4j/api/index.html)

License
-------

See the file called LICENSE in this directory.

About
-----

The Network Virtual Terminal for Java (NVT4J) is a library which can drive
a terminal emulator connected to the application through a telnet session.

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

