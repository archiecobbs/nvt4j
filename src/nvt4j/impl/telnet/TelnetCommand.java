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

public abstract class TelnetCommand {

    public static final int SE = 240;
    public static final int NOP = 241;
    public static final int DM = 242;
    public static final int BRK = 243;
    public static final int IP = 244;
    public static final int AO = 245;
    public static final int AYT = 246;
    public static final int EC = 247;
    public static final int EL = 248;
    public static final int GA = 249;
    public static final int SB = 250;
    public static final int WILL = 251;
    public static final int WONT = 252;
    public static final int DO = 253;
    public static final int DONT = 254;
    public static final int IAC = 255;

    private int position;

    public int getPosition() { return position; }

    protected TelnetCommand(int position) {
        this.position = position;
    }

    public static String toString(byte c) {
        return toString(0xFF & c);
    }

    public static String toString(int c) {
        String s;
        switch (c) {
        case SE:
            s = "SE";
            break;
        case NOP:
            s = "NOP";
            break;
        case DM:
            s = "DM";
            break;
        case BRK:
            s = "BRK";
            break;
        case IP:
            s = "IP";
            break;
        case AO:
            s = "AO";
            break;
        case AYT:
            s = "AYT";
            break;
        case EC:
            s = "EC";
            break;
        case EL:
            s = "EL";
            break;
        case GA:
            s = "GA";
            break;
        case SB:
            s = "SB";
            break;
        case WILL:
            s = "WILL";
            break;
        case WONT:
            s = "WONT";
            break;
        case DO:
            s = "DO";
            break;
        case DONT:
            s = "DONT";
            break;
        case IAC:
            s = "IAC";
            break;
        default:
            s = String.valueOf(c);
            break;
        }
        return s;
    }

}
