/*
BSD 3-Clause License

Copyright (c) 2017, Paweł Pałucha
Copyright (c) 2008, Atomique.net
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
3-Clause License

Copyright (c) 2017, Paweł Pałucha
Copyright (c) 2008, Atomique.net
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package net.atomique.ksar.Parser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pawel
 */


public class LinuxTest {
    
    public LinuxTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of guessDateFormat method, of class Linux.
     */
    @Test
    public void testGuessDateFormat() {
        System.out.println("guessDateFormat");
        
        Linux instance = new Linux();
        String dt = "2017-01-02";
        String expResult = "yy-MM-dd";
        String result = instance.guessDateFormat(dt);
        assertEquals(expResult, result);
        
        instance = new Linux();
        dt = "28/02/2017";
        expResult = "dd/MM/yy";
        result = instance.guessDateFormat(dt);
        assertEquals(expResult, result);
        
        instance = new Linux();
        dt = "02/28/2017";
        expResult = "MM/dd/yy";
        result = instance.guessDateFormat(dt);
        assertEquals(expResult, result);
        
        instance = new Linux();
        dt = "02/01/2017";
        expResult = "MM/dd/yy";
        result = instance.guessDateFormat(dt);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of parse_header method, of class Linux.
     */
    @Test
    public void testParse_header() {
        System.out.println("parse_header");
        String s = "Linux 3.10.0-514.2.2.el7.x86_64 (prod.test.org) 	2017-01-14 	_x86_64_	(8 CPU)";
        Linux instance = new Linux();
        instance.parse_header(s);
    }

    /**
     * Test of askDateFormat method, of class Linux.
     */
    @Test
    public void testAskDateFormat() {
        System.out.println("askDateFormat");
        String title = "";
        String dt = "";
        Linux instance = new Linux();
        String expResult = null;
        String result = instance.askDateFormat(title, dt);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of parse method, of class Linux.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String line = "";
        String[] columns = null;
        Linux instance = new Linux();
        int expResult = 0;
        //TODO
        //int result = instance.parse(line, columns);
        //assertEquals(expResult, result);
        
    }
    
}
