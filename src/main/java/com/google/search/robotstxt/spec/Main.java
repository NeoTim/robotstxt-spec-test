// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.search.robotstxt.spec;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "testRunner")
public class Main implements Callable<Integer> {

  @CommandLine.Option(
      names = "--command",
      required = true,
      description = "The command that runs the parser")
  public String callParserCommand;

  @CommandLine.Option(
      names = "--userTestDir",
      description = "The path to the directory that contains the user's test files")
  public String myTestsDir = null;

  @CommandLine.Option(names = "--outputType", description = "The format that the parser uses")
  public OutputType outputType = OutputType.EXITCODE;

  @CommandLine.Option(names = "--allowedPattern", description = "The pattern used for -allowed-")
  public String allowedPattern = "0";

  @CommandLine.Option(
      names = "--disallowedPattern",
      description = "The pattern used for -disallowed-")
  public String disallowedPattern = "1";

  public static void main(String[] args) throws IOException, InterruptedException {
    int exitCode = new CommandLine(new Main()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    CMDArgs cmdArgs =
        new CMDArgs(callParserCommand, myTestsDir, outputType, allowedPattern, disallowedPattern);

    try {
      cmdArgs.validateArguments();
    } catch (IllegalArgumentException e) {
      System.err.println(e.toString());
      return 1;
    }

    // Decide the output type
    ParserMatcher parserMatcher;
    if (cmdArgs.getMode() == OutputType.PRINTING) {
      parserMatcher = new PrintingParserMatcher();
    } else {
      parserMatcher = new ExitcodeParserMatcher();
    }

    ProtoParser protoParser = new ProtoParser();
    TestsResult result = new TestsResult();

    // Read and run the Compliance Test Cases
    List<TestInfo> complianceTestCases = protoParser.readMessages("/CTC");
    new ComplianceTestRunner().runTests(complianceTestCases, parserMatcher, cmdArgs, result);

    // Read and run the User Test Cases
    if (cmdArgs.getMyTestsDir() != null) {
      List<TestInfo> userTestCases = protoParser.readMessages(cmdArgs.getMyTestsDir());
      new UserTestRunner().runTests(userTestCases, parserMatcher, cmdArgs, result);
    }

    System.out.println(result);
    return 0;
  }
}
