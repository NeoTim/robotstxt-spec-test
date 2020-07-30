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

import com.google.search.robotstxt.spec.specification.SpecificationProtos;

/** Information about a specific test case */
public class TestInfo {
  private String robotsTxtContent;
  private SpecificationProtos.Expectation expectation;

  /** Default constructor */
  public TestInfo() {}

  /**
   * Constructor with parameters (sets all the fields
   *
   * @param robotsTxtContent The robots.txt file contents
   * @param expectation The structure from specification.proto (url, user-agent, expected outcome,
   *     explanation)
   */
  public TestInfo(String robotsTxtContent, SpecificationProtos.Expectation expectation) {
    this.robotsTxtContent = robotsTxtContent;
    this.expectation = expectation;
  }

  public String getRobotsTxtContent() {
    return robotsTxtContent;
  }

  public String getUrl() {
    return this.expectation.getTesturl();
  }

  public String getUserAgent() {
    return this.expectation.getUseragent();
  }

  public SpecificationProtos.Outcome getExpectedOutcome() {
    return this.expectation.getExpectedOutcome();
  }

  public String getAdditionalExplanation() {
    return this.expectation.getAdditionalExplanation();
  }

  public String toString() {
    return "The robots.txt content: \n\n"
        + this.robotsTxtContent
        + "\n\n"
        + "The URL: "
        + this.expectation.getTesturl()
        + "\n"
        + "The user-agent: "
        + this.expectation.getUseragent()
        + "\n"
        + "The expected outcome: "
        + this.expectation.getExpectedOutcome()
        + "\n"
        + "The additional explanation: "
        + this.expectation.getAdditionalExplanation()
        + "\n";
  }
}
