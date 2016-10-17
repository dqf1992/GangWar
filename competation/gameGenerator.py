import random
import os
import numpy #version 1.11.2
import unittest
import time

#create folder

#boundaries of testCases
N=15
problemType = ["COMPETITION"]
player = ["X","O"]
possibleMoves = [".","X","O"]
cellValue = 99
numberOfTestCases = 5

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
                if i != gridSize -1:
                    eachRow = eachRow + "\n"
                gridAquired = gridAquired + eachRow.strip(" ")
            gridAquired = gridAquired.strip(" ")
            
            youPlay = numpy.random.choice(player)
            algorithm = numpy.random.choice(problemType,)
            
            #Don't get large problems ... 
            depth = 200.0
            
            question = str(gridSize)+"\n"+algorithm+"\n"+youPlay+"\n"+str(depth)+"\n"+grid+gridAquired
            inputFile = open(nameOfInputFile,"w")
            inputFile.write(question)
            inputFile.close()
                
        #testcase generator ends
            
        
if __name__ == '__main__':
    unittest.main()      