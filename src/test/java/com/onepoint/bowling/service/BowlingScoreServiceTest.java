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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.onepoint.bowling.domain.BasicRoll;
import com.onepoint.bowling.domain.Game;
import com.onepoint.bowling.domain.GameFrame;
import com.onepoint.bowling.domain.SpecificRoll;

class BowlingScoreServiceTest {

	@Test
	void testFullStrike() {
		BowlingScoreService service = new BowlingScoreService(new FrameFactory());
		Game readGame = service.readGame("X X X X X X X X X XXX"); //$NON-NLS-1$
		List<GameFrame> frames = readGame.getFrames();
		assertEquals(10, frames.size());
		for (int i = 0; i < 9; i++) {
			GameFrame gameFrame = frames.get(i);
			assertSame(frames.get(i + 1), gameFrame.getNextFrame());
			assertEquals(1, gameFrame.getRolls().size());
			assertEquals(SpecificRoll.STRIKE, gameFrame.getRolls().get(0));
		}

		GameFrame gameFrame = frames.get(9);
		assertNull(gameFrame.getNextFrame());
		assertEquals(3, gameFrame.getRolls().size());
		assertEquals(SpecificRoll.STRIKE, gameFrame.getRolls().get(0));
		assertEquals(SpecificRoll.STRIKE, gameFrame.getRolls().get(1));
		assertEquals(SpecificRoll.STRIKE, gameFrame.getRolls().get(2));

		assertEquals(300, service.computeScore(readGame));
	}

	@Test
	void testMultipleFullStrike() {
		BowlingScoreService service = new BowlingScoreService(new FrameFactory());
		List<Game> games = service.readGames(//
				"X X X X X X X X X XXX\n" //$NON-NLS-1$
						+ "X X X X X X X X X XXX" //$NON-NLS-1$
		);
		assertEquals(2, games.size());
		for (Game readGame : games) {
			List<GameFrame> frames = readGame.getFrames();
			assertEquals(10, frames.size());
			for (int i = 0; i < 9; i++) {
				GameFrame gameFrame = frames.get(i);
				assertSame(frames.get(i + 1), gameFrame.getNextFrame());
				assertEquals(1, gameFrame.getRolls().size());
				assertEquals(SpecificRoll.STRIKE, gameFrame.getRolls().get(0));
			}

			GameFrame gameFrame = frames.get(9);
			assertNull(gameFrame.getNextFrame());
			assertEquals(3, gameFrame.getRolls().size());
			assertEquals(SpecificRoll.STRIKE, gameFrame.getRolls().get(0));
			assertEquals(SpecificRoll.STRIKE, gameFrame.getRolls().get(1));
			assertEquals(SpecificRoll.STRIKE, gameFrame.getRolls().get(2));

			assertEquals(300, service.computeScore(readGame));
		}
	}

	@Test
	void testNineAndGutter() {
		BowlingScoreService service = new BowlingScoreService(new FrameFactory());
		Game readGame = service.readGame("9- 9- 9- 9- 9- 9- 9- 9- 9- 9-"); //$NON-NLS-1$
		List<GameFrame> frames = readGame.getFrames();
		assertEquals(10, frames.size());
		for (int i = 0; i < 9; i++) {
			GameFrame gameFrame = frames.get(i);
			assertSame(frames.get(i + 1), gameFrame.getNextFrame());
			assertEquals(2, gameFrame.getRolls().size());
			assertEquals(new BasicRoll(9, false), gameFrame.getRolls().get(0));
			assertEquals(SpecificRoll.GUTTER, gameFrame.getRolls().get(1));
		}

		GameFrame gameFrame = frames.get(9);
		assertNull(gameFrame.getNextFrame());
		assertEquals(2, gameFrame.getRolls().size());
		assertEquals(new BasicRoll(9, false), gameFrame.getRolls().get(0));
		assertEquals(SpecificRoll.GUTTER, gameFrame.getRolls().get(1));

		assertEquals(90, service.computeScore(readGame));
	}

	@Test
	void testFiveAndSpare() {
		BowlingScoreService service = new BowlingScoreService(new FrameFactory());
		Game readGame = service.readGame("5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/5 "); //$NON-NLS-1$
		List<GameFrame> frames = readGame.getFrames();
		assertEquals(10, frames.size());
		for (int i = 0; i < 9; i++) {
			GameFrame gameFrame = frames.get(i);
			assertSame(frames.get(i + 1), gameFrame.getNextFrame());
			assertEquals(2, gameFrame.getRolls().size());
			assertEquals(new BasicRoll(5, false), gameFrame.getRolls().get(0));
			assertEquals(new BasicRoll(5, true), gameFrame.getRolls().get(1));
		}

		GameFrame gameFrame = frames.get(9);
		assertNull(gameFrame.getNextFrame());
		assertEquals(3, gameFrame.getRolls().size());
		assertEquals(new BasicRoll(5, false), gameFrame.getRolls().get(0));
		assertEquals(new BasicRoll(5, true), gameFrame.getRolls().get(1));
		assertEquals(new BasicRoll(5, false), gameFrame.getRolls().get(2));

		assertEquals(150, service.computeScore(readGame));
	}

	@Test
	void errorOnInvalidFrameforScoreComputation() {
		Game g = mock(Game.class);
		GameFrame frame = mock(GameFrame.class);
		doReturn(Arrays.asList(frame)).when(g).getFrames();
		doReturn(Collections.emptyList()).when(frame).getRolls();
		BowlingScoreService service = new BowlingScoreService(new FrameFactory());
		assertThrows(IllegalStateException.class, () -> service.computeScore(g));
	}

}
