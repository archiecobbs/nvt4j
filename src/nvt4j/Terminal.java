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

package nvt4j;

import java.io.IOException;

/**
 * This interface is used by the application to read characters from- and
 * to send characters to, the terminal emulator.
 */
public interface Terminal {

    /**
     * Turns the terminal emulator's cursor on or off.
     */
    public void setCursor(boolean on) throws IOException;

    /**
     * Clears the screen.
     */
    public void clear() throws IOException;

    /**
     * Returns the number of rows in the terminal emulator window.
     * If the window is resized this value is updated accordingly.
     */
    public int getRows();

    /**
     * Returns the number of columns in the terminal emulator window.
     * If the window is resized this value is updated accordingly.
     */
    public int getColumns();

    /**
     * Returns the next character sent by the terminal, or -1 if the stream
     * is closed. The terminal is in 'raw' mode, not line-buffered mode.
     * This method blocks until a character is available.
     */
    public int get() throws IOException;

    /**
     * Reads some characters from the terminal and writes them to buffer 'buf'
     * starting from position 0, and returns the number of characters read,
     * or -1 if the stream is closed. This method blocks until a character is
     * available. The terminal is in 'raw' mode, not line-buffered mode.
     */
    public int get(byte[] buf) throws IOException;

    /**
     * Reads up to 'len' characters from the terminal and writes them to buffer 'buf'
     * starting from position 'off', and returns the number of characters read,
     * or -1 if the stream is closed. This method blocks until a character is available.
     * The terminal is in 'raw' mode, not line-buffered mode.
     */
    public int get(byte[] buf, int off, int len) throws IOException;

    /**
     * Writes 'len' characters to the terminal, starting from position 'off'.
     */
    public void put(byte[] buf, int off, int len) throws IOException;

    /**
     * Writes buffer 'buf' to the terminal.
     */
    public void put(byte[] buf) throws IOException;

    /**
     * Writes a string to the terminal. The characters are extracted
     * from the string using the method String.getBytes(), which translates
     * each (2-byte) unicode character to a byte.
     *
     * @throws IllegalArgumentException if the end of string would be outside the window
     */
    public void put(String s) throws IOException;

    /**
     * Writes one character to the terminal. The int is converted into a byte
     * by casting.
     */
    public void put(int c) throws IOException;

    /**
     * Writes one character to the terminal. The char is converted into a byte
     * by casting.
     */
    public void put(char c) throws IOException;

    /**
     * Writes one character to the terminal.
     */
    public void put(byte c) throws IOException;

    /**
     * Moves the cursor and writes 'len' characters to the terminal,
     * starting from position 'off'.
     *
     * @throws IllegalArgumentException if the coordinates are out of range
     */
    public void put(int row, int column, byte[] buf, int off, int len) throws IOException;

    /**
     * Moves the cursor and writes buffer 'buf' to the terminal.
     *
     * @throws IllegalArgumentException if the coordinates are out of range
     */
    public void put(int row, int column, byte[] buf) throws IOException;

    /**
     * Moves the cursor and writes a string to the terminal. The characters are extracted
     * from the string using the method String.getBytes(), which translates
     * each (2-byte) unicode character to a byte.
     *
     * @throws IllegalArgumentException if the coordinates are out of range
     * @throws IllegalArgumentException if the end of string would be outside the window
     */
    public void put(int row, int column, String s) throws IOException;

    /**
     * Moves the cursor and writes one character to the terminal.
     * The int is converted into a byte by casting.
     *
     * @throws IllegalArgumentException if the coordinates are out of range
     */
    public void put(int row, int column, int c) throws IOException;

    /**
     * Moves the cursor and writes one character to the terminal.
     * The char is converted into a byte by casting.
     *
     * @throws IllegalArgumentException if the coordinates are out of range
     */
    public void put(int row, int column, char c) throws IOException;

    /**
     * Moves the cursor and writes one character to the terminal.
     *
     * @throws IllegalArgumentException if the coordinates are out of range
     */
    public void put(int row, int column, byte c) throws IOException;

    /**
     * Sends all characters in the buffer (if any) to the terminal.
     */
    public void flush() throws IOException;

    /**
     * Sends the terminal emulator a command to move the cursor
     * to the given row and column. The row must be between 1 and
     * the value of getRows(), while the column must be between 1
     * and the value of getColumn().
     *
     * @throws IllegalArgumentException if the coordinates are out of range
     */
    public void move(int row, int column) throws IOException;

    /**
     * Closes the terminal.
     */
    public void close() throws IOException;

}
