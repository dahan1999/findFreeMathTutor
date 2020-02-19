from ctypes import cdll
from ctypes import c_char_p
lib = cdll.LoadLibrary('./libseshat.so')
strokes2Math = lib.strokes2Math
strokes2Math.restype = c_char_p

