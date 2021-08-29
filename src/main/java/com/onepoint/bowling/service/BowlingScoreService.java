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

import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.onepoint.bowling.domain.Game;
import com.onepoint.bowling.domain.GameFrame;
import com.onepoint.bowling.domain.Roll;

public class BowlingScoreService {

	private static class ScoreSummer implements IntConsumer {

		private IntConsumer scoreLogger;
		private int score;

		public ScoreSummer(IntConsumer scoreLogger) {
			this.scoreLogger = scoreLogger;
			
		}

		@Override
		public void accept(int value) {
			this.score+=value;
			scoreLogger.accept(score);
		}

		int getScore() {
			return this.score;
		}
	}

	private final FrameFactory rollFactory;

	public BowlingScoreService(FrameFactory rollFactory) {
		this.rollFactory = rollFactory;
	}

	public List<Game> readGames(String inputData) {
		return Stream.of(inputData.split("\\R")) // //$NON-NLS-1$
				.map(String::trim) //
				.filter(Predicate.not(String::isEmpty))//
				.map(this::readGame)//
				.collect(Collectors.toList());
	}

	public Game readGame(String inputData) {
		GameBuilder builder = new GameBuilder(rollFactory);
		Stream.of(inputData.trim().split(" ")).forEach(builder::addFrame); //$NON-NLS-1$
		return builder.build();
	}

	public int computeScore(Game game) {
		return computeScore(game, i -> {
		});
	}

	public int computeScore(Game game, IntConsumer scoreLogger) {
		ScoreSummer scoreSummer = new ScoreSummer(scoreLogger);
		game.getFrames().stream()//
				.mapToInt(this::computeFrameScore) //
				.forEachOrdered(scoreSummer);
		return scoreSummer.getScore();
	}

	private int computeFrameScore(GameFrame frame) {
		List<Roll> rolls = frame.getRolls();
		if (rolls.size() == 1) {
			GameFrame nextFrame = frame.getNextFrame();
			List<Roll> nextFrameRolls = nextFrame.getRolls();
			if (nextFrameRolls.size() == 1) {
				GameFrame nextNextFrame = frame.getNextFrame();
				List<Roll> nextNextFrameRolls = nextNextFrame.getRolls();
				return 20 + nextNextFrameRolls.get(0).getHitsPins();
			} else {
				return 10 + nextFrameRolls.get(0).getHitsPins() + nextFrameRolls.get(1).getHitsPins();
			}
		} else if (rolls.size() == 2) {
			Roll firstRoll = rolls.get(0);
			Roll secondRoll = rolls.get(1);
			int result = firstRoll.getHitsPins() + secondRoll.getHitsPins();
			if (secondRoll.isSpare()) {
				GameFrame nextFrame = frame.getNextFrame();
				result += nextFrame.getRolls().get(0).getHitsPins();
			}
			return result;
		} else if (rolls.size() == 3) {
			return rolls.stream().mapToInt(Roll::getHitsPins).reduce(0, (i, j) -> i + j);
		}
		throw new IllegalStateException();
	}
}
