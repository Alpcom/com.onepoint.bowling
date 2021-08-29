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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import com.onepoint.bowling.domain.Game;
import com.onepoint.bowling.service.BowlingScoreService;
import com.onepoint.bowling.service.FrameFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;

/**
 * 
 * Application main class
 * 
 * @author aleprevost
 *
 */
@Command(name = "treasure", version = "1.0", mixinStandardHelpOptions = true)
public class BowlingCli implements Callable<Integer> {

	private static final String NO_DATA_PROVIDE = "please provide data"; //$NON-NLS-1$
	private static final String ALL_ARGs_FILL = "please provide only one source"; //$NON-NLS-1$

	private static final Logger LOGGER = LoggerFactory.getLogger(BowlingCli.class);

	@Option(names = { "-f", "--input-file-path" }, description = "path to file to read")
	Path inputFilePath;
	@Option(names = { "-d", "--input-data" }, description = "data to read")
	String data;

	@Override
	public Integer call() throws IOException {
		if (this.inputFilePath == null && data == null) {
			LOGGER.error(NO_DATA_PROVIDE);
			return 3;
		}
		if (this.inputFilePath != null && data != null) {
			LOGGER.error(ALL_ARGs_FILL);
			return 3;
		}
		String toParse = data;
		if (data == null) {
			toParse = String.join(System.lineSeparator(), Files.readAllLines(inputFilePath));
		}
		BowlingScoreService service = new BowlingScoreService(new FrameFactory());
		List<Game> readGames = service.readGames(toParse);
		for (Game g : readGames) {
			service.computeScore(g, i -> LOGGER.warn("{}", i));
		}
		return 0;
	}

	/**
	 * main method call at start up
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		System.exit(createCommandLine().execute(args));
	}

	static CommandLine createCommandLine() {
		var command = new BowlingCli();
		var commandLine = new CommandLine(command);
		commandLine.setExecutionExceptionHandler(command::handleExecutionException);
		return commandLine;
	}

	int handleExecutionException(Exception ex, CommandLine commandLine, ParseResult parseResult) throws Exception {
		LOGGER.error("An Unknown error occurs"); //$NON-NLS-1$
		LOGGER.debug("Cause :", ex); //$NON-NLS-1$
		return 4;
	}
}
