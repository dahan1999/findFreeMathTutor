import mysql.connector
import pickle
from process_latex import process_sympy
from sympy import *

"""
mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    passwd="cll7494",
    database="mathVideoIndex"
)

mycursor = mydb.cursor()

sql_query = "select id,url, exp,videoId FROM mathVideoIndex.youtubeVideo where exp is not null and exp <> '';"
mycursor.execute(sql_query)
records = mycursor.fetchall()

with open('listfile.data', 'wb') as filehandle:
    # read the data as binary data stream
    pickle.dump(records, filehandle)

filehandle.close()
"""
# prepare symbolized expressions
def prepareSymbols():
    with open('listfile.data', 'rb') as filehandle:
        # read the data as binary data stream
        records2 = pickle.load(filehandle)
    filehandle.close()

    symbolizedExp = []
    for rec in records2:
        expList = []
        for e in rec[2].splitlines():
            try:
                expList.append(process_sympy(e))
            except:
                continue
        if expList:
            symbolizedExp.append({'vid':rec[3], 'exp':expList})
    return symbolizedExp

def calSimilatiry(expr1, expr2):
    args1 = preorder_traversal(expr1)

    args2 = preorder_traversal(expr2)

    similarity = 1

    for (arg1, arg2) in zip(args1, args2):
        if arg1.func == arg2.func:
            similarity *= 2
            similarity += 1
    return similarity

def searchExps(allSymExps, exp):
    similarPoints = []
    for sym in allSymExps:
        simPoint = 0
        for e in sym['exp']:
            tmp = calSimilatiry(e,exp)
            if tmp > simPoint:
                simPoint = tmp
        similarPoints.append({'vid': sym['vid'], 'point': simPoint})
    return similarPoints;


"""
# a regression test
for sym in symbolizedExp:
    if sym['exp']:
        max = 0
        vid = ''
        for e in searchExps(symbolizedExp, sym['exp'][0]):
            if e['point'] > max:
                max = e['point']
                vid = e['vid']
        if vid != sym['vid']:
            selfSim = calSimilatiry(sym['exp'][0], sym['exp'][0])
            print("vid %s does not match found vid: %s, self sim point is %s, max point is %s" % (sym['vid'], vid, selfSim, max))
"""