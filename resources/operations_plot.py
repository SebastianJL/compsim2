#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Oct  6 16:41:03 2017

@author: johannes
"""

from pylab import *

nParticles = [10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0]

swaps = [0.22590361445783133, 1.0217469879518073, 1.851632530120482, 2.691337951807229, 3.5056217469879516, 4.335189120481927]

comparisons = [1.7512048192771084, 6.14355421686747, 10.337512048192771, 14.546965662650603, 18.61059548192771, 22.755539590361447]

partitions = [0.10361445783132531, 0.1724698795180723, 0.17989156626506025, 0.18171265060240963, 0.18041445783132531, 0.1799323313253012]

ax = plt.subplot(111)
#ax.set_xscale("log", nonposx='clip')
ax.set_xlabel("nParticles")
title("Asymptotic operation behaviour")

plot(nParticles, swaps, label='swaps/nParticles')
plot(nParticles, comparisons, label='comparisons/nParticles')
plot(nParticles, partitions, label='partitions/nParticles')
legend()

show()