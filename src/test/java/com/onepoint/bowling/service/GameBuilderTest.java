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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.onepoint.bowling.domain.Game;
import com.onepoint.bowling.domain.GameFrame;
import com.onepoint.bowling.domain.Roll;

class GameBuilderTest {

	@Test
	void testOnUniqueFrame() {
		String frameString = "someFrame";
		FrameFactory rollFact = mock(FrameFactory.class);
		GameFrame frame = mock(GameFrame.class);
		doReturn(frame).when(rollFact).createFrame(any(), eq(frameString));
		doReturn(frame).when(rollFact).createFrame(any(), eq("--"));

		doReturn(true).when(frame).isFinalFrame();
		doReturn(true).when(frame).isNonFinalFrame();
		GameBuilder objToTest = new GameBuilder(rollFact);
		objToTest.addFrame(frameString);
		Game build = objToTest.completeAndBuild();
		assertEquals(10, build.getFrames().size());
		GameFrame newlyCreatedFrame = build.getFrames().get(0);
		assertSame(frame, newlyCreatedFrame);
		assertNull(newlyCreatedFrame.getNextFrame());
	}

	@Test
	void testOnMultipleFrame() {
		String frameString = "someFrame";
		FrameFactory rollFact = mock(FrameFactory.class);
		GameFrame frame = mock(GameFrame.class);
		doReturn(frame).when(rollFact).createFrame(any(), eq(frameString));
		doReturn(true).when(frame).isFinalFrame();
		doReturn(true).when(frame).isNonFinalFrame();
		GameBuilder objToTest = new GameBuilder(rollFact);
		for (int i = 0; i < 10; i++) {
			objToTest.addFrame(frameString);
		}
		Game build = objToTest.build();
		assertEquals(10, build.getFrames().size());
		for (int i = 0; i < 10; i++) {
			assertSame(frame, build.getFrames().get(0));
		}
	}

}
