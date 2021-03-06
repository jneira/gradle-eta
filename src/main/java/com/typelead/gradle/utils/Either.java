package com.typelead.gradle.utils;

import java.util.function.Function;

/**
 * Right-biased Either
 */
public abstract class Either<L, R> {

  public static <L, R> Either<L, R> left(L l) {
    //noinspection unchecked
    return (Either<L, R>) new Left<>(l);
  }

  public static <L, R> Either<L, R> right(R r) {
    //noinspection unchecked
    return (Either<L, R>) new Right<>(r);
  }

  public static <X> X merge(Either<? extends X, ? extends X> e) {
    return e.fold(x -> x, x -> x);
  }

  abstract public <X> Either<L, X> map(Function<R, X> f);

  abstract public <X> Either<L, X> flatMap(Function<R, Either<L, X>> f);

  abstract public <X> X fold(Function<L, X> f, Function<R, X> g);

  abstract public <X> Either<X, R> leftMap(Function<L, X> f);

  abstract public R valueOr(Function<L, R> f);

  abstract public Either<R, L> swap();

  public static final class Left<L> extends Either<L, Object> {

    final L value;

    Left(L value) {
      this.value = value;
    }

    @Override
    public <X> Either<L, X> map(Function<Object, X> f) {
      //noinspection unchecked
      return (Either<L, X>) this;
    }

    @Override
    public <X> Either<L, X> flatMap(Function<Object, Either<L, X>> f) {
      //noinspection unchecked
      return (Either<L, X>) this;
    }

    @Override
    public <X> X fold(Function<L, X> f, Function<Object, X> g) {
      return f.apply(value);
    }

    @Override
    public <X> Either<X, Object> leftMap(Function<L, X> f) {
      return new Left<>(f.apply(value));
    }

    @Override
    public Object valueOr(Function<L, Object> f) {
      return f.apply(value);
    }

    @Override
    public Either<Object, L> swap() {
      return new Right<>(value);
    }
  }

  public static final class Right<R> extends Either<Object, R> {

    final R value;

    Right(R value) {
      this.value = value;
    }

    @Override
    public <X> Either<Object, X> map(Function<R, X> f) {
      return new Right<>(f.apply(value));
    }

    @Override
    public <X> Either<Object, X> flatMap(Function<R, Either<Object, X>> f) {
      return f.apply(value);
    }

    @Override
    public <X> X fold(Function<Object, X> f, Function<R, X> g) {
      return g.apply(value);
    }

    @Override
    public <X> Either<X, R> leftMap(Function<Object, X> f) {
      //noinspection unchecked
      return (Either<X, R>) this;
    }

    @Override
    public R valueOr(Function<Object, R> f) {
      return value;
    }

    @Override
    public Either<R, Object> swap() {
      return new Left<>(value);
    }
  }
}
