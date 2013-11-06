basedir = .
test_dir = ./test
test_command=C:\Users\tintin\node_modules\.bin\mocha.cmd
use_should=--require should
test_reporter=--reporter spec
coverage_file=./coverage.html
use_blanket = --require blanket
#use_debug= --debug-brk
use_debug=

test:
	$(test_command) $(use_debug) $(use_should) $(test_reporter) $(test_dir)
test_res:	
	$(test_command) $(use_debug) $(use_should) $(test_reporter) $(test_dir)/lib/res
	
coverage:
	$(test_command) $(use_blanket) -R html-cov > $(coverage_file)
	
.PHONY: test test_res coverage