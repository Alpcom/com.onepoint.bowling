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

import javax.swing.text.StyledEditorKit.BoldAction;

import com.onepoint.bowling.domain.BasicRoll;
import com.onepoint.bowling.domain.GameFrame;
import com.onepoint.bowling.domain.Roll;
import com.onepoint.bowling.domain.SpecificRoll;

public class FrameFactory {
	private static final int NETHER_FINAL_OR_NON_FINAL = 0;
	private static final int ONLY_FINAL = 1;
	private static final int ONLY_NON_FINAL = 2;
	private static final int FINAL_AND_NON_FINAL = 3;

	/**
	 * 
	 * @param output
	 * @param rolls
	 * @return paar of boolean first is non final candidate and the seconde final
	 *         candidate
	 */
	private int checkOutPutConsistance(ArrayList<Roll> output, String rolls) {
		if (output.size() == 1) {
			Roll first = output.get(0);
			// strike
			if (!isStrike(first)) {
				return NETHER_FINAL_OR_NON_FINAL;
			}
			return ONLY_NON_FINAL;
		} else if (output.size() == 2) {
			Roll first = output.get(0);
			// basic basic
			// basic gutter
			// basic spare --> non final
			// gutter basic
			// gutter gutter
			// gutter spare --> non final
			// -----> (basic | gutter) + !strike
			if (!isBasicOrGutter(first)) {
				return NETHER_FINAL_OR_NON_FINAL;
			}
			Roll second = output.get(1);
			if (isStrike(second)) {
				return NETHER_FINAL_OR_NON_FINAL;
			}
			if (isSpare(second)) {
				return ONLY_NON_FINAL;
			} else {
				return FINAL_AND_NON_FINAL;
			}
		} else if (output.size() == 3) {
			Roll first = output.get(0);
			Roll second = output.get(1);
			Roll third = output.get(2);
			// one of spare case:
			// basic spare gutter
			// basic spare basic
			// basic spare strike
			// gutter spare gutter
			// gutter spare basic
			// gutter spare strike
			// -----> (basic | gutter) + spare + !spare
			boolean oneOfSpareCase = isBasicOrGutter(first) && isSpare(second) && !isSpare(third);
			// one of double strike case :
			// strike strike gutter
			// strike strike basic
			// strike strike strike
			// -----> strike + strike + !spare
			boolean oneOfdoubleStrikeCase = isStrike(first) && isStrike(second) && !isSpare(third);
			// one of simple strike case :
			// strike basic gutter
			// strike basic basic
			// strike basic spare
			// strike gutter gutter
			// strike gutter basic
			// strike gutter spare
			// -----> strike + (basic | gutter) + !strike
			boolean oneOfSimpleStrikeCase = isStrike(first) && isBasicOrGutter(second) && !isStrike(third);
			if (!(oneOfSpareCase || oneOfdoubleStrikeCase || oneOfSimpleStrikeCase)) {
				return NETHER_FINAL_OR_NON_FINAL;
			}
			return ONLY_FINAL;
		} else {
			return NETHER_FINAL_OR_NON_FINAL;
		}
	}

	private boolean isStrike(Roll roll) {
		return roll == SpecificRoll.STRIKE;
	}

	private boolean isSpare(Roll roll) {
		return roll.isSpare();
	}

	private boolean isBasicOrGutter(Roll roll) {
		return ((roll instanceof BasicRoll) && !roll.isSpare()) || (roll == SpecificRoll.GUTTER);
	}

	private Roll createRoll(char c, int last) {
		switch (c) {
		case '/':
			return new BasicRoll(10 - last, true);
		case 'X':
			return SpecificRoll.STRIKE;
		case '-':
			return SpecificRoll.GUTTER;
		default:
			return new BasicRoll(Integer.valueOf(Character.toString(c)), false);
		}
	}

	public GameFrame createFrame(GameFrame lastBuildFrame, String rolls) {
		ArrayList<Roll> output = new ArrayList<>(3);
		int last = 0;
		for (char c : rolls.toCharArray()) {
			Roll createRoll = createRoll(c, last);
			last = createRoll.getHitsPins();
			output.add(createRoll);
		}
		output.trimToSize();
		int consistance = checkOutPutConsistance(output, rolls);
		switch (consistance) {
		case FINAL_AND_NON_FINAL:
			return new GameFrame(lastBuildFrame, output, true, true);
		case ONLY_FINAL:
			return new GameFrame(lastBuildFrame, output, true, false);
		case ONLY_NON_FINAL:
			return new GameFrame(lastBuildFrame, output, false, true);
		case NETHER_FINAL_OR_NON_FINAL:
		default:
			throw new IllegalArgumentException(String.format("% is not a valid frame.", rolls));
		}
	}

}
