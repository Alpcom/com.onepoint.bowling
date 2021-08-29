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

import java.util.ArrayList;
import java.util.List;

import com.onepoint.bowling.domain.Game;
import com.onepoint.bowling.domain.GameFrame;
import com.onepoint.bowling.domain.Roll;

class GameBuilder {

	private final ArrayList<GameFrame> frames;
	private final FrameFactory rollFactory;

	private GameFrame lastBuildFrame = null;

	GameBuilder(FrameFactory rollFactory) {
		this.rollFactory = rollFactory;
		this.frames = new ArrayList<>();

	}

	GameBuilder addFrame(String rolls) {
		lastBuildFrame = rollFactory.createFrame(lastBuildFrame, rolls);
		frames.add(lastBuildFrame);
		return this;
	}

	Game build() {
		frames.trimToSize();
		checkGameConsistence();
		return new Game(frames);
	}

	private void checkGameConsistence() {
		if (frames.size() != 10) {
			throw new IllegalArgumentException("Game should contains 10 frames");
		}
		for (int i = 0; i < 9; i++) {
			if (!frames.get(i).isNonFinalFrame()) {
				throw new IllegalArgumentException(
						String.format("%s is not a valid frame for index %d", frames.get(i), i));
			}
		}
		if (!frames.get(9).isFinalFrame()) {
			throw new IllegalArgumentException(String.format("%s is not a valid frame for last frame.", frames.get(9)));
		}
	}

	public Game completeAndBuild() {
		for (int i = frames.size(); i < 10; i++) {
			addFrame("--");
		}
		return build();
	}

}
