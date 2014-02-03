# This Makefile is just a convenience.  It bounces all requests to the Makefile
# in the parent directory.

.PHONY: default style check clean

SHELL = bash

default:
	$(MAKE) -C .. default

check:
	$(MAKE) -C .. check

blackbox:
	$(MAKE) -C .. blackbox

unit:
	$(MAKE) -C .. unit

clean:
	$(MAKE) -C .. clean

style:
	$(MAKE) -C .. style
