#!/usr/local/bin python
# coding=utf-8

import os
import time
import re
import sys
import subprocess

currDir = os.getcwd();
player1Dir = os.path.join(currDir, "player1");
player2Dir = os.path.join(currDir, "player2");
player1 = 'X';
player2 = 'O';

os.system('python gameGenerator.py');
os.chdir(player1Dir);
os.system('make newgame');
os.chdir(player2Dir);
os.system('make newgame');
os.chdir(currDir);

def determineWinner(data, boardSize):
	val = 0;
	for i in xrange(4+boardSize, 4+2*boardSize):
		num = data[i-boardSize].split(' ');
		state = list(data[i]);
		for j in xrange(boardSize):
			if state[j] == 'X':
				val += int(num[j]);
			elif state[j] == 'O':
				val -= int(num[j]);
	print("");
	if val > 0:
		print('X win by ' + str(val));
	elif val < 0:
		print('O win by ' + str(-val));
	else:
		print('Game draw');

for i in xrange(1):
	fileName = "input" + str(i) + ".txt";
	# with open(fileName, 'r') as fin:
	# 	print fin.read();
	timeRemain1 = 200.0;
	timeRemain2 = 200.0;
	if i % 2 == 0:
		os.chdir(player1Dir);
		player = player1;
		timeRemain = timeRemain1;
	else:
		os.chdir(player2Dir);
		player = player2;
		timeRemain = timeRemain2;
	while True:
		os.system('cp ../' + fileName + ' input.txt');
		with open('input.txt', 'r') as fin:
			data = fin.readlines();
		data[2] = player + '\n';
		data[3] = str(timeRemain) + '\n';
		boardSize = int(data[0]);
		# assert len(data) == 4 + 2 * boardSize
		gameOver = True;
		for i in xrange(4 + boardSize, 4 + 2*boardSize):
			sys.stdout.write(data[i]);
			sys.stdout.flush();
			if re.match('(.*)\.(.*)', data[i]):
				gameOver = False;
		if gameOver: 
			determineWinner(data, boardSize);
			break;
		if timeRemain < 0:
			if player == player1:
				print(player1 + ' run out of time, winner is ' + player2);
			else:
				print(player2 + ' run out of time, winner is ' + player1);
			break;
		with open('input.txt', 'w') as fin:
			fin.writelines(data);

		# startTime = time.time();
		# os.system('time make run &> /dev/null');

		try:
		    retcode = subprocess.call("(time make run &>/dev/null) 2>time.log", shell=True)
		    # if retcode < 0:
		    #     print >>sys.stderr, "Child was terminated by signal", -retcode
		    # else:
		    #     print >>sys.stderr, "Child returned", retcode
		except OSError as e:
		    print >>sys.stderr, "Execution failed:", e
		# os.system('{time make run &>/dev/null;} 2>time.log');
		with open('time.log', 'r') as flog:
			log = flog.readlines();
		timeCPU = 0;
		for t in log:
			s = t.split('\t')
			if len(s) > 1 and (s[0] == 'sys' or s[0] == 'user') :
				timeCPU = float(s[1].split('m')[1].split('s')[0]) + timeCPU;
		

		timeRemain = timeRemain - timeCPU;
		print(timeRemain);

		# sys.stdout.write(log[1].split('\t')[0]);
		
		# os.system('echo $! > pid.txt');

		with open('output.txt', 'r') as fin:
			state = fin.readlines();

		for i in xrange(1, 1+boardSize):
			data[i+3+boardSize] = state[i];

		os.chdir(currDir);
		with open(fileName, 'w') as fin:
			fin.writelines(data)

		if player == player1:
			player = player2;
			os.chdir(player2Dir);
			timeRemain1 = timeRemain;
			timeRemain = timeRemain2;
		else:
			player = player1;
			os.chdir(player1Dir);
			timeRemain2 = timeRemain;
			timeRemain = timeRemain1;

os.chdir(player1Dir);
os.system('make clean');
os.chdir(player2Dir);
os.system('make clean');
		