/*
 * FindBugs4JUnit. Copyright (c) 2011 youDevise, Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
*/
package com.youdevise.fbplugins;

import static java.lang.String.format;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import edu.umd.cs.findbugs.BugInstance;

public class BugInstanceMatchers {

    static class BugInstanceTypeMatcher extends BaseMatcher<BugInstance> {

        private final String bugType;

        private BugInstanceTypeMatcher(String bugType) {
            this.bugType = bugType;
        }
        
        @Override
        public boolean matches(Object obj) {
            if(! (obj instanceof BugInstance)) {
                return false;
            }
            BugInstance bug = (BugInstance) obj;
            
            return bugType.equals(bug.getType());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(format("with bug of type '%s'", bugType));
        }
        
    }
    
    public static Matcher<BugInstance> hasType(String bugType) {
        return new BugInstanceTypeMatcher(bugType);
    }
    
}
