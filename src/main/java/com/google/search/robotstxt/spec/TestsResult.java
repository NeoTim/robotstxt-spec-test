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

import com.google.search.robotstxt.spec.ParserMatcher.TestOutcome;
import com.google.search.robotstxt.spec.specification.SpecificationProtos;

/** Holds the results of the tests performed */
public class TestsResult {
  private int totalNumberComplianceTests;
  private int totalNumberUserTests;
  private int totalNumberGoogleTests;
  private int currentNumberComplianceTests;
  private int currentNumberUserTests;
  private int currentNumberGoogleTests;
  private int numberFailedComplianceTests;
  private int numberFailedUserTests;
  private int numberFailedGoogleTests;

  /** Default constructor */
  public TestsResult() {}

  public void setTotalNumberComplianceTests(int totalNumberComplianceTests) {
    this.totalNumberComplianceTests = totalNumberComplianceTests;
  }

  public void setTotalNumberGoogleTests(int totalNumberGoogleTests) {
    this.totalNumberGoogleTests = totalNumberGoogleTests;
  }

  public void setTotalNumberUserTests(int totalNumberUserTests) {
    this.totalNumberUserTests = totalNumberUserTests;
  }

  /** Reports the success of one Complience Test */
  public void reportSuccessComplianceTests(TestInfo passedTest) {
    if (passedTest.getTestType() == SpecificationProtos.TestType.GOOGLE_SPECIFIC) {
      this.currentNumberGoogleTests++;
      System.out.println(
          "GOOGLE TEST #" + currentNumberGoogleTests + "/" + totalNumberGoogleTests + " PASSED");
    } else {
      this.currentNumberComplianceTests++;
      System.out.println(
          "COMPLIANCE TEST #"
              + currentNumberComplianceTests
              + "/"
              + totalNumberComplianceTests
              + " PASSED");
    }
  }

  /** Reports the success of one user test */
  public void reportSuccessUserTests() {
    this.currentNumberUserTests++;
    System.out.println(
        "USER TEST #" + currentNumberUserTests + "/" + totalNumberUserTests + " PASSED");
  }

  /**
   * Adds the failure of one Compliance Test
   *
   * @param failedTest The info about the failed test
   * @param testOutcome The outcome of the parser
   */
  public void reportFailureComplianceTests(TestInfo failedTest, TestOutcome testOutcome) {
    System.out.println("---------------------------------");
    if (failedTest.getTestType().equals(SpecificationProtos.TestType.GOOGLE_SPECIFIC)) {
      this.currentNumberUserTests++;
      this.numberFailedGoogleTests++;
      System.out.println(
          "GOOGLE TEST #" + currentNumberGoogleTests + "/" + totalNumberGoogleTests + " FAILED");
    } else {
      this.currentNumberComplianceTests++;
      this.numberFailedComplianceTests++;
      System.out.println(
          "COMPLIANCE TEST #"
              + currentNumberComplianceTests
              + "/"
              + totalNumberComplianceTests
              + " FAILED");
    }

    System.out.println("Test file path: " + failedTest.getTestFilePath());
    System.out.println(failedTest.toString());
    System.out.println("Your outcome: " + testOutcome.outcome().toString());
    System.out.println();
    System.out.println("Exit code: " + testOutcome.exitCode());
    System.out.println("STDOUT:\n" + testOutcome.stdOut());
    System.out.println();
    System.out.println("STDERR:\n" + testOutcome.stdErr());
    System.out.println("---------------------------------");
  }

  /**
   * Adds the failure of one user test
   *
   * @param failedTest The info about the failed test
   * @param testOutcome The outcome of the parser
   */
  public void reportFailureUserTests(TestInfo failedTest, TestOutcome testOutcome) {
    this.currentNumberUserTests++;
    this.numberFailedUserTests++;
    System.out.println("---------------------------------");
    System.out.println(
        "USER TEST #" + currentNumberUserTests + "/" + totalNumberUserTests + " FAILED");
    System.out.println("Test file path: " + failedTest.getTestFilePath());
    System.out.println(failedTest.toString());
    System.out.println("Your outcome: " + testOutcome.outcome().toString());
    System.out.println();
    System.out.println("Exit code: " + testOutcome.exitCode());
    System.out.println("STDOUT:\n" + testOutcome.stdOut());
    System.out.println();
    System.out.println("STDERR:\n" + testOutcome.stdErr());
    System.out.println("---------------------------------");
  }

  @Override
  public String toString() {
    String result =
        "TEST RESULTS:"
            + "\nNumber of Compliance Tests: "
            + this.totalNumberComplianceTests
            + "\nNumber of Compliance Tests Failed: "
            + this.numberFailedComplianceTests
            + "\nNumber of Google-specific Tests: "
            + this.totalNumberGoogleTests
            + "\nNumber of Google-specific Tests Failed: "
            + this.numberFailedGoogleTests
            + "\nNumber of user's tests: "
            + this.totalNumberUserTests
            + "\nNumber of user's tests failed: "
            + this.numberFailedUserTests;

    if (this.numberFailedComplianceTests == 0) {
      if (this.numberFailedGoogleTests == 0) {
        result =
            result
                + "\n\n"
                + "The parser FOLLOWS the standard and adheres to Google's specifications";
      } else {
        result =
            result
                + "\n\n"
                + "The parser FOLLOWS the standard but do not adhere to Google's specifications";
      }
    } else {
      result = result + "\n\n" + "The parser DOES NOT FOLLOW the standard";
    }

    return result;
  }
}
