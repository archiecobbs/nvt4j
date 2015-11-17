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

package nvt4j.impl.telnet;

public class TelnetOption {

    public static TelnetOption ECHO =
	new TelnetOption(false, 1, "ECHO");

    public static TelnetOption LINEMODE =
	new TelnetOption(true, 34, "LINEMODE");

    public static TelnetOption NAWS =
	new TelnetOption(true, 31, "NAWS");

    public static TelnetOption SUPPRESS_GO_AHEAD =
	new TelnetOption(false, 3, "SUPPRESS-GO-AHEAD");

    public static TelnetOption TERMINAL_TYPE =
	new TelnetOption(true, 24, "TERMINAL-TYPE");

    private static TelnetOption[] options;

    static {
	options = new TelnetOption[256];
	options[ECHO.getCode()] = ECHO;
	options[LINEMODE.getCode()] = LINEMODE;
	options[NAWS.getCode()] = NAWS;
	options[SUPPRESS_GO_AHEAD.getCode()] = SUPPRESS_GO_AHEAD;
	options[TERMINAL_TYPE.getCode()] = TERMINAL_TYPE;
    }

    public static TelnetOption getOption(int code) {
	TelnetOption option = options[code];
	if (option == null) {
	    throw new RuntimeException("No option for code: " + code);
	}
	return option;
    }

    private boolean complex;
    private int code;
    private String name;

    public boolean isComplex() { return complex; }
    public int getCode() { return code; }
    public String getName() { return name; }
    
    private TelnetOption(boolean complex,
			 int code,
			 String name) {
	this.complex = complex;
	this.code = code;
	this.name = name;
    }

    public String toString() {
	return name;
    }

}
