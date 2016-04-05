/**
 *  Copyright 2006 Guglielmo Lichtner (lichtner_at_bway_dot_net)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dellroad.nvt4j.impl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.dellroad.nvt4j.Terminal;
import org.dellroad.nvt4j.impl.telnet.DefaultOptionHandler;
import org.dellroad.nvt4j.impl.telnet.FunctionCommand;
import org.dellroad.nvt4j.impl.telnet.OptionCommand;
import org.dellroad.nvt4j.impl.telnet.TelnetCommand;
import org.dellroad.nvt4j.impl.telnet.TelnetCommandException;
import org.dellroad.nvt4j.impl.telnet.TelnetInputStream;
import org.dellroad.nvt4j.impl.telnet.TelnetOption;
import org.dellroad.nvt4j.impl.telnet.TelnetOptionHandler;
import org.dellroad.nvt4j.impl.telnet.TelnetOutputStream;
import org.dellroad.nvt4j.impl.option.EchoOptionHandler;
import org.dellroad.nvt4j.impl.option.LinemodeOptionHandler;
import org.dellroad.nvt4j.impl.option.NawsOptionHandler;
import org.dellroad.nvt4j.impl.option.SuppressGoAheadOptionHandler;

public class TerminalImpl implements Terminal {

    private class OptionStartThread extends Thread {

        private IOException exception;

        public IOException getException() { return exception; }

        public void run() {
            try {
                for (int i = 0; i < optionHandlers.length; i++) {
                    TelnetOptionHandler optionHandler = optionHandlers[i];
                    if (optionHandler != null) {
                        optionHandler.start(out);
                    }
                }
            } catch (IOException exception) {
                this.exception = exception;
            }
        }

    }

    public static final String CLEAR_SCREEN = "\033[2J";
    public static final String CURSOR_OFF = "\033[?25l";
    public static final String CURSOR_ON = "\033[?25h";
    public static final String MOVE = "\033[";
    public static final String AUTO_WRAP_OFF = "\033[?7l";
    public static final String AUTO_WRAP_ON = "\033[?7h";

    private Socket socket;
    private TelnetInputStream in;
    private TelnetOutputStream out;
    private TelnetOptionHandler[] optionHandlers;
    private int handlerQuorum;
    private int readyCount;

    /*
     * Terminal dimensions.
     */
    private int rows;
    private int columns;

    public int getRows() { return rows; }
    public int getColumns() { return columns; }

    public void resize(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public void setCursor(boolean on) throws IOException {
        put(on ? CURSOR_ON : CURSOR_OFF);
    }

    public void clear() throws IOException {
        put(CLEAR_SCREEN);
    }

    public TerminalImpl(Socket socket) throws IOException {
        this(socket.getInputStream(), socket.getOutputStream());
        this.socket = socket;
    }

    public TerminalImpl(InputStream in, OutputStream out) throws IOException {
        this.in = new TelnetInputStream(in);
        this.out = new TelnetOutputStream(out);
        optionHandlers = new TelnetOptionHandler[256];
        TelnetOptionHandler[] tmp = new TelnetOptionHandler[] {
            new SuppressGoAheadOptionHandler(),
            new EchoOptionHandler(),
            new LinemodeOptionHandler(),
            new NawsOptionHandler(this)
        };
        handlerQuorum = tmp.length;
        for (int i = 0; i < tmp.length; i++) {
            optionHandlers[tmp[i].getOption().getCode()] = tmp[i];
        }
        init();
    }

    private void init() throws IOException {
        OptionStartThread optionStarter = new OptionStartThread();
        optionStarter.start();
        while (readyCount < handlerQuorum) {
            try {
                IOException exception = optionStarter.getException();
                if (exception != null) {
                    throw exception;
                }
                if (in.read() == -1)
                    throw new EOFException();
            } catch (TelnetCommandException e) {
                handleCommandException(e);
            }
        }
        clear();
        put(AUTO_WRAP_OFF);
        setCursor(false);
        move(1, 1);
        flush();
    }

    private void handleCommandException(TelnetCommandException e) throws IOException {
        TelnetCommand telnetCommand = e.getTelnetCommand();
        if (telnetCommand instanceof OptionCommand) {
            OptionCommand optionCommand = (OptionCommand) telnetCommand;
            TelnetOption option = ((OptionCommand) telnetCommand).getOption();
            TelnetOptionHandler optionHandler =
                optionHandlers[optionCommand.getOption().getCode()];
            boolean wasReady = optionHandler.isReady();
            optionCommand.execute(optionHandler);
            boolean isReady = optionHandler.isReady();
            if (!wasReady && isReady) {
                ++readyCount;
            } else if (wasReady && !isReady) {
                throw new IOException("Client disabled option: " + option);
            }
        } else {
            throw new IOException("Unsupported telnet option: " + telnetCommand);
        }
    }

    public int get() throws IOException {
        int r;
        while (true) {
            try {
                r = in.read();
                break;
            } catch (TelnetCommandException e) {
                handleCommandException(e);
            }
        }
        return r;
    }

    public int get(byte[] buf) throws IOException {
        return get(buf, 0, buf.length);
    }

    public int get(byte[] buf, int off, int len) throws IOException {
        int r;
        while (true) {
            try {
                r = in.read(buf, off, len);
                break;
            } catch (TelnetCommandException e) {
                handleCommandException(e);
            }
        }
        return r;
    }

    public void put(byte[] buf, int off, int len) throws IOException {
        out.write(buf, off, len);
    }

    public void put(byte[] buf) throws IOException {
        put(buf, 0, buf.length);
    }

    public void put(String s) throws IOException {
        put(s.getBytes());
    }

    public void put(int c) throws IOException {
        put((byte) c);
    }

    public void put(char c) throws IOException {
        put((byte) c);
    }

    public void put(byte c) throws IOException {
        out.write(c);
    }

    public void put(int row, int column, byte[] buf, int off, int len) throws IOException {
        move(row, column);
        out.write(buf, off, len);
    }

    public void put(int row, int column, byte[] buf) throws IOException {
        move(row, column);
        put(buf, 0, buf.length);
    }

    public void put(int row, int column, String s) throws IOException {
        move(row, column);
        put(s.getBytes());
    }

    public void put(int row, int column, int c) throws IOException {
        move(row, column);
        put((byte) c);
    }

    public void put(int row, int column, char c) throws IOException {
        move(row, column);
        put((byte) c);
    }

    public void put(int row, int column, byte c) throws IOException {
        move(row, column);
        out.write(c);
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void move(int row, int column) throws IOException {
        put(MOVE);
        put(String.valueOf(column));
        put(';');
        put(String.valueOf(row));
        put('H');
    }

    public void close() throws IOException {
        if (socket != null) {
            try { socket.close(); } catch (IOException e) {}
        }
        try { in.close(); } catch (IOException e) {}
        try { out.close(); } catch (IOException e) {}
    }

}
