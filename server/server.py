from websocket_server import WebsocketServer
import random
import pickle
from process_latex import process_sympy
from sympy import *
from ctypes import cdll
from ctypes import c_char_p
lib = cdll.LoadLibrary('./libseshat.so')
strokes2Math = lib.strokes2Math
strokes2Math.restype = c_char_p


# Called for every client connecting (after handshake)
def new_client(client, server):
    print("New client connected and was given id %d" % client['id'])
    server.send_message_to_all("Hey all, a new client has joined us")


# Called for every client disconnecting
def client_left(client, server):
    print("Client(%d) disconnected" % client['id'])

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

def searchSymExps(allSymExps, exp):
    similarPoints = []
    for sym in allSymExps:
        simPoint = 0
        for e in sym['exp']:
            tmp = calSimilatiry(e,exp)
            if tmp > simPoint:
                simPoint = tmp
        similarPoints.append({'vid': sym['vid'], 'point': simPoint})
    max = 0
    vid = ''
    for p in similarPoints:
        if p['point'] > max:
            max = p['point']
            vid = p['vid']
    return {'vid': vid, 'point': max}

# Called when a client sends a message
def message_received(client, server, message):
    if not hasattr(message_received, "records"):
        #message_received.records = ["tyoPXhzGzrE", "thrwb-JvVSM", "AJdCfaGjWlk", "EYG1XvNUZF0", "ieiRIATCOUI"]
        message_received.records = prepareSymbols()

    #print("Client(%d) said: %s" % (client['id'], message))
    inputExp = strokes2Math(message.encode('ascii'))
    print(inputExp)
    foundVideo = searchSymExps(message_received.records, process_sympy(inputExp.decode("utf-8") ))
    server.send_message(client, foundVideo['vid'])




PORT = 9001
server = WebsocketServer(PORT)
server.set_fn_new_client(new_client)
server.set_fn_client_left(client_left)
server.set_fn_message_received(message_received)
server.run_forever()
