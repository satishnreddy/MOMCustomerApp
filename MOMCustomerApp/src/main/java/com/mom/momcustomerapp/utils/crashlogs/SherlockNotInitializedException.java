package com.mom.momcustomerapp.utils.crashlogs;

public class SherlockNotInitializedException extends RuntimeException {
  public SherlockNotInitializedException() {
    super("Initialize Sherlock using Sherlock.init(context) before using its methods");
  }
}
