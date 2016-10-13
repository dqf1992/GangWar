#!/usr/local/bin python
# coding=utf-8

import os

os.system('mkdir -p cases')

for i in xrange(1, 11):
	print("-->Copying test case #{0}<--".format(i));
	os.system('cp ./Test/Test{0}/input.txt ./cases/input{0}.txt'.format(i))
	os.system('cp ./Test/Test{0}/output.txt ./cases/output{0}.txt'.format(i))