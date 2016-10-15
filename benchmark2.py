#!/usr/local/bin python
# coding=utf-8

import os
import time

os.system('rm -rf results')
os.system('mkdir -p results')
os.system('cp src/*.java .')
os.system('javac homework.java')
for i in xrange(100):
    os.system('cp ./testcases/{0}.in ./input.txt'.format(i))
    os.system('cp ./input.txt ./results/input{0}.txt'.format(i))
    print("-->On test case #{0}<--".format(i))
    start_time = time.time();
    os.system('java homework > /dev/null')
    print("Running time: {0}ms".format(int((time.time() - start_time) * 1000)))
    os.system('diff ./output.txt ./testcases/{0}.out'.format(i))
    os.system('cp ./output.txt ./results/output{0}.txt'.format(i))

os.system('rm input.txt output.txt *.java *.class')