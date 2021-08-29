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

import java.util.Objects;

public class BasicRoll implements Roll {
	private final int nbOfhitPins;
	private final boolean isSpare;

	public BasicRoll(int nbOfhitPins, boolean isSpare) {
		this.nbOfhitPins = nbOfhitPins;
		this.isSpare = isSpare;
	}

	@Override
	public boolean isSpare() {
		return isSpare;
	}

	@Override
	public int getHitsPins() {
		return nbOfhitPins;
	}

	@Override
	public String toString() {
		if (isSpare) {
			return "Spare : " + nbOfhitPins;
		} else {
			return "Hit : " + nbOfhitPins;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(isSpare, nbOfhitPins);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicRoll other = (BasicRoll) obj;
		return isSpare == other.isSpare && nbOfhitPins == other.nbOfhitPins;
	}
}
