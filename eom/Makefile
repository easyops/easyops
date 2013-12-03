basedir = .
test_dir = ./test
test_command=C:\Users\tintin\node_modules\.bin\mocha.cmd
use_should=--require should
test_reporter=--reporter spec
coverage_file=./coverage.html
use_blanket = --require blanket
timeout = --timeout 5000
#use_debug= --debug-brk
use_debug=

cmd = $(test_command) $(use_debug) $(timeout) $(use_should) $(test_reporter)

test:
	$(cmd) $(test_dir)
test_res:	
	$(cmd)  $(test_dir)/lib/res
test_bizz:
	$(cmd)  $(test_dir)/lib/bizz
	
test_mw:
	$(cmd)  $(test_dir)/lib/bizz/mw.js
	
test_host:
	$(cmd) $(test_dir)/lib/bizz/host.js
	
coverage:
	$(test_command) $(use_blanket) -R html-cov > $(coverage_file)
	
.PHONY: test test_res test_bizz coverage