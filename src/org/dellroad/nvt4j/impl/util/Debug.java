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

package org.dellroad.nvt4j.impl.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.CharArrayWriter;
import java.io.BufferedReader;
import java.io.CharArrayReader;

public class Debug {

    private static void print(String s) {
        System.err.println(s);
    }

    public static void trace() {
        String trace[] = getTrace(3);
        print("Trace: " + trace[0] + " " + trace[1]);
    }

    public static void trace(String s) {
        String trace[] = getTrace(3);
        print("Trace: " + trace[0] + " " + trace[1] + " " + s);
    }

    public static String[] getTrace(int level) {
        String[] trace = new String[2];
        try {
            //print stack trace to a char array
            Throwable t = new Throwable();
            CharArrayWriter cw = new CharArrayWriter();
            PrintWriter w = new PrintWriter(cw);
            t.printStackTrace(w);
            w.close();
            //now parse the stack trace
            CharArrayReader cr = new CharArrayReader(cw.toCharArray());
            BufferedReader r = new BufferedReader(cr);
            String line = null;
            while ((line = r.readLine()) != null && level-- > 0);
            r.close();
            //line looks like " at Test.main(Test.java:6)"
            int ap = line.indexOf("at ");
            int lp = line.indexOf('(');
            int rp = line.indexOf(')');
            int cp = line.indexOf(':');
            trace[0] = line.substring(ap + 3, lp);
            trace[1] = line.substring(cp + 1, rp);
        } catch (Exception e) {
            //ignore (method doesn't work with JIT on)
        }
        return trace;
    }

}
