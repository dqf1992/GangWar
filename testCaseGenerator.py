import random
import os
import numpy #version 1.11.2
import unittest
import time

#create folder
testcases="testcases2"
currentDirectory = os.getcwd()
os.chdir(currentDirectory)
try:
    os.mkdir(testcases)
except :
    print "Runing test suite ... For creating test case delete folder!"
os.chdir(testcases)

#boundaries of testCases
N=26
problemType = ["ALPHABETA"]
player = ["X","O"]
possibleMoves = [".","X","O"]
cellValue = 99
numberOfTestCases = 100
depthLimit = 3
minMaxDepthLimit = 3

#expected output generator
class TestAI(unittest.TestCase):

    def testCaseGenerator(self):
        #testcase generator
        for eachTestCase in range(numberOfTestCases):
            nameOfInputFile = "input" + str(eachTestCase)+".txt"
            gridSize = random.randint(2,N)
            grid =""
            for i in range(gridSize):
                #row Generator
                eachRow =""
                for j in range(gridSize):
                    eachRow = eachRow + " "+str(random.randint(1,cellValue))
                eachRow = eachRow + "\n"
                grid = grid + eachRow.strip(" ")
            
            gridAquired = ""
            for i in range(gridSize):
                #row Generator
                eachRow =""
                for j in range(gridSize):
                    #X O . terms Generator with probablities
                    eachRow = eachRow +str(numpy.random.choice(possibleMoves, p=[1, 0, 0]))
                eachRow = eachRow + "\n"
                gridAquired = gridAquired + eachRow.strip(" ")
            gridAquired = gridAquired.strip(" ")
            
            youPlay = numpy.random.choice(player)
            algorithm = numpy.random.choice(problemType,)
            depth = 1
            
            #Don't get large problems ... 
            if gridSize > 1 and  gridSize <= 6 :
                depth = numpy.random.choice([6])
            elif gridSize > 6 and gridSize <= 9:
                depth = numpy.random.choice([5])
            elif  gridSize > 9 and gridSize <= 17:
                depth = numpy.random.choice([4])
            else:
                depth = numpy.random.choice([3])
            
            question = str(gridSize)+"\n"+algorithm+"\n"+youPlay+"\n"+str(depth)+"\n"+grid+gridAquired
            inputFile = open(nameOfInputFile,"w")
            inputFile.write(question)
            inputFile.close()
                
        #testcase generator ends
        os.chdir(currentDirectory)
        os.system('cp src/*.java .')
        os.system('javac homework.java')
        os.chdir(testcases);
        os.system('mv ../*.class .')
        # test case output generator
        for eachTestCase in range(numberOfTestCases):
            print "Test case working on "+str(eachTestCase)
            nameOfInputFile = "input" + str(eachTestCase)+".txt"
            nameOfOutputFile = "output" + str(eachTestCase)+".txt"
            #takes input from nameOfInputFile and writes to nameOfOutputFile
            print("-->On test case #{0}<--".format(eachTestCase))
            start_time = time.time();
            os.system('java homework '+ nameOfInputFile + ' ' + nameOfOutputFile + ' > /dev/null')
            print("Running time: {0}ms".format(int((time.time() - start_time) * 1000)))
            
        
if __name__ == '__main__':
    unittest.main()      