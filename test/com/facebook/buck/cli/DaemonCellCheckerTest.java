/*
 * Copyright 2019-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.facebook.buck.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.facebook.buck.core.config.BuckConfig;
import com.facebook.buck.core.config.FakeBuckConfig;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

public class DaemonCellCheckerTest {
  @Test
  public void testEqualsForDaemonRestart() {
    BuckConfig buckConfig =
        FakeBuckConfig.builder()
            .setSections(
                ImmutableMap.of(
                    "build", ImmutableMap.of("threads", "3"),
                    "cxx", ImmutableMap.of("cc", "/some_location/gcc")))
            .build();
    BuckConfig buckConfigMoreThreads =
        FakeBuckConfig.builder()
            .setSections(
                ImmutableMap.of(
                    "build", ImmutableMap.of("threads", "4"),
                    "cxx", ImmutableMap.of("cc", "/some_location/gcc")))
            .build();
    BuckConfig buckConfigDifferentCompiler =
        FakeBuckConfig.builder()
            .setSections(
                ImmutableMap.of(
                    "build", ImmutableMap.of("threads", "3"),
                    "cxx", ImmutableMap.of("cc", "/some_location/clang")))
            .build();

    assertFalse(buckConfig.equals(buckConfigMoreThreads));
    assertFalse(buckConfig.equals(buckConfigDifferentCompiler));

    assertTrue(DaemonCellChecker.equalsForDaemonRestart(buckConfig, buckConfigMoreThreads));
    assertFalse(DaemonCellChecker.equalsForDaemonRestart(buckConfig, buckConfigDifferentCompiler));
    assertFalse(
        DaemonCellChecker.equalsForDaemonRestart(
            buckConfigMoreThreads, buckConfigDifferentCompiler));
  }
}
