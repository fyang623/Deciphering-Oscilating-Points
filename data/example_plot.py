#!/usr/bin/env python3

import json
import matplotlib.pyplot as plt

data = []
with open("oscillator.json") as data_file:
    data = json.load(data_file)


plt.plot(data, 'o')
plt.show()

