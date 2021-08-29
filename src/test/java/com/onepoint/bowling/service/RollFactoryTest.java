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
package com.onepoint.bowling.service;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.onepoint.bowling.domain.BasicRoll;
import com.onepoint.bowling.domain.Roll;
import com.onepoint.bowling.domain.SpecificRoll;

class RollFactoryTest {

	@ParameterizedTest
	@MethodSource("getValidParsingData")

	void testValidInput(String input, List<Roll> expectedRolls) {
		FrameFactory factory = new FrameFactory();
		List<Roll> createRolls = factory.createFrame(null, input).getRolls();
		assertIterableEquals(expectedRolls, createRolls);
	}

	private static Stream<Arguments> getValidParsingData() {
		return Stream.of(//
		// @formatter:off
				Arguments.of("X", Arrays.asList(SpecificRoll.STRIKE)), //
Arguments.of("13", Arrays.asList(new BasicRoll(1, false), new BasicRoll(3, false))), //
Arguments.of("1/", Arrays.asList(new BasicRoll(1, false), new BasicRoll(9, true))), //
Arguments.of("-/", Arrays.asList(SpecificRoll.GUTTER, new BasicRoll(10, true))), //
Arguments.of("-3", Arrays.asList(SpecificRoll.GUTTER, new BasicRoll(3, false))), //
Arguments.of("--", Arrays.asList(SpecificRoll.GUTTER, SpecificRoll.GUTTER)), //
Arguments.of("2-", Arrays.asList(new BasicRoll(2, false), SpecificRoll.GUTTER)), //
Arguments.of("1/3", Arrays.asList(new BasicRoll(1, false), new BasicRoll(9, true), new BasicRoll(3, false))), //
Arguments.of("1/X", Arrays.asList(new BasicRoll(1, false), new BasicRoll(9, true), SpecificRoll.STRIKE)), //
Arguments.of("XXX", Arrays.asList(SpecificRoll.STRIKE,SpecificRoll.STRIKE,SpecificRoll.STRIKE)), //
Arguments.of("XX-", Arrays.asList(SpecificRoll.STRIKE,SpecificRoll.STRIKE,SpecificRoll.GUTTER)), //
Arguments.of("X-/", Arrays.asList(SpecificRoll.STRIKE,SpecificRoll.GUTTER,new BasicRoll(10, true))), //
Arguments.of("X2/", Arrays.asList(SpecificRoll.STRIKE,new BasicRoll(2, false),new BasicRoll(8, true))), //
Arguments.of("X--", Arrays.asList(SpecificRoll.STRIKE,SpecificRoll.GUTTER,SpecificRoll.GUTTER)) //
		// @formatter:on
		);
	}

	@ParameterizedTest
	@MethodSource("getInvalidParsingData")
	void testInvalidInput(String input) {
		FrameFactory factory = new FrameFactory();
		assertThrows(IllegalArgumentException.class, () -> factory.createFrame(null, input));
	}

	private static Stream<Arguments> getInvalidParsingData() {
		return Stream.of(//
				Arguments.of(""), //
				Arguments.of("1"), //
				Arguments.of("X1"), //
				Arguments.of("1X"), //
				Arguments.of("/1"), //
				Arguments.of("1//"), //
				Arguments.of("1X1"), //
				Arguments.of("XX/"), //
				Arguments.of("X1X")//
		);
	}

}
