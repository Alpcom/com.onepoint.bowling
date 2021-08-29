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
package com.onepoint.bowling.domain;

import java.util.Collections;
import java.util.List;

public class GameFrame {

	private final List<Roll> rolls;
	private final boolean isFinal;
	private final boolean isNonFinal;
	private GameFrame nextFrame;

	public GameFrame(GameFrame previousFrame, List<Roll> rolls, boolean isFinal, boolean isNonFinal) {
		if (previousFrame != null) {
			previousFrame.setNextFrame(this);
		}
		this.isFinal = isFinal;
		this.isNonFinal = isNonFinal;
		this.rolls = Collections.unmodifiableList(rolls);
	}

	private void setNextFrame(GameFrame gameFrame) {
		if (this.nextFrame == null) {
			this.nextFrame = gameFrame;
		} else {
			throw new IllegalStateException("Game frame can only have one next frame");
		}
	}

	public GameFrame getNextFrame() {
		return nextFrame;
	}

	public List<Roll> getRolls() {
		return rolls;
	}

	public boolean isNonFinalFrame() {
		return isNonFinal;
	}

	public boolean isFinalFrame() {
		return isFinal;
	}

	@Override
	public String toString() {
		return "GameFrame [rolls=" + rolls + ", isFinal=" + isFinal + ", isNonFinal=" + isNonFinal + "]";
	}

}
