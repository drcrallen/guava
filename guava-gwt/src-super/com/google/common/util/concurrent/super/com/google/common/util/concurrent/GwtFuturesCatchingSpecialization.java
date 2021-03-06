/*
 * Copyright (C) 2006 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures.AsyncCatchingFuture;
import com.google.common.util.concurrent.Futures.CatchingFuture;

import java.util.concurrent.Executor;

/**
 * Hidden superclass of {@link Futures} that provides us a place to declare special GWT versions of
 * the {@link Futures#catching(ListenableFuture, Class, com.google.common.base.Function)
 * Futures.catching} family of methods. Those versions have slightly different signatures.
 */
abstract class GwtFuturesCatchingSpecialization {
  /*
   * In the GWT versions of the methods (below), every exceptionType parameter is required to be
   * Class<Throwable>. To handle only certain kinds of exceptions under GWT, you'll need to write
   * your own instanceof tests.
   */

  public static <V> ListenableFuture<V> catching(
      ListenableFuture<? extends V> input,
      Class<Throwable> exceptionType,
      Function<? super Throwable, ? extends V> fallback) {
    return catching(input, exceptionType, fallback, directExecutor());
  }

  public static <V> ListenableFuture<V> catching(
      ListenableFuture<? extends V> input,
      Class<Throwable> exceptionType,
      Function<? super Throwable, ? extends V> fallback,
      Executor executor) {
    CatchingFuture future = new CatchingFuture<V, Throwable>(input, exceptionType, fallback);
    input.addListener(future, executor);
    return future;
  }

  public static <V> ListenableFuture<V> catchingAsync(
      ListenableFuture<? extends V> input,
      Class<Throwable> exceptionType,
      AsyncFunction<? super Throwable, ? extends V> fallback) {
    return catchingAsync(input, exceptionType, fallback, directExecutor());
  }

  public static <V> ListenableFuture<V> catchingAsync(
      ListenableFuture<? extends V> input,
      Class<Throwable> exceptionType,
      AsyncFunction<? super Throwable, ? extends V> fallback,
      Executor executor) {
    AsyncCatchingFuture<V, Throwable> future =
        new AsyncCatchingFuture<V, Throwable>(input, exceptionType, fallback);
    input.addListener(future, executor);
    return future;
  }
}
