/**
 * MIT License
 *
 * Copyright (c) 2021 Le Prevost-Corvellec Arnault
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.onepoint.bowling.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import picocli.CommandLine;

class BowlingCliTest {

	@Test
	void testInputDataParam() throws IOException {
		var exitCode = BowlingCli.createCommandLine().execute("-d=X X X X X X X X X XXX"); //$NON-NLS-1$
		assertEquals(0, exitCode);
	}

	@ParameterizedTest
	@MethodSource("testFailArgs")
	void testFail(String score) throws IOException {
		var exitCode = BowlingCli.createCommandLine().execute("-d=" + score); //$NON-NLS-1$
		assertEquals(4, exitCode);
	}

	@Test
	void testNoArg() throws IOException {
		var exitCode = BowlingCli.createCommandLine().execute(); // $NON-NLS-1$
		assertEquals(3, exitCode);
	}

	@Test
	void testToMuchArg() throws IOException {
		var exitCode = BowlingCli.createCommandLine().execute("-d=arg1","-f=arg2"); //$NON-NLS-1$
		assertEquals(3, exitCode);
	}

	private static Stream<Arguments> testFailArgs() {
		return Stream.of(//
				Arguments.of("X X X X X X X X XXX XXX"), //
				Arguments.of("X X X X X X X X XXX X"), //
				Arguments.of("X X X X X X X X X X1"), //
				Arguments.of("X1 X X X X X X X X X1"), //
				Arguments.of("X X X X X X X X X X"), //
				Arguments.of("89 X X X X X X X XXX")//
		);
	}
}
